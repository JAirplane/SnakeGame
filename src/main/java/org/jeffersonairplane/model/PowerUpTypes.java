package org.jeffersonairplane.model;

import lombok.Getter;

/**
 * All power up types in the game.
 * Main purpose is to create power ups dynamically.
 * Range from 0 to 100.
*/
@Getter
public enum PowerUpTypes {
    APPLE(0, 0),
	TAILCUTTER(0, 0);

    private int minChance;
	private int maxChance;
	
	private PowerUpTypes(int min, int max) {
		minChance = min;
		maxChance = max;
	}

    /**
	* Sets range of creation particular power up from 0 to 100.
	* All chances are zero by default, so should be set up manually using this method.
	* @param min is a lower limit of the range.
	 * @param max is higher limit of the range.
	*/
	public void setCreationChance(int min, int max) {
		if(min < 0 || max < 0 || max > 100 || min > max) return;
		minChance = min;
		maxChance = max;
	}
}
