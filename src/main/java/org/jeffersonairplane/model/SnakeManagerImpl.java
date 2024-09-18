package org.jeffersonairplane.model;

import java.util.function.Consumer;

public class SnakeManagerImpl implements SnakeManager {
    private Snake snake;
    SnakeManagerImpl() {
        snake = new Snake();
    }
    SnakeManagerImpl(Snake snake) {
        this.snake = snake;
    }

    /**
     * <p>Fills snake blocks, commonly on initialization</p>
     *
     * @param snakeSize           amount of snake blocks to fill
     * @param head                initial head coordinate on a field
     * @param direction           initial snake direction, tail should be created opposite to it
     * @param fieldWidthInBlocks  need to check that snake blocks inside the field
     * @param fieldHeightInBlocks need to check that snake blocks inside the field
     */
    @Override
    public void fillSnake(int snakeSize, Coordinate head, Direction direction, int fieldWidthInBlocks, int fieldHeightInBlocks) {
        int centerXBlock = fieldWidthInBlocks % 2 == 0 ? fieldWidthInBlocks / 2 : fieldWidthInBlocks / 2 + 1;
        int centerYBlock = fieldHeightInBlocks % 2 == 0 ? fieldHeightInBlocks / 2 : fieldHeightInBlocks / 2 + 1;
        int xSnakeCoord = centerXBlock;

        for(int i = 0; i < Math.min(snakeSize, centerXBlock); i++) {
            snake.getSnakeBlocks().addFirst(new Coordinate(xSnakeCoord, centerYBlock));
            --xSnakeCoord;
        }
    }

    /**
     * <p>Moves snake one step forward in a current snake direction</p>
     */
    @Override
    public void snakeStep() {

    }

    /**
     * <p>Changes current snake direction.</p>
     * Commonly do nothing if parameter is the same as current snake direction or opposite to it.
     *
     * @param newDirection sets the direction of snake movement
     */
    @Override
    public Direction changeSnakeDirection(Direction newDirection) {
        return null;
    }

    /**
     * <p>Applies any possible effects on snake (grow, speed up, etc...)</p>
     *
     * @param powerUpEffect lambda that takes snake as arg and applies effect on it
     */
    @Override
    public void changeSnakeState(Consumer<Snake> powerUpEffect) {

    }
}
