package de.jugh.move;

import javafx.animation.Interpolator;
import javafx.scene.Node;

/**
 * Linear Movement in the XZ-Plane.
 * 
 * @author Daniel (d.ludwig@micromata.de)
 *
 */
public class MovementXZPlane implements IMovement
{
	// The wanted translation
	private double movement;

	// Time needed for the full translation
	private long duration;

	private double angle;

	private Interpolator interpolator = Interpolator.LINEAR;

	private Node moveThis;

	private long startTime;

	private boolean started;

	private double xMove;

	private double zMove;

	private double lastXMove;

	private double lastZMove;

	public MovementXZPlane(double movement, long duration, double angle, Node moveThis) {
		super();
		this.movement = movement;
		this.duration = duration;
		this.angle = angle;
		this.moveThis = moveThis;
		xMove = moveThis.getTranslateX();
		zMove = moveThis.getTranslateZ();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jugh.move.IMovement#startMovement()
	 */
	@Override
	public final void startMovement()
	{
		startTime = System.currentTimeMillis();
		started = true;
	}

	@Override
	public void changeMovement(double percentage)
	{
		xMove = (xMove - lastXMove) * percentage + lastXMove;
		zMove = (zMove - lastZMove) * percentage + lastZMove;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jugh.move.IMovement#updateMovement()
	 */
	@Override
	public final void updateMovement()
	{
		double z = Math.sin(Math.toRadians(angle));
		double x = Math.cos(Math.toRadians(angle));

		double percDone = (double) (System.currentTimeMillis() - startTime) / (double) duration;
		double interpolatedValue = interpolator.interpolate(0, movement, percDone);

		lastXMove = xMove;
		lastZMove = zMove;

		xMove = xMove + x * interpolatedValue;
		zMove = zMove + z * interpolatedValue;

	}

	@Override
	public void applyMovement()
	{
		moveThis.setTranslateX(xMove);
		moveThis.setTranslateZ(zMove);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jugh.move.IMovement#isStarted()
	 */
	@Override
	public boolean isStarted()
	{
		return started;
	}

	public double getAngle()
	{
		return angle;
	}

	public double getzMove()
	{
		return zMove;
	}

	public double getxMove()
	{
		return xMove;
	}

}
