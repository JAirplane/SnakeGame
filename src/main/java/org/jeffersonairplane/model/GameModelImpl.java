package org.jeffersonairplane.model;

import lombok.*;
import org.jeffersonairplane.view.InputObserver;

import java.util.Arrays;
import java.util.*;
import java.util.logging.*;

/**
 * Model part of MVVM pattern.
 * Contains data and game logic.
 */
public class GameModelImpl implements GameModel {
	
	@Getter
	private final FieldDimension dimension;
    private final SnakeManager snakeManager;
	private final PowerUpManager powerUpManager;
	@Getter @Setter
	private long framesCounter = 0;
	@Getter @Setter
	private long score = 0;
	@Getter @Setter
	private int snakeMovementRhythm;

	private final List<PowerUpTakenObserver> powerUpTakenObservers = new ArrayList<>();
	
    private final Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * Constructor converts pixels to blocks.
	 * @param dimension contains field width and height in blocks (not pixels).
	 * @param snakeManager control snake {@link org.jeffersonairplane.model.SnakeManager}.
	 * @param snakeMovementRhythm is a periodicity of snake movement in frames.
	 * @param powerUpManager manages power ups creation, lifecycle and death.
	 */
    public GameModelImpl(FieldDimension dimension, SnakeManager snakeManager, int snakeMovementRhythm,
						 PowerUpManager powerUpManager) {
        this.dimension = dimension;
        this.snakeManager = snakeManager;
		this.snakeMovementRhythm = snakeMovementRhythm;
		this.powerUpManager = powerUpManager;
    }
	
	/**
	 * Check if snake collided with a play field borders or itself.
	 * Commonly it is a game over check.
	 * @return true if collided.
	 */
    @Override
    public boolean checkCollisions() {
        try {
            return snakeManager.snakeCollideWithBorders(dimension.blocksAmountXAxis(), dimension.blocksAmountYAxis()) ||
					snakeManager.snakeSelfCollide();
        }
        catch(NullPointerException e) {
            logger.log(Level.SEVERE, e.getMessage() + " " + Arrays.toString(e.getStackTrace()));
            throw new NullPointerException("Snake head is null");
        }
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
	 * Getter for {@link FieldDimension} instance
	 * @return playingField dimension instance
	 */
	@Override
	public FieldDimension getFieldDimension() {
		return dimension;
	}

	/**
	 * Getter for {@link PowerUp} instance.
	 * @return List<PowerUp> collection of all existed power ups on the playing field.
	 */
	@Override
	public List<PowerUp> getPowerUps() {
		return powerUpManager.getPowerUps();
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
	 * Applies effect of power up on snake.
	 * @param powerUp to apply.
	 * @return true if effect worked on snake.
	 */
    @Override
    public boolean powerUpEffect(PowerUp powerUp) {
		if(powerUp == null) return false;
		snakeManager.changeSnakeState(powerUp::influence);
		powerUpManager.removePowerUp(powerUp);
		return true;
    }
	
	/**
	 * Coordinate is free if there is no snake or power up exist on it.
	 * @return true if no game element on particular point at the moment.
	 */
	public boolean coordinateIsFree(Coordinate point) {
		for(PowerUp pu: powerUpManager.getPowerUps()) {
			if(pu.getPoint().equals(point)) return false;
		}
		for(Coordinate snakeBlock: snakeManager.getSnake().getSnakeBlocks()) {
			if(snakeBlock.equals(point)) return false;
		}
		return true;
	}
	
	/**
	 * Coordinate is valid if there is no snake or power up exist on it.
	 * @return coordinate of random free block on the playing field.
	 */
	public Coordinate getNewFreeCoordinate() {
		Random rnd = new Random();
		boolean pointIsFree = false;
		Coordinate point = null;
		while(!pointIsFree) {
			point = new Coordinate(
					rnd.nextInt(dimension.blocksAmountXAxis()) + 1,
					rnd.nextInt(dimension.blocksAmountYAxis()) + 1);
			pointIsFree = coordinateIsFree(point);
		}
		return point;
	}

	@Override
	public boolean oneFrameGameAction() {
		if(framesCounter == Long.MAX_VALUE) {
			framesCounter = 0;
		}
		else ++framesCounter;

		powerUpManager.countdownWaitingPowerUps();
		powerUpManager.createPowerUps(this::getNewFreeCoordinate);
		powerUpManager.runNewPowerUpCountdown();
		
		if(framesCounter % snakeMovementRhythm == 0) {
			snakeMove();
			Coordinate headPoint = snakeManager.getSnake().getSnakeBlocks().getLast();
			var powerUp = powerUpManager.getPowerUpByPoint(headPoint);
			if(powerUp.isPresent()) {
				++score;
				notifyPowerUpTakenObservers(powerUp.get());
				powerUpEffect(powerUp.get());
			}
			return checkCollisions();
		}
		return false;
	}

	/**
	 * Registers {@link PowerUpTakenObserver}.
	 *
	 * @param obs is an observer to register.
	 */
	@Override
	public void registerPowerUpTakenObserver(PowerUpTakenObserver obs) {
		powerUpTakenObservers.add(obs);
	}

	/**
	 * Removes {@link PowerUpTakenObserver}.
	 *
	 * @param obs is an observer to remove.
	 */
	@Override
	public void removePowerUpTakenObserver(PowerUpTakenObserver obs) {
		powerUpTakenObservers.remove(obs);
	}

	/**
	 * Notifies {@link PowerUpTakenObserver} if power up eaten by snake.
	 *
	 * @param powerUp is a power up eaten.
	 */
	@Override
	public void notifyPowerUpTakenObservers(PowerUp powerUp) {
		for(var obs: powerUpTakenObservers) {
			obs.powerUpTakenUpdate(powerUp);
		}
	}
}
