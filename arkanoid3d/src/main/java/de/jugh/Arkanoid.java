package de.jugh;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import de.jugh.content.Game;
import de.jugh.loader.GameContentLoader;
import de.jugh.move.IMovement;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.DepthTest;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.stage.Stage;

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

	private static Logger LOG = Logger.getLogger(Arkanoid.class);
	
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
	}

	private void buildGameContent()
	{
		final String loaderFQN = GameProperties.get().getProperty("game.content.loader");
		try {
			GameContentLoader gameContentLoader;
			gameContentLoader = (GameContentLoader) getClass().getClassLoader().loadClass(loaderFQN).newInstance();
			Game oldGameContent = gameContent;
			if (gameContent != null) {
				world.getChildren().remove(gameContent);
			}
			gameContent = gameContentLoader.createGame();
			if(gameContent == null) {
				gameContent = oldGameContent; // return to the previous state.
			}
			world.getChildren().add(gameContent);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			throw new ArkGameException("Failed to load content loader", e);
		}
	}

	private void buildGameTimer()
	{
		final AnimationTimer animationTimer = new GameTimer();
		animationTimer.start();
	}

	private void buildControls(Scene scene)
	{
		scene.setOnKeyPressed(event -> {
			currentPressedKey = event.getCode();
		});
	}

	class GameTimer extends AnimationTimer
	{

		@Override
		public void handle(long now)
		{
			frameTracking();

			keyHandling();
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
			} else if (KeyCode.LEFT.equals(currentPressedKey)) {
				gameContent.previousLevel();
			} else if (KeyCode.RIGHT.equals(currentPressedKey)) {
				gameContent.nextLevel();
			} else if(KeyCode.R.equals(currentPressedKey)) {
				LOG.info("Reloading game content");
				buildGameContent();
			}

			currentPressedKey = null;
		}

	}
}
