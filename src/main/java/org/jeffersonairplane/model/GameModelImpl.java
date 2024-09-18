package org.jeffersonairplane.model;

import java.util.Arrays;
import java.util.logging.*;

public class GameModelImpl implements GameModel {
    private final int blocksWidth;
    private final int blocksHeight;
    private final Snake snake;
    private PowerUp powerUp;
    private final Logger logger;
    GameModelImpl(int pixelWidth, int pixelHeight, int blockSizeInPixels,
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

    @Override
    public boolean checkCollisions() {
        try {
            return borderCollide() && selfCollide();
        }
        catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage() + " " + Arrays.toString(e.getStackTrace()));
            throw new IllegalStateException("Snake head is null");
        }
    }

    @Override
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

    @Override
    public void powerUpEffect() {
        if(powerUp == null) return;
        if(snake == null) throw new NullPointerException("Snake is null");
        powerUp.influence(snake);
        powerUp = null;
    }

    @Override
    public void createPowerUp(PowerUpTypes type, Coordinate coordinate) {
        if(type == null) return;
        if(coordinate == null) throw new NullPointerException("PowerUp coordinate creation failed. Null Coordinate.");
        powerUp = switch (type) {
            case APPLE -> new Apple(coordinate);
        };
    }

}
