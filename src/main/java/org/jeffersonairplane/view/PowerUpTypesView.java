package org.jeffersonairplane.view;

import lombok.*;

import java.awt.*;

/**
 * All power up types and its colors for painting.
*/
@Getter
public enum PowerUpTypesView {
	APPLE(Color.BLACK),
	TAILCUTTER(Color.BLACK);

	private Color color;
	
	PowerUpTypesView(Color color) {
		this.color = color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}