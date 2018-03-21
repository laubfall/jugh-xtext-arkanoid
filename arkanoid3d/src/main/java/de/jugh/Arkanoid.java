package de.jugh;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.math3.geometry.euclidean.threed.Plane;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import de.jugh.content.Game;
import de.jugh.content.GameContentLoader;
import de.jugh.move.DetectedCollision;
import de.jugh.move.IMovement;
import de.jugh.move.MovementXZPlane;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.collections.transformation.FilteredList;
import javafx.scene.DepthTest;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.stage.Stage;
import javafx.util.Pair;

public class Arkanoid extends Application
{
	//
	// World content
	//
	private final Group root = new Group();
	private final Xform world = new Xform();
	private Box player;
	private Bullet bullet;
	private Game gameContent;

	//
	// End World content;
	//

	//
	// Animation vars beyond that point.
	//
	private long frameCounter = 0;
	private long currentFrame = 0;

	private KeyCode currentPressedKey;
	//
	// <-- End Animation vars
	//

	//
	// Collision Box
	//
	private final Group collisionBoxGroup = new Group();

	//
	// Movements for entities
	//
	private Map<Node, List<IMovement>> movements = new HashMap<>();

	public static void main(String[] arg)
	{
		launch(arg);
	}

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		root.getChildren().add(world);
		root.setDepthTest(DepthTest.ENABLE);

		root.getChildren().add(collisionBoxGroup);

		final GameCamera gameCam = new GameCamera();
		root.getChildren().add(gameCam);

		buildGameContent();
		
		Scene scene = new Scene(root, GameProperties.get().getDoubleProperty("app.scene.width"),
				GameProperties.get().getDoubleProperty("app.scene.height"), true);
		scene.setFill(Color.BLACK);

		primaryStage.setTitle("Arkanoid");
		primaryStage.setScene(scene);
		primaryStage.show();

		scene.setCamera(gameCam.getCamera());

		buildControls(scene);

		buildGameTimer();
//		buildBoundingBoxes();
	}

	private void buildGameContent()
	{
		GameContentLoader gameContentLoader = new GameContentLoader();
		gameContent = gameContentLoader.createGame();
		world.getChildren().add(gameContent);
	}

	private void buildGameTimer()
	{
		final AnimationTimer animationTimer = new GameTimer();
		animationTimer.start();
	}

	private void refineCollisionMovement(final List<DetectedCollision> collisions)
	{
		collisions.forEach(collision -> {
			final Node collider = collision.getCollider();
			List<IMovement> moves = movements.get(collider);
			moves.forEach(m -> {
				double percent = 0.9;
				while (collision.getCollisionDetectedBy().get().isPresent() && percent > 0.0) {
					m.changeMovement(percent);
					m.updateMovement();
					percent -= 0.1;
				}
				m.applyMovement();
			});
		});
	}

	private void applyMovements()
	{
		movements.values().forEach(movements -> movements.forEach(move -> {
			if (move.isStarted()) {
				move.updateMovement();
				move.applyMovement();
			}
		}));
	}

	private List<DetectedCollision> collisionDetection()
	{
		final List<DetectedCollision> result = new ArrayList<>();
		collisionDetectionPlayer().ifPresent(result::add);
		collisionDetectionBullet().ifPresent(result::add);
		return result;
	}

	private Optional<DetectedCollision> collisionDetectionBullet()
	{
		// detect collision by the bullet with the outer borders.
		FilteredList<Node> filtered = collisionBoxGroup.getChildren()
				.filtered(border -> border.getBoundsInParent().intersects(bullet.getBoundsInParent()));
		if (filtered.isEmpty() == false) {
			return Optional.of(new DetectedCollision(bullet, filtered, this::collisionDetectionBullet));
		}

		return Optional.ofNullable(null);
	}

	private Optional<DetectedCollision> collisionDetectionPlayer()
	{
		// detect collision by the player object with the outer borders.
		FilteredList<Node> filtered = collisionBoxGroup.getChildren()
				.filtered(border -> border.getBoundsInParent().intersects(player.getBoundsInParent()));
		if (filtered.isEmpty() == false) {
			player.setDrawMode(DrawMode.LINE);
		} else {
			player.setDrawMode(DrawMode.FILL);
		}

		return Optional.ofNullable(null);
	}

	private void buildControls(Scene scene)
	{
		scene.setOnKeyPressed(event -> {
			currentPressedKey = event.getCode();
		});
	}

	/**
	 * Four boxes that define a rectangle centered around the origin. The boxes are not visible and only used to detect
	 * collision with the bullet.
	 * 
	 */
	private void buildBoundingBoxes()
	{
		// left
		Box border = new Box(10, 1000, 1000);
		border.setTranslateX(-500);
		collisionBoxGroup.getChildren().add(border);
		// right
		border = new Box(10, 1000, 1000);
		border.setTranslateX(500);
		collisionBoxGroup.getChildren().add(border);

		// top
		border = new Box(1000, 1000, 10);
		border.setTranslateZ(500);
		collisionBoxGroup.getChildren().add(border);

		// bottom
		border = new Box(1000, 1000, 10);
		border.setTranslateZ(-500);
		collisionBoxGroup.getChildren().add(border);

	}

	private void buildPlayer()
	{
		player = new Box(80, 20, 20);
		world.getChildren().add(player);
	}

	private void buildBullet()
	{
		bullet = new Bullet(10);
		final PhongMaterial mat = new PhongMaterial(Color.WHITE);
		bullet.setMaterial(mat);
		world.getChildren().add(bullet);

		movements.computeIfAbsent(bullet, b -> Arrays.asList(new MovementXZPlane(10, 1000, 10.0, b)));
	}

	class GameTimer extends AnimationTimer
	{

		@Override
		public void handle(long now)
		{
			frameTracking();

			keyHandling();

//			movementAndCollisionHandling();
		}

		private void movementAndCollisionHandling()
		{
			// 1. calculate movements and apply them to the entities
			applyMovements();

			// 2. calculate collisions
			final List<DetectedCollision> detectedCollisions = collisionDetection();

			// 3. refine movement in case of existing collisions
			refineCollisionMovement(detectedCollisions);

			// 4. apply the new movement based on the last collision
			detectedCollisions.forEach(dc -> {
				// TODO keep it more generic
				List<IMovement> old = movements.get(bullet);
				MovementXZPlane oldXz = (MovementXZPlane) old.get(0);

				// TODO take care of further colliders
				final List<Pair<Plane, Plane>> opposingPlanes = JfxMath.opposingPlanes(bullet.getBoundsInParent(),
						dc.getColliders().get(0).getBoundsInParent());

				// calculate the movement vector and check the angle
				final Vector3D movementVec3d = new Vector3D(oldXz.getxMove(), 0, oldXz.getzMove());

				// TODO
				double angle = Vector3D.angle(opposingPlanes.get(1).getValue().getNormal(), movementVec3d);

				// TODO calculate the new angle. The new one depends on the angle to the collision face
				MovementXZPlane movement = new MovementXZPlane(10, 1000 * 3, 2 * Math.toDegrees(angle), bullet);
				movements.put(dc.getCollider(), new ArrayList<>());
				movements.get(dc.getCollider()).add(movement);
				movement.startMovement();
			});
		}

		private void frameTracking()
		{
			// keep track of the frames and perform some debug logging
			if (frameCounter + 60 < currentFrame) {
				frameCounter = currentFrame;
				if (currentPressedKey != null) {
					System.out.println("last pressed key: " + currentPressedKey);
				}
			}
			currentFrame++;
		}

		private void keyHandling()
		{
			if (KeyCode.A.equals(currentPressedKey)) {
				player.setTranslateX(player.getTranslateX() + 10);
			} else if (KeyCode.D.equals(currentPressedKey)) {
				player.setTranslateX(player.getTranslateX() - 10);
			} else if (KeyCode.SPACE.equals(currentPressedKey)) {
				movements.get(bullet).forEach(move -> move.startMovement());
			} else if(KeyCode.LEFT.equals(currentPressedKey)) {
				gameContent.previousLevel();
			} else if(KeyCode.RIGHT.equals(currentPressedKey)) {
				gameContent.nextLevel();
			}

			currentPressedKey = null;
		}

	}
}
