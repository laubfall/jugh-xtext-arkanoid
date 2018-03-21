package de.jugh.playground;

import org.apache.commons.math3.geometry.euclidean.threed.Plane;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.Test;

import de.jugh.JfxMath;
import javafx.scene.shape.Box;

public class CollisionPlaneTest
{
	@Test
	public void collisionPlane01()
	{
		final Box b1 = new Box(10, 10, 10);
		final Box b2 = new Box(10, 10, 10);
		
		/**
		 * ____
		 * |  |  b1
		 * ----
		 * 			____
		 * 			|	| b2
		 * 			----
		 */
		
		
		b2.setTranslateX(40);
		b2.setTranslateZ(40);
		
		Plane rightPlane = JfxMath.rightPlane(b1.getBoundsInParent());
		Vector3D pointAt = rightPlane.getPointAt(new Vector2D(3, 1), 0.0);
		
		
		System.out.println(pointAt);
		
	}
}
