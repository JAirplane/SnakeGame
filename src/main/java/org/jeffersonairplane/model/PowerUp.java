package org.jeffersonairplane.model;

public abstract class PowerUp implements Influencing {
    private final Coordinate point;
    PowerUp(Coordinate point) {
        this.point = point;
    }
    public Coordinate getPoint() {
        return point;
    }
}
