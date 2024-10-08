package org.jeffersonairplane.model;

import java.util.*;

/**
* All power up types in the game.
* Main purpose is to create power ups dynamically.
*/
public enum PowerUpTypes {
    APPLE;
	
	private final List<Integer> percentageOfTypeCreation = new ArrayList<>();
	
	PowerUpTypes() {
		for(int i = 0; i < PowerUpTypes.values().length; i++) {
			percentageOfTypeCreation.add(0);
		}
	}
	/**
	* Getter for percentage chance to create particular power up.
	* @return chance for particular power up to be created, if arg index out of bound return 0 percent chance.
	*/
	public int getCreationPercentage() {
		return percentageOfTypeCreation.get(this.ordinal());
	}
	
	/**
	* Sets particular chance of power up to be created.
	* Initially collection is empty.
	* So it's necessary to add percentages in the same order as power ups present in Enum.
	* @param percentage chance for particular power up to be created. More percentage - more frequently power up will appear in the game.
	*/
	public void setCreationPercentage(int percentage) {
		if(percentage < 0 || percentage > 100) return;
		percentageOfTypeCreation.set(this.ordinal(), percentage);
	}
}
