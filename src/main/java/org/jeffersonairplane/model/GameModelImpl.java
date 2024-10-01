package org.jeffersonairplane.model;

import java.util.Arrays;
import java.util.*;
import java.util.logging.*;

/**
 * Model part of MVVM pattern.
 * Contains data and game logic.
 */
public class GameModelImpl implements GameModel {
	/**
	 * Model part of MVVM pattern.
	 */
	private FieldDimension dimension;
    private final SnakeManager snakeManager;
    private List<PowerUp> powerUps;
    private final Logger logger;
	
	/**
	 * Constructor converts pixels to blocks.
	 * @param dimension contains field width and height in blocks (not pixels).
	 * @param snakeManager control snake {@link org.jeffersonairplane.model.SnakeManager}
	 * @param logger logs information.
	 */
    public GameModelImpl(FieldDimension dimension, SnakeManager snakeManager, Logger logger) {
        this.dimension = dimension;
        this.snakeManager = snakeManager;
        this.logger = logger;
		powerUps = new ArrayList<>();
    }
	
	/**
	 * Check if snake collided with a play field borders or itself.
	 * Commonly it is a game over check.
	 * @return true if collided.
	 */
    @Override
    public boolean checkCollisions() {
        try {
            return snakeManager.snakeCollideWithBorders(dimension.blocksAmountXAxis(), dimension.getBlocksAmountYAxis()) ||
					snakeManager.snakeSelfCollide();
        }
        catch(NullPointerException e) {
            logger.log(Level.SEVERE, e.getMessage() + " " + Arrays.toString(e.getStackTrace()));
            throw new NullPointerException("Snake head is null");
        }
    }
	
	/**
	 * Getter for {@link org.jeffersonairplane.model.FieldDimension} instance
	 * @return {@link org.jeffersonairplane.model.FieldDimension} instance
	 */
	@Override
	public FieldDimension getFieldDimension() {

		return dimension;
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
     * <p>Changes current snake direction.</p>
     * Commonly do nothing if parameter is the same as current snake direction or opposite to it.
     * @param newDirection sets the direction of snake movement.
	 * @return true if direction was changed.
     */
	@Override
	public boolean changeSnakeDirection(Direction newDirection) {
		return snakeManager.changeSnakeDirection(newDirection);
	}
	
	/**
	 * {@link org.jeffersonairplane.model.Snake} moves one step in its current direction
	 */
	@Override
	public void snakeMove() {
		snakeManager.snakeStep();
	}
	
	/**
	 * Getter for collection of {@link org.jeffersonairplane.model.PowerUp} instances.
	 * @return List<PowerUp> collection of all existed power ups on the playing field.
	 */
	@Override
	public List<PowerUp> getPowerUps() {
		return powerUps;
	}
	
	/**
	 * Checks if snake head collided with some power up
	 * Does not apply power up effect
	 * @return Optional<PowerUp> power up is present if exists in the power up collection.
	 */
    @Override
    public Optional<PowerUp> powerUpTaken() {
        try {
            if(powerUps == null) throw new NullPointerException("PowerUp collection is null");
			for(PowerUp power: powerUps) {
				if(snakeManager.snakeHeadAt(power.getPoint())) {
					return Optional.of(power);
				}
			}
			return Optional.empty();
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage() + " " + Arrays.toString(e.getStackTrace()));
            throw new IllegalStateException(e.getMessage());
        }
    }
	
	/**
	 * Applies effect of power up on snake.
	 * @param powerUp to apply.
	 * @return true if effect worked on snake.
	 */
    @Override
    public boolean powerUpEffect(PowerUp powerUp) {
		try {
			if(powerUp == null || !powerUps.contains(powerUp)) return false;
			snakeManager.changeSnakeState(powerUp::influence);
			powerUps.remove(powerUp);
			return true;
		}
        catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage() + " " + Arrays.toString(e.getStackTrace()));
            throw new IllegalStateException(e.getMessage());
        }
    }

	/**
	 * Creates new {@link org.jeffersonairplane.model.PowerUp} instance in the game.
	 * @param type particular type of PowerUp to create.
	 * @param coordinate of created Power Up.
	 * @return true if power up successfully created and added to power up collection.
	 */
    @Override
    public boolean createPowerUp(PowerUpTypes type, Coordinate coordinate) {
        if(type == null) return false;
        if(coordinate == null) throw new NullPointerException("PowerUp coordinate creation failed. Null Coordinate.");
        PowerUp powerUp = switch (type) {
            case APPLE -> new Apple(coordinate);
			default -> null;
        };
		if(powerUp == null) return false;
		powerUps.add(powerUp);
		return true;
    }

}
