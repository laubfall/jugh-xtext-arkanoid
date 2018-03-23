package de.jugh.content;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Group;

/**
 * Game | - Level | - Brick
 * 
 * @author Daniel
 *
 */
public class Game extends Group
{
	private List<Level> levels = new ArrayList<Level>();

	private Level currentLevel;

	public void addLevel(Level level)
	{
		levels.add(level);
		currentLevel = level;
		getChildren().clear();
		getChildren().add(level);
	}

	public void nextLevel()
	{
		int indexOf = levels.indexOf(currentLevel);
		if (indexOf == 0 && levels.size() == 1) {
			return; // there is only one level so do nothing
		}

		if (indexOf == levels.size() - 1) {
			currentLevel = levels.get(0);
		} else {
			currentLevel = levels.get(indexOf + 1);
		}

		getChildren().clear();
		getChildren().add(currentLevel);
	}

	public void previousLevel()
	{
		int indexOf = levels.indexOf(currentLevel);
		if (indexOf == 0 && levels.size() == 1) {
			return; // there is only one level so do nothing
		}

		if (indexOf == 0) {
			currentLevel = levels.get(levels.size() - 1);
		} else {
			currentLevel = levels.get(indexOf - 1);
		}

		getChildren().clear();
		getChildren().add(currentLevel);
	}
}
