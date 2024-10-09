package org.jeffersonairplane.model;

import java.util.*;

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
	 * @return Snake instance.
	 */
	Snake getSnake();

	/**
	 * Getter for {@link org.jeffersonairplane.model.FieldDimension} instance
	 * @return playingField instance
	 */
	public FieldDimension getFieldDimension();
	
	/**
	 * Getter for {@link org.jeffersonairplane.model.PowerUp} instance
	 * @return List<PowerUp> collection of all existed power ups on the playing field.
	 */
	List<PowerUp> getPowerUps();
	
	/**
	 * {@link org.jeffersonairplane.model.Snake} moves one step in its current direction.
	 */
	void snakeMove();
	
	/**
     * <p>Changes current snake direction.</p>
     * Commonly do nothing if parameter is the same as current snake direction or opposite to it.
     * @param newDirection sets the direction of snake movement.
	 * @return true if direction was changed.
     */
	boolean changeSnakeDirection(Direction newDirection);
	
	/**
	 * Apply power up effect.
	 * @param powerUp to apply.
	 * @return true if power up was applied.
	 */
    boolean powerUpEffect(PowerUp powerUp);

	boolean oneFrameGameAction();
}
