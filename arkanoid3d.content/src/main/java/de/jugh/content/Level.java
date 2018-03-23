package de.jugh.content;

import javafx.scene.Group;
import javafx.scene.Node;

public class Level extends Group
{
	public void addRow(Row row) {
		// calculate the horizontal offset for every new row.
		double verticalOffset = (getChildren().size() - 1) * 3;
		
		for (Node n : getChildren()) {
			verticalOffset += 10;
		}

		row.setTranslateZ(verticalOffset*-1);
		getChildren().add(row);
	}
}
