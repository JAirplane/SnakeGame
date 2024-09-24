package org.jeffersonairplane.model;

import java.util.Arrays;
import java.util.logging.*;

/**
 * Model part of MVVM pattern.
 * Contains data and game logic.
 */
public class GameModelImpl implements GameModel {
    private final int blocksWidth;
    private final int blocksHeight;
    private final SnakeManager snakeManager;
    private PowerUp powerUp;
    private final Logger logger;
	
	/**
	 * Constructor converts pixels to blocks
	 * @param pixelWidth is a width of play field in pixels
	 * @param pixelHeight is a height of play field in pixels
	 * @param blockWidthInPixels is a one block width in pixels
	 * @param blockHeightInPixels is a one block height in pixels
	 * @param snakeManager control snake {@link org.jeffersonairplane.model.SnakeManager}
	 * @param logger logs information
	 */
    GameModelImpl(int pixelWidth, int pixelHeight, int blockWidthInPixels, int blockHeightInPixels,
                  SnakeManager snakeManager, Logger logger) {
        blocksWidth = pixelWidth / blockWidthInPixels;
        blocksHeight = pixelHeight / blockHeightInPixels;
        this.snakeManager = snakeManager;
        this.logger = logger;
    }
	
	/**
	 * Check if snake collided with a play field borders or itself
	 * Commonly it is a game over check
	 * @return true if collided
	 */
    @Override
    public boolean checkCollisions() {
        try {
            return snakeManager.snakeCollideWithBorders(blocksWidth, blocksHeight) && snakeManager.snakeSelfCollide();
        }
        catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage() + " " + Arrays.toString(e.getStackTrace()));
            throw new IllegalStateException("Snake head is null");
        }
    }
	
	/**
	 * Getter for snake instance
	 * @return Snake instance
	 */
	@Override
	public Snake getSnake() {
		return snakeManager.getSnake();
	}
	
	/**
	 * Getter for powerUp instance
	 * @return PowerUp instance
	 */
	@Override
	public PowerUp getPowerUp() {
		return powerUp;
	}
	
	/**
	 * Check if snake collided with power up
	 * Does not apply power up effect
	 * @return true if collided
	 */
    @Override
    public boolean powerUpTaken() {
        try {
            if(powerUp == null) return false;
            return snakeManager.snakeHeadAt(powerUp.getPoint());
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage() + " " + Arrays.toString(e.getStackTrace()));
            throw new IllegalStateException("Snake head is null");
        }
    }
	
	/**
	 * Applies effect of power up on snake
	 * @return true if effect worked on snake
	 */
    @Override
    public boolean powerUpEffect() {
        if(powerUp == null) return false;
        snakeManager.changeSnakeState(powerUp::influence);
        powerUp = null;
		return true;
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
