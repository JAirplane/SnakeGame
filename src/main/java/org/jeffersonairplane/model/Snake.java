package org.jeffersonairplane.model;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

/**
 * Snake class implements snake's state, movement and grow.
 */
@Getter
public class Snake {

    /**
     * -- GETTER --
     */
    private final Deque<Coordinate> snakeBlocks = new LinkedList<>();
    /**
     * -- GETTER --
     * -- SETTER --
     */
    @Setter
    private Direction direction;
	
	/**
	 * No args constructor.
	 * Sets current snake direction to RIGHT by default.
	 */
    public Snake() {
		direction = Direction.RIGHT;
    }

    /**
	 * Compares two collections of snake blocks.
	 * @param object is another {@link org.jeffersonairplane.model.Snake} object.
	 * @return true if collections are equal.
	 */
    @Override
    public boolean equals(Object object) {
        if(object instanceof Snake snake) {
            return this.getSnakeBlocks().equals(snake.getSnakeBlocks()) && this.getDirection() == snake.getDirection();
        }
        return false;
    }
}
