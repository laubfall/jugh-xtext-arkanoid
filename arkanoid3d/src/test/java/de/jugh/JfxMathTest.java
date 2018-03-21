package de.jugh;

import java.util.List;

import org.apache.commons.math3.geometry.euclidean.threed.Plane;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.Assert;
import org.junit.Test;

import de.jugh.JfxMath;
import javafx.geometry.Bounds;
import javafx.scene.shape.Box;
import javafx.util.Pair;

/**
 * Tests {@link JfxMath}.
 * 
 * @author Daniel (d.ludwig@micromata.de)
 *
 */
public class JfxMathTest
{
	@Test
	public void opposingPlanes01()
	{
		Box b1 = new Box(10, 10, 10);
		Box b2 = new Box(10, 10, 10);
		b2.setTranslateX(40);
		b2.setTranslateZ(5);

		List<Pair<Plane, Plane>> opposingPlanes = JfxMath.opposingPlanes(b1.getBoundsInParent(),
				b2.getBoundsInParent());
		Assert.assertNotNull(opposingPlanes);
		Assert.assertEquals(2, opposingPlanes.size());

		opposingPlanes
				.forEach(pair -> System.out.println(pair.getKey().getNormal() + " " + pair.getValue().getNormal()));

		b2.setTranslateX(-40);
		b2.setTranslateZ(-30);

		opposingPlanes = JfxMath.opposingPlanes(b1.getBoundsInParent(), b2.getBoundsInParent());
		Assert.assertNotNull(opposingPlanes);
		Assert.assertEquals(2, opposingPlanes.size());

		opposingPlanes
				.forEach(pair -> System.out.println(pair.getKey().getNormal() + " " + pair.getValue().getNormal()));
	}

	@Test
	public void planeNormalAngle01()
	{
		final Box box = new Box(10, 10, 5);
		box.setTranslateX(-80);
		final Bounds boundsInParent = box.getBoundsInParent();

		final Plane upPlane = JfxMath.upPlane(boundsInParent);
		final Vector3D normal = upPlane.getNormal();

		Assert.assertNotNull(normal);

		Vector3D movementVector = new Vector3D(-1, 0, 4);
		double angle = Vector3D.angle(normal, movementVector);
		System.out.println("Angle to UpPlane: " + Math.toDegrees(angle));
	}

	@Test
	public void planeNormalAngle02()
	{
		final Box box = new Box(10, 10, 5);
		box.setTranslateX(-80);
		final Bounds boundsInParent = box.getBoundsInParent();

		final Plane rightPlane = JfxMath.rightPlane(boundsInParent);
		final Vector3D normal = rightPlane.getNormal();

		Assert.assertNotNull(normal);
		Assert.assertEquals(1, (int) normal.getX());

		Vector3D movementVector = new Vector3D(-1, 0, 1);
		double angle = Vector3D.angle(normal, movementVector);

		System.out.println("Angle to RightPlane: " + Math.toDegrees(angle));
	}

	@Test
	public void planeNormalAngle03()
	{
		final Box box = new Box(10, 10, 5);
		box.setTranslateX(-80);
		final Bounds boundsInParent = box.getBoundsInParent();

		final Plane rightPlane = JfxMath.leftPlane(boundsInParent);
		final Vector3D normal = rightPlane.getNormal();

		Assert.assertNotNull(normal);
		Assert.assertEquals(-1, (int) normal.getX());

		Vector3D movementVector = new Vector3D(-1, 0, 1);
		double angle = Vector3D.angle(normal, movementVector);

		System.out.println("Angle to LeftPlane: " + Math.toDegrees(angle));
	}

	@Test
	public void planeNormalAngle04()
	{
		final Box box = new Box(10, 10, 5);
		box.setTranslateX(-80);
		final Bounds boundsInParent = box.getBoundsInParent();

		final Plane backPlane = JfxMath.backPlane(boundsInParent);
		final Vector3D normal = backPlane.getNormal();

		Assert.assertNotNull(normal);
		Assert.assertEquals(-1, (int) normal.getZ());

		Vector3D movementVector = new Vector3D(-1, 0, 1);
		double angle = Vector3D.angle(normal, movementVector);

		System.out.println("Angle to BackPlane: " + Math.toDegrees(angle));
	}

	@Test
	public void planeNormalAngle05()
	{
		final Box box = new Box(10, 10, 5);
		box.setTranslateX(-80);
		final Bounds boundsInParent = box.getBoundsInParent();

		final Plane frontPlane = JfxMath.frontPlane(boundsInParent);
		final Vector3D normal = frontPlane.getNormal();

		Assert.assertNotNull(normal);
		Assert.assertEquals(1, (int) normal.getZ());
		Assert.assertEquals(0, (int) normal.getY());
		Assert.assertEquals(0, (int) normal.getX());

		Vector3D movementVector = new Vector3D(-1, 0, 1);
		double angle = Vector3D.angle(normal, movementVector);

		System.out.println("Angle to FrontPlane: " + Math.toDegrees(angle));
	}
}
