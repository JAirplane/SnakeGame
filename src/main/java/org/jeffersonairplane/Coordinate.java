package org.jeffersonairplane;

record Coordinate(int xCoord, int yCoord) {
    Coordinate() {
        this(0, 0);
    }
}
