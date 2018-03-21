package de.jugh;

import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;

public class GameCamera extends Group
{

	//
	// Camera stuff.
	//
	private final PerspectiveCamera camera = new PerspectiveCamera(true);
	private final Xform cameraXform = new Xform();
	private final Xform cameraXform2 = new Xform();
	private final Xform cameraXform3 = new Xform();
	//
	// End Camera stuff.

	public GameCamera() 
	{
		cameraXform.getChildren().add(cameraXform2);
		cameraXform2.getChildren().add(cameraXform3);
		cameraXform3.getChildren().add(camera);
		cameraXform3.setRotateZ(180.0);

		camera.setNearClip(GameProperties.get().getDoubleProperty("game.camera.clip.near"));
		camera.setFarClip(GameProperties.get().getDoubleProperty("game.camera.clip.far"));
		camera.setTranslateZ(GameProperties.get().getDoubleProperty("game.camera.distance"));
		cameraXform.ry.setAngle(GameProperties.get().getDoubleProperty("game.camera.yangle"));
		cameraXform.rx.setAngle(GameProperties.get().getDoubleProperty("game.camera.xangle"));

		getChildren().add(cameraXform);
	}

	public PerspectiveCamera getCamera()
	{
		return camera;
	}

}
