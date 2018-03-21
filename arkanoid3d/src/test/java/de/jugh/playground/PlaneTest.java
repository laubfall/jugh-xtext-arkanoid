package de.jugh.playground;

import org.apache.commons.math3.geometry.euclidean.threed.Line;
import org.apache.commons.math3.geometry.euclidean.threed.Plane;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.Test;

import de.jugh.JfxMath;
import javafx.scene.shape.Box;

public class PlaneTest
{
	@Test
	public void parallelPlanes01()
	{
		Box b1 = new Box(10, 10, 10);
		Box b2 = new Box(10, 10, 10);
		b2.setTranslateX(50);

		Plane b1Plane = JfxMath.rightPlane(b1.getBoundsInParent());
		Plane b2Plane = JfxMath.leftPlane(b2.getBoundsInParent());

		System.out.println(Vector3D.angle(b1Plane.getNormal(), b2Plane.getNormal()));
		System.out.println("Offset: " + b1Plane.getOffset(b2Plane));

		b2Plane = JfxMath.upPlane(b2.getBoundsInParent());

		System.out.println(Vector3D.angle(b1Plane.getNormal(), b2Plane.getNormal()));
		System.out.println("Offset: " + b1Plane.getOffset(b2Plane));
		System.out.println(b1Plane.intersection(b2Plane));

		b2Plane = JfxMath.rightPlane(b2.getBoundsInParent());

		System.out.println(Vector3D.angle(b1Plane.getNormal(), b2Plane.getNormal()));
		System.out.println("Offset: " + b1Plane.getOffset(b2Plane));
	}

	@Test
	public void parallelPlanes02()
	{
		Box b1 = new Box(10, 10, 10);
		Box b2 = new Box(10, 10, 10);
		b2.setTranslateX(5);
		

		Plane b1Plane = JfxMath.rightPlane(b1.getBoundsInParent());
		Plane b2Plane = JfxMath.leftPlane(b2.getBoundsInParent());

		System.out.println(Vector3D.angle(b1Plane.getNormal(), b2Plane.getNormal()));
		System.out.println("Offset: " + b1Plane.getOffset(b2Plane));

		b2Plane = JfxMath.upPlane(b2.getBoundsInParent());

		System.out.println(Vector3D.angle(b1Plane.getNormal(), b2Plane.getNormal()));
		System.out.println("Offset: " + b1Plane.getOffset(b2Plane));
		System.out.println(b1Plane.intersection(b2Plane));

		b2Plane = JfxMath.rightPlane(b2.getBoundsInParent());

		System.out.println(Vector3D.angle(b1Plane.getNormal(), b2Plane.getNormal()));
		System.out.println("Offset: " + b1Plane.getOffset(b2Plane));
	}
}
