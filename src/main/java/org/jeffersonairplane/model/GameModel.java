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
    boolean checkCollisions();
	
	/**
	 * Getter for snake instance from {@link org.jeffersonairplane.model.SnakeManager}
	 * @return Snake instance
	 */
	Snake getSnake();
	
	/**
	 * {@link org.jeffersonairplane.model.Snake} moves one step in its current direction
	 */
	void snakeMove();
	
	/**
     * <p>Changes current snake direction.</p>
     * Commonly do nothing if parameter is the same as current snake direction or opposite to it.
     * @param newDirection sets the direction of snake movement.
     */
	void changeSnakeDirection(Direction newDirection);
	
	/**
	 * Getter for {@link org.jeffersonairplane.model.PowerUp} instance
	 * @return PowerUp instance
	 */
	PowerUp getPowerUp();

	/**
	 * Getter for {@link org.jeffersonairplane.model.PlayingField} instance
	 * @return playingField instance
	 */
	PlayingField getPlayingField();

	/**
	 * Check if snake took some power up.
	 * @return true if power up taken.
	 */
    boolean powerUpTaken();
	
	/**
	 * Apply power up effect.
	 * @return true if power up applied.
	 */
    boolean powerUpEffect();
	
	/**
	 * Creates new power up on the play field.
	 * @param type is a type of power up to createPowerUp.
	 * @param coordinate is a coordinate of this power up.
	 */
    void createPowerUp(PowerUpTypes type, Coordinate coordinate);
}
