package de.jugh.playground;

import org.junit.Assert;
import org.junit.Test;

import javafx.scene.Group;
import javafx.scene.shape.Box;

public class IntersectionTest
{
	@Test
	public void intersect01()
	{
		final Group world = new Group();
		Box box1 = new Box(10, 10, 10);
		world.getChildren().add(box1);
		Box box2 = new Box(5, 5, 5);
		world.getChildren().add(box2);

		Assert.assertTrue(box1.getBoundsInParent().intersects(box2.getBoundsInParent()));

		box2.setTranslateX(box2.getTranslateX() + 3);

		Assert.assertTrue(box1.getBoundsInParent().intersects(box2.getBoundsInParent()));

		box2.setTranslateX(box2.getTranslateX() + 13);

		Assert.assertFalse(box1.getBoundsInParent().intersects(box2.getBoundsInParent()));
	}
}
