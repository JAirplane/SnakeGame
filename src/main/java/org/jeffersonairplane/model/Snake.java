package org.jeffersonairplane.model;

import java.util.*;

/**
 * Snake class implements snake's state, movement and grow
 */
public class Snake {
    private final Deque<Coordinate> snakeBlocks = new LinkedList<>();
    private Direction currentDir;
    public Snake() {
        currentDir = Direction.RIGHT;
    }
    public Snake(int snakeSize, int fieldWidthInBlocks, int fieldHeightInBlocks, Direction direction) {
        currentDir = direction;

        int centerXBlock = fieldWidthInBlocks % 2 == 0 ? fieldWidthInBlocks / 2 : fieldWidthInBlocks / 2 + 1;
        int centerYBlock = fieldHeightInBlocks % 2 == 0 ? fieldHeightInBlocks / 2 : fieldHeightInBlocks / 2 + 1;
        int xSnakeCoord = centerXBlock;

        for(int i = 0; i < Math.min(snakeSize, centerXBlock); i++) {
            snakeBlocks.addFirst(new Coordinate(xSnakeCoord, centerYBlock));
            --xSnakeCoord;
        }
    }
    private Direction changeDirection(Direction newDirection) {
        if(newDirection != null && ((newDirection.equals(Direction.LEFT) && !currentDir.equals(Direction.RIGHT)) ||
                (newDirection.equals(Direction.RIGHT) && !currentDir.equals(Direction.LEFT)) ||
                (newDirection.equals(Direction.UP) && !currentDir.equals(Direction.DOWN)) ||
                (newDirection.equals(Direction.DOWN) && !currentDir.equals(Direction.UP)))) {

            currentDir = newDirection;
        }
        return currentDir;
    }
    public Direction getCurrentDir() {
        return currentDir;
    }
    public Deque<Coordinate> getSnakeBlocks() {
        return snakeBlocks;
    }
    public void grow() {
        Coordinate tail = snakeBlocks.peekFirst();
        if(tail != null) {
            snakeBlocks.offerFirst(new Coordinate(tail.xCoord(), tail.yCoord()));
        }
    }

    public void step(Direction newDirection) {
        snakeBlocks.pollFirst();

        Direction dir = changeDirection(newDirection);
        int headXCoord = snakeBlocks.getLast().xCoord();
        int headYCoord = snakeBlocks.getLast().yCoord();
        Coordinate newHead = switch(dir) {
            case LEFT -> newHead = new Coordinate(--headXCoord, headYCoord);
            case UP -> newHead = new Coordinate(headXCoord, ++headYCoord);
            case RIGHT -> newHead = new Coordinate(++headXCoord, headYCoord);
            case DOWN -> newHead = new Coordinate(headXCoord, --headYCoord);
        };
        snakeBlocks.offerLast(newHead);
    }
    @Override
    public boolean equals(Object object) {
        if(object instanceof Snake snake) {
            return this.getSnakeBlocks().equals(snake.getSnakeBlocks());
        }
        return false;
    }
}
