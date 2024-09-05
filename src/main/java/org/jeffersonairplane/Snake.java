package org.jeffersonairplane;

import java.util.*;

class Snake {
    private final List<Coordinate> snakeBlocks = new ArrayList<>();

    Snake(int snakeSize, Coordinate centerBlock, int fieldWidthInBlocks) {
        int maxSnakeSize = fieldWidthInBlocks / 2;
        if(fieldWidthInBlocks % 2 != 0) {
            ++maxSnakeSize;
        }
        int xSnakeCoord = centerBlock.xCoord();
        for(int i = 0; i < Math.min(snakeSize, maxSnakeSize); i++) {
            snakeBlocks.add(new Coordinate(xSnakeCoord, centerBlock.yCoord()));
            --xSnakeCoord;
        }
    }
    public void grow() {
        Coordinate tail = snakeBlocks.getLast();
        if(tail != null) {
            snakeBlocks.add(new Coordinate(tail.xCoord(), tail.yCoord()));
        }
    }
    public void step(Direction direction) {
        for(int i = snakeBlocks.size() - 1; i > 0; i--) {
            snakeBlocks.set(i, snakeBlocks.get(i - 1));
        }
        int headXCoord = snakeBlocks.getFirst().xCoord();
        int headYCoord = snakeBlocks.getFirst().yCoord();
        Coordinate newHead = switch(direction){
            case LEFT -> newHead = new Coordinate(--headXCoord, headYCoord);
            case UP -> newHead = new Coordinate(headXCoord, ++headYCoord);
            case RIGHT -> newHead = new Coordinate(++headXCoord, headYCoord);
            case DOWN -> newHead = new Coordinate(headXCoord, --headYCoord);
        };

    }
}
