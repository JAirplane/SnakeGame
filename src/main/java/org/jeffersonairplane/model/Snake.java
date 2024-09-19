package org.jeffersonairplane.model;

import java.util.*;

/**
 * Snake class implements snake's state, movement and grow.
 */
public class Snake {
	
    private final Deque<Coordinate> snakeBlocks = new LinkedList<>();
    private Direction direction;
	
	/**
	 * No args constructor.
	 * Sets current snake direction to RIGHT by default.
	 */
    public Snake() {
		direction = Direction.RIGHT;
    }
	
	/**
	 * Getter for current snake direction.
	 * @return current snake direction.
	 */
    public Direction getDirection() {

		return direction;
    }
	
	/**
	 * Setter for current snake direction.
	 * @param direction is a new snake direction.
	 */
	public void setDirection(Direction direction) {

		this.direction = direction;
    }
	
	/**
	 * Getter for collection of coordinates of blocks, snake consists of.
	 * @return snakeBlocks is a deque of coordinates of blocks, snake consists of.
	 */
    public Deque<Coordinate> getSnakeBlocks() {

		return snakeBlocks;
    }
	
	/**
	 * Compares two collections of snake blocks.
	 * @param object is another {@link org.jeffersonairplane.model.Snake} object.
	 * @return true if collections are equal.
	 */
    @Override
    public boolean equals(Object object) {
        if(object instanceof Snake snake) {
            return this.getSnakeBlocks().equals(snake.getSnakeBlocks());
        }
        return false;
    }
}
