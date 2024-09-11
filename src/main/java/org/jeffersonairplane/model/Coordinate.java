package org.jeffersonairplane.model;

public record Coordinate(int xCoord, int yCoord) {
    Coordinate() {
        this(0, 0);
    }
}
