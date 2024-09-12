package org.jeffersonairplane.model;

public class GameExecution {
    private final int blocksWidth;
    private final int blocksHeight;
    private final Snake snake;
    GameExecution(int pixelWidth, int pixelHeight, int blockSizeInPixels,
                  Snake snake) {
        blocksWidth = pixelWidth / blockSizeInPixels;
        blocksHeight = pixelHeight / blockSizeInPixels;
        this.snake = snake;
    }
    private boolean borderCollide() throws Exception {
        Coordinate head = snake.getSnakeBlocks().peekLast();
        if(head == null) throw new NullPointerException();
        return head.xCoord() < 1 || head.xCoord() > blocksWidth
                || head.yCoord() < 1 || head.yCoord() > blocksHeight;
    }
    private boolean selfCollide() throws Exception{
        Coordinate head = snake.getSnakeBlocks().pollLast();
        if(head == null) throw new NullPointerException();
        for(Coordinate snakeBlock: snake.getSnakeBlocks()) {
            if(head.xCoord() == snakeBlock.xCoord() && head.yCoord() == snakeBlock.yCoord()) {
                return true;
            }
        }
        return false;
    }

    public void gameplayStep(Direction direction) {
        snake.step(direction);
    }
}
