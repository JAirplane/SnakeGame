package org.jeffersonairplane.model;

import lombok.Getter;

/**
* Parent class of all power ups.
* @see org.jeffersonairplane.model.Influencing
*/
@Getter
public abstract class PowerUp implements Influencing {

    /**
     * -- GETTER --
     */
    private final Coordinate point;
	
	/**
	* Constructor.
	* @param point is a coordinate of power up.
	* @see org.jeffersonairplane.model.Coordinate
	*/
    PowerUp(Coordinate point) {
		this.point = point;
    }
}
