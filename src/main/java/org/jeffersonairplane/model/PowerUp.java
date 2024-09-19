package org.jeffersonairplane.model;

/**
* Parent class of all power ups.
* @see org.jeffersonairplane.model.Influencing
*/
public abstract class PowerUp implements Influencing {
	
    private final Coordinate point;
	
	/**
	* Constructor.
	* @param point is a coordinate of power up.
	* @see org.jeffersonairplane.model.Coordinate
	*/
    PowerUp(Coordinate point) {

		this.point = point;
    }
	/**
	* Getter for coordinate.
	* @return point is a coordinate of power up.
	* @see org.jeffersonairplane.model.Coordinate
	*/
    public Coordinate getPoint() {

		return point;
    }
}
