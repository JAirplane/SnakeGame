package org.jeffersonairplane.model;

/**
 * Game field consists of squares on a plane
 * @param xCoord of particular square
 * @param yCoord of particular square
 */
public record Coordinate(int xCoord, int yCoord) {
    Coordinate() {
        this(0, 0);
    }
}
