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
	 * Getter for snake instance from {@link org.jeffersonairplane.model.SnakeManager}
	 * @return Snake instance
	 */
	public Snake getSnake();
	
	/**
	 * Getter for {@link org.jeffersonairplane.model.PowerUp} instance
	 * @return PowerUp instance
	 */
	public PowerUp getPowerUp();

	/**
	 * Getter for {@link org.jeffersonairplane.model.PlayingField} instance
	 * @return playingField instance
	 */
	public PlayingField getPlayingField();

	/**
	 * Check if snake took some power up.
	 * @return true if power up taken.
	 */
    public boolean powerUpTaken();
	
	/**
	 * Apply power up effect.
	 * @return true if power up applied.
	 */
    public boolean powerUpEffect();
	
	/**
	 * Creates new power up on the play field.
	 * @param type is a type of power up to createPowerUp.
	 * @param coordinate is a coordinate of this power up.
	 */
    public void createPowerUp(PowerUpTypes type, Coordinate coordinate);
}
