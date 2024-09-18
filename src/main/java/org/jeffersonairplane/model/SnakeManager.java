package org.jeffersonairplane.model;

import java.util.function.Consumer;

/**
 * Controls and manage snake
 */
public interface SnakeManager {
    /**
     * <p>Fills snake blocks, commonly on initialization</p>
     * @param snakeSize amount of snake blocks to fill
     * @param head initial head coordinate on a field
     * @param direction initial snake direction, tail should be created opposite to it
     * @param fieldWidthInBlocks need to check that snake blocks inside the field
     * @param fieldHeightInBlocks need to check that snake blocks inside the field
     */
    void fillSnake(int snakeSize, Coordinate head, Direction direction, int fieldWidthInBlocks, int fieldHeightInBlocks);
    /**
     * <p>Moves snake one step forward in a current snake direction</p>
     */
    void snakeStep();
    /**
     * <p>Changes current snake direction.</p>
     * Commonly do nothing if parameter is the same as current snake direction or opposite to it.
     * @param newDirection sets the direction of snake movement
     */
    Direction changeSnakeDirection(Direction newDirection);
    /**
     * <p>Applies any possible effects on snake (grow, speed up, etc...)</p>
     * @param powerUpEffect lambda that takes snake as arg and applies effect on it
     */
    void changeSnakeState(Consumer<Snake> powerUpEffect);
}
