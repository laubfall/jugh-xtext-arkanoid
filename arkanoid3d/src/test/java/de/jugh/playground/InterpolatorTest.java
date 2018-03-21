package de.jugh.playground;

import org.junit.Test;

import javafx.animation.Interpolator;

public class InterpolatorTest
{

	@Test
	public void interpolateDiscrete()
	{
		Interpolator dis = Interpolator.LINEAR;
		
		long time = System.currentTimeMillis();
		long expectedRuntime = 1000 * 3;
		long endTime = time + expectedRuntime;
		
		long startTime = time;
		int interpolate = 10; 
		while(time < endTime) {
			double percDone = (double)(time - startTime) / (double)expectedRuntime;
			System.out.println(time + " " + " " + endTime + " " + percDone);
			interpolate = dis.interpolate(10, 200000, percDone);
			System.out.println(interpolate);
			time = System.currentTimeMillis();
		}
	}

}
