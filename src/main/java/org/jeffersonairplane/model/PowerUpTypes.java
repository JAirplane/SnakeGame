package org.jeffersonairplane.model;

import lombok.Getter;

/**
* All power up types in the game.
* Main purpose is to create power ups dynamically.
*/
@Getter
public enum PowerUpTypes {
    APPLE(0);

    private int creationChance;
	
	private PowerUpTypes(int creationChance) {
		this.creationChance = creationChance;
	}

    /**
	* Sets particular chance of power up to be created.
	* All chances are zero by default, so should be set up manually using this method.
	* @param chance for particular power up to be created. More percentage - more frequently power up will appear in the game.
	*/
	public void setCreationChance(int chance) {
		if(chance < 0 || chance > 100) return;
		creationChance = chance;
	}
}
