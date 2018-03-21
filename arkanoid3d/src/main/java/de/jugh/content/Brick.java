package de.jugh.content;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

/**
 * A typical arkanoid brick that can be destroyed.
 * 
 * @author Daniel (d.ludwig@micromata.de)
 *
 */
public class Brick extends Box
{

	public Brick() {
		super(30, 10, 10);
	}

	public final void color(Color color)
	{
		PhongMaterial phongMaterial = new PhongMaterial(color);
		setMaterial(phongMaterial);
	}
}
