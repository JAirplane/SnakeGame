package org.jeffersonairplane.model;

public interface ModelExecution {
    public boolean checkCollisions();
    public boolean powerUpTaken();
    public boolean gameplayStep(Direction direction);
}
