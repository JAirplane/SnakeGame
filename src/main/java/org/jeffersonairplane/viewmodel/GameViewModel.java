package org.jeffersonairplane.viewmodel;

import org.jeffersonairplane.view.*;

/**
 * ViewModel of MVVM pattern.
 */
public interface GameViewModel extends InputObserver {

    /**
     * Converts snake blocks to points and sends List of {@link org.jeffersonairplane.view.RectangleUpperLeftPoint} to View.
     */
    void drawSnake();

    /**
     * Moves {@link org.jeffersonairplane.model.Snake} instance one step forward.
     */
    void snakeMove();

    /**
     * Checks if {@link org.jeffersonairplane.model.Snake} does not collide with borders or itself.
     */
    boolean checkSnakeCollisions();
	
	/**
     * Quite self explanatory :)
     */
	void runGame();
}
