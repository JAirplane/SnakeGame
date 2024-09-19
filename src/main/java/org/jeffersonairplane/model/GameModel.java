package org.jeffersonairplane.model;

/**
 * Model part of MVVM pattern.
 * Contains data and game logic.
 */
public interface GameModel {
	/**
	 * Check collisions of snake with itself and with field borders.
	 * @return true if collision actually happened.
	 */
    public boolean checkCollisions();
	
	/**
	 * Check if snake took some power up.
	 * @return true if power up taken.
	 */
    public boolean powerUpTaken();
	
	/**
	 * Apply power up effect.
	 */
    public void powerUpEffect();
	
	/**
	 * Creates new power up on the play field.
	 * @param type is a type of power up to createPowerUp.
	 * @param coordinate is a coordinate of this power up.
	 */
    public void createPowerUp(PowerUpTypes type, Coordinate coordinate);
}
