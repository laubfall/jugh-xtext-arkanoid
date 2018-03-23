package de.jugh.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.scene.Group;
import javafx.scene.Node;

public class Row extends Group
{
	public void addBrick(Brick brick)
	{
		// calculate horizontal offset and apply it to the brick
		// initial value is the total spacing for all bricks.
		double horizontalOffset = (getChildren().size() - 1) * 3;
		for (Node n : getChildren()) {
			final Brick actual = (Brick) n;
			horizontalOffset += actual.widthProperty().doubleValue();
		}

		brick.setTranslateX(horizontalOffset);

		getChildren().add(brick);

		// List<Node> copy = new ArrayList<Node>(getChildren().size());
		// copy.addAll(getChildren());
		// Collections.reverse(copy);
		// getChildren().clear();
		// getChildren().addAll(copy);
	}

}
