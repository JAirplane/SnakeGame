package org.jeffersonairplane.model;

import java.util.Arrays;
import java.util.logging.*;

public class GameExecution implements ModelExecution {
    private final int blocksWidth;
    private final int blocksHeight;
    private final Snake snake;
    private PowerUp powerUp;
    private final Logger logger;
    GameExecution(int pixelWidth, int pixelHeight, int blockSizeInPixels,
                  Snake snake, Logger logger) {
        blocksWidth = pixelWidth / blockSizeInPixels;
        blocksHeight = pixelHeight / blockSizeInPixels;
        this.snake = snake;
        this.logger = logger;
    }
    private boolean borderCollide() {
        Coordinate head = snake.getSnakeBlocks().peekLast();
        if(head == null) throw new NullPointerException();
        return head.xCoord() < 1 || head.xCoord() > blocksWidth
                || head.yCoord() < 1 || head.yCoord() > blocksHeight;
    }
    private boolean selfCollide() {
        Coordinate head = snake.getSnakeBlocks().peekLast();
        if(head == null) throw new NullPointerException();
        for(Coordinate snakeBlock: snake.getSnakeBlocks()) {
            if(head.xCoord() == snakeBlock.xCoord() && head.yCoord() == snakeBlock.yCoord() && snakeBlock != head) {
                return true;
            }
        }
        return false;
    }
    public boolean checkCollisions() {
        try {
            return borderCollide() && selfCollide();
        }
        catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage() + " " + Arrays.toString(e.getStackTrace()));
            throw new IllegalStateException("Snake head is null");
        }
    }
    public boolean powerUpTaken() {
        try {
            if(powerUp == null) return false;
            Coordinate head = snake.getSnakeBlocks().peekLast();
            if(head == null) throw new NullPointerException("Snake head is null");
            return head.xCoord() == powerUp.getPoint().xCoord() &&
                    head.yCoord() == powerUp.getPoint().yCoord();
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage() + " " + Arrays.toString(e.getStackTrace()));
            throw new IllegalStateException("Snake head is null");
        }
    }
    public boolean gameplayStep(Direction direction) {
        snake.step(direction);
        if(checkCollisions()) {
            return false;
        }
        else if(powerUpTaken()) {
            powerUp.influence(snake);
        }
        return true;
    }
}
