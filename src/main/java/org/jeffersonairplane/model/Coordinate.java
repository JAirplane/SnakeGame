package org.jeffersonairplane.model;

/**
 * Game field consists of squares on a plane
 * @param xCoord X-axis coordinate of particular block on play field
 * @param yCoord Y-axis coordinate of particular block on play field
 */
public record Coordinate(int xCoord, int yCoord) {

    /**
     * Copy constructor
     * @param other Coordinate for copying
     */
    Coordinate(Coordinate other) {
        this(other.xCoord(), other.yCoord());
    }
    /**
     * Game field consists of squares on a plane
     * @param object Another object to compare with.
     * @return true if x and y coordinates are equal
     */
    @Override
    public boolean equals(Object object) {
        if(object instanceof Coordinate coordinate) {
            return this.xCoord() == coordinate.xCoord() && this.yCoord() == coordinate.yCoord();
        }
        return false;
    }
}
