package de.jugh;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.geometry.euclidean.threed.Plane;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import javafx.geometry.Bounds;
import javafx.util.Pair;

/**
 * Methods to make mathematical calculations easier when we have to deal with javafx structures.
 * 
 * @author Daniel (d.ludwig@micromata.de)
 *
 */
public class JfxMath
{
	public static double impactAngle(List<Pair<Plane, Plane>> opposingPlanes, Vector3D movementVector)
	{

		return 0.0;
	}

	/**
	 * Finds the two planes that faces each other. Actually only implemented for the left and right and front and
	 * backside of bounds.
	 * 
	 * @param bound1
	 * @param bound2
	 * @return list containing exactly 2 pairs of planes that faces each other. Never empty. Key always contains the
	 *         opposing Plane of the first bound parameter. The value contains the opposing plane of the second bound
	 *         parameter.
	 */
	public static List<Pair<Plane, Plane>> opposingPlanes(Bounds bound1, Bounds bound2)
	{
		final List<Pair<Plane, Plane>> result = new ArrayList<>();

		// wie weit ist b1.rechts und b2.links auseinander?
		Plane p1Right = rightPlane(bound1);
		Plane p2Left = leftPlane(bound2);
		double rightLeft = p1Right.getOffset(p2Left);
		// wie weit ist b1.links und b2.rechts auseinander?
		Plane p1Left = leftPlane(bound1);
		Plane p2Right = rightPlane(bound2);
		double leftRight = p1Left.getOffset(p2Right);

		if (rightLeft > leftRight) {
			result.add(new Pair<Plane, Plane>(p1Right, p2Left));
		} else if (rightLeft < leftRight) {
			result.add(new Pair<Plane, Plane>(p1Left, p2Right));
		}

		// wie weit ist b1.oben und b2.unten auseinander?
		// wie weit ist b1.unten und b2.oben auseinander?
		Plane p1Front = frontPlane(bound1);
		Plane p2Back = backPlane(bound2);
		double frontBack = p1Front.getOffset(p2Back);
		Plane p1Back = backPlane(bound1);
		Plane p2Front = frontPlane(bound2);
		double backFront = p1Back.getOffset(p2Front);

		if (frontBack > backFront) {
			result.add(new Pair<Plane, Plane>(p1Front, p2Back));
		} else if (frontBack < backFront) {
			result.add(new Pair<Plane, Plane>(p1Back, p2Front));
		}

		return result;
	}

	public static Plane upPlane(Bounds bound)
	{
		// create the three vectors that creates the plane. Starting with the upper left point of the plane
		// going further clockwise.
		final Vector3D v1 = new Vector3D(bound.getMinX(), bound.getMinY(), bound.getMinZ());
		final Vector3D v2 = new Vector3D(bound.getMinX(), bound.getMinY(), bound.getMinZ() + bound.getDepth());
		final Vector3D v3 = new Vector3D(bound.getMinX() + bound.getWidth(), bound.getMinY(), bound.getMinZ());

		return new Plane(v1, v2, v3, 0.0);
	}

	public static Plane rightPlane(Bounds bound)
	{
		// Starting with the upper right point
		final Vector3D v1 = new Vector3D(bound.getMinX() + bound.getWidth(), bound.getMinY(), bound.getMinZ());
		final Vector3D v2 = new Vector3D(bound.getMinX() + bound.getWidth(), bound.getMinY(),
				bound.getMinZ() + bound.getDepth());
		final Vector3D v3 = new Vector3D(bound.getMinX() + bound.getWidth(), bound.getMinY() - bound.getHeight(),
				bound.getMinZ() + bound.getDepth());

		return new Plane(v1, v2, v3, 0.0);
	}

	public static Plane leftPlane(Bounds bound)
	{
		// Starting with the upper left point.
		final Vector3D v1 = new Vector3D(bound.getMinX(), bound.getMinY(), bound.getMinZ());
		final Vector3D v2 = new Vector3D(bound.getMinX(), bound.getMinY(), bound.getMinZ() + bound.getDepth());
		final Vector3D v3 = new Vector3D(bound.getMinX(), bound.getMinY() - bound.getHeight(), bound.getMinZ());

		return new Plane(v3, v2, v1, 0.0);
	}

	public static Plane frontPlane(Bounds bound)
	{
		// Starting with the upper left point
		final Vector3D v1 = new Vector3D(bound.getMinX(), bound.getMinY(), bound.getMinZ());
		final Vector3D v2 = new Vector3D(bound.getMinX() + bound.getWidth(), bound.getMinY(), bound.getMinZ());
		final Vector3D v3 = new Vector3D(bound.getMinX() + bound.getWidth(), bound.getMinY() - bound.getHeight(),
				bound.getMinZ());

		return new Plane(v3, v2, v1, 0.0);
	}

	public static Plane backPlane(Bounds bound)
	{
		// Starting with the upper back left point
		final Vector3D v1 = new Vector3D(bound.getMinX(), bound.getMinY(), bound.getMinZ() + bound.getDepth());
		final Vector3D v2 = new Vector3D(bound.getMinX() + bound.getWidth(), bound.getMinY(),
				bound.getMinZ() + bound.getDepth());
		final Vector3D v3 = new Vector3D(bound.getMinX() + bound.getWidth(), bound.getMinY() - bound.getHeight(),
				bound.getMinZ() + bound.getDepth());

		return new Plane(v1, v2, v3, 0.0);
	}
}
