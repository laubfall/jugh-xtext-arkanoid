package de.jugh.loader;

import de.jugh.content.Brick;
import de.jugh.content.Game;
import de.jugh.content.Level;
import de.jugh.content.Row;
import javafx.scene.paint.Color;

/**
 * A class that "load" the game content.
 * 
 * @author Daniel (d.ludwig@micromata.de)
 *
 */
public class GameContentLoader
{
	public Game createGame()
	{
		final Game game = new Game();

		final Level l1 = new Level();
		game.addLevel(l1);

		final Row r1 = new Row();
		l1.addRow(r1);
		
		createSomeBricks(r1);

		final Row r2 = new Row();
		l1.addRow(r2);
		
		createSomeBricks(r2);
		
		final Level l2 = new Level();
		game.addLevel(l2);
		
		final Row r3 = new Row();
		createSomeBricks(r3);
		l2.addRow(r3);
		
		return game;
	}

	private void createSomeBricks(final Row r1)
	{
		for (int i = 0; i < 5; i++) {
			Brick b = new Brick();
			r1.addBrick(b);
			b.color(Color.BLUEVIOLET);
		}
	}
}
