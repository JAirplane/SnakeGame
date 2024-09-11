package org.jeffersonairplane.model;

import java.util.*;

public class Snake {
    private final Deque<Coordinate> snakeBlocks = new LinkedList<>();
    private Direction currentDir;
    public Snake(int snakeSize, Coordinate centerBlock, int fieldWidthInBlocks) {
        currentDir = Direction.RIGHT;

        int maxSnakeSize = fieldWidthInBlocks / 2;
        if(fieldWidthInBlocks % 2 != 0) {
            ++maxSnakeSize;
        }
        int xSnakeCoord = centerBlock.xCoord();
        for(int i = 0; i < Math.min(snakeSize, maxSnakeSize); i++) {
            snakeBlocks.addFirst(new Coordinate(xSnakeCoord, centerBlock.yCoord()));
            --xSnakeCoord;
        }
    }
    private Direction changeDirection(Direction newDirection) {
        if((newDirection.equals(Direction.LEFT) && !currentDir.equals(Direction.RIGHT)) ||
                (newDirection.equals(Direction.RIGHT) && !currentDir.equals(Direction.LEFT)) ||
                (newDirection.equals(Direction.UP) && !currentDir.equals(Direction.DOWN)) ||
                (newDirection.equals(Direction.DOWN) && !currentDir.equals(Direction.UP))) {

            currentDir = newDirection;
        }
        return currentDir;
    }
    public void grow() {
        Coordinate tail = snakeBlocks.getFirst();
        if(tail != null) {
            snakeBlocks.addFirst(new Coordinate(tail.xCoord(), tail.yCoord()));
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
}
