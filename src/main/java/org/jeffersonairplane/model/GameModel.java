package org.jeffersonairplane.model;

public interface GameModel {
    public boolean checkCollisions();
    public boolean powerUpTaken();
    public void powerUpEffect();
    public void createPowerUp(PowerUpTypes type, Coordinate coordinate);
}
