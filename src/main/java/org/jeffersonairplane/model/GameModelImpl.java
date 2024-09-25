package org.jeffersonairplane.model;

import java.util.Arrays;
import java.util.logging.*;

/**
 * Model part of MVVM pattern.
 * Contains data and game logic.
 */
public class GameModelImpl implements GameModel {
	private final PlayingField playingField;
    private final SnakeManager snakeManager;
    private PowerUp powerUp;
    private final Logger logger;
	
	/**
	 * Constructor converts pixels to blocks
	 * @param playingField contains field width and height in blocks (not pixels)
	 * @param snakeManager control snake {@link org.jeffersonairplane.model.SnakeManager}
	 * @param logger logs information
	 */
    GameModelImpl(PlayingField playingField, SnakeManager snakeManager, Logger logger) {
        this.playingField = playingField;
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
            return snakeManager.snakeCollideWithBorders(playingField.getFieldWidth(), playingField.getFieldHeight()) && 
					snakeManager.snakeSelfCollide();
        }
        catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage() + " " + Arrays.toString(e.getStackTrace()));
            throw new IllegalStateException("Snake head is null");
        }
    }
	
	/**
	 * Getter for {@link org.jeffersonairplane.model.PlayingField} instance
	 * @return playingField instance
	 */
	@Override
	public PlayingField getPlayingField() {

		return playingField;
	}
	
	/**
	 * Getter for {@link org.jeffersonairplane.model.Snake} instance
	 * @return Snake instance
	 */
	@Override
	public Snake getSnake() {
		return snakeManager.getSnake();
	}
	
	/**
	 * Getter for {@link org.jeffersonairplane.model.PowerUp} instance
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

	/**
	 * Creates new {@link org.jeffersonairplane.model.PowerUp} instance in the game.
	 * @param type particular type of PowerUp to create.
	 * @param coordinate of created Power Up.
	 */
    @Override
    public void createPowerUp(PowerUpTypes type, Coordinate coordinate) {
        if(type == null) return;
        if(coordinate == null) throw new NullPointerException("PowerUp coordinate creation failed. Null Coordinate.");
        powerUp = switch (type) {
            case APPLE -> new Apple(coordinate);
        };
    }

}
