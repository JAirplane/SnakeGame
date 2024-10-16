package org.jeffersonairplane.model;

import java.util.*;

/**
 * Model part of MVVM pattern.
 * Contains data and game logic.
 */
public interface GameModel extends PowerUpTakenObservable {
	
	/**
	 * Sets chance of creation for every power up type in the game.
	 */
	void setPowerUpTypesCreationChances();
	
	/**
	 * Getter for snake instance from {@link org.jeffersonairplane.model.SnakeManager}
	 * @return Snake instance.
	 */
	Snake getSnake();
	
	/**
	 * Getter for snake movement frame rate.
	 * @return snake movement frame rate.
	 */
	int getSnakeMovementRhythm();
	
	/**
	 * Setter for snake movement frame rate.
	 * @param rhythm snake movement frame rate.
	 */
	void setSnakeMovementRhythm(int rhythm);
	
	/**
	 * Getter for {@link org.jeffersonairplane.model.FieldDimension} instance
	 * @return playingField instance
	 */
	FieldDimension getDimension();
	
	/**
	 * Getter for {@link org.jeffersonairplane.model.PowerUp} instance
	 * @return List<PowerUp> collection of all existed power ups on the playing field.
	 */
	List<PowerUp> getPowerUps();
	
	/**
	 * Check collisions of snake with itself and with field borders.
	 * @return true if collision actually happened.
	 */
    boolean checkCollisions();
	
	/**
     * Changes current snake direction.
     * Commonly do nothing if parameter is the same as current snake direction or opposite to it.
     * @param newDirection sets the direction of snake movement.
	 * @return true if direction was changed.
     */
	boolean changeSnakeDirection(Direction newDirection);
	
	/**
	 * {@link org.jeffersonairplane.model.Snake} moves one step in its current direction.
	 */
	void snakeMove();
	
	/**
	 * Apply power up effect.
	 * @param powerUp to apply.
	 * @return true if power up was applied.
	 */
    boolean powerUpEffect(PowerUp powerUp);

	/**
	 * Returns current players game score.
	 * @return score.
	 */
	long getScore();

	/**
	 * Sets players score value.
	 * @param score to set.
	 */
	void setScore(long score);

	/**
	 * Returns current number of gameplay frames passed.
	 * @return frames counter.
	 */
	long getFramesCounter();

	/**
	 * Sets frames counter.
	 * @param framesValue to set.
	 */
	void setFramesCounter(long framesValue);

	/**
	 * Returns snake manager.
	 * @return snake manager.
	 */
	SnakeManager getSnakeManager();

	/**
	 * Returns power up manager.
	 * @return power up manager.
	 */
	PowerUpManager getPowerUpManager();

	/**
	 * Runs one game frame.
	 * @return true if snake collided with border or itself.
	 */
	boolean oneFrameGameAction();
}
