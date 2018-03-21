package de.jugh.move;

import org.junit.Assert;
import org.junit.Test;

import javafx.scene.Node;
import javafx.scene.shape.Box;

/**
 * 
 * @author Daniel (d.ludwig@micromata.de)
 *
 */
public class MovementXZPlaneTest
{
	@Test
	public void angle01() throws InterruptedException
	{
		Node moveThis = new Box(10, 10, 10);
		MovementXZPlane mov = new MovementXZPlane(10, 10, 45, moveThis);

		mov.startMovement();
		Thread.sleep(50);
		mov.updateMovement();

		double x = mov.getxMove();
		double z = mov.getzMove();

		Assert.assertFalse(x < 0.0);
		Assert.assertFalse(z < 0.0);
		Assert.assertEquals((int) x, (int) z);

		mov = new MovementXZPlane(10, 10, 225, moveThis);
		mov.startMovement();
		Thread.sleep(50);
		mov.updateMovement();

		x = mov.getxMove();
		z = mov.getzMove();

		System.out.println(x + " " + z);

		Assert.assertTrue(x < 0.0);
		Assert.assertTrue(z < 0.0);
		Assert.assertEquals((int) x, (int) z);
	}

	@Test
	public void angle02() throws InterruptedException
	{
		Node moveThis = new Box(10, 10, 10);
		MovementXZPlane mov = new MovementXZPlane(10, 10, 10, moveThis);

		mov.startMovement();
		Thread.sleep(50);
		mov.updateMovement();

		double x = mov.getxMove();
		double z = mov.getzMove();
		
		System.out.println(x + " " + z);
		
		mov = new MovementXZPlane(10, 10, 350, moveThis);

		mov.startMovement();
		Thread.sleep(50);
		mov.updateMovement();
		
		x = mov.getxMove();
		z = mov.getzMove();
		
		System.out.println(x + " " + z);
	}
}
