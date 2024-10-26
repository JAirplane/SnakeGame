package org.jeffersonairplane.model;

import lombok.*;
import org.jeffersonairplane.*;
import org.jeffersonairplane.viewmodel.Direction;

import java.util.Arrays;
import java.util.*;
import java.util.logging.*;

/**
 * Model part of MVVM pattern.
 * Contains data and game logic.
 */
public class GameModelImpl implements GameModel {
	
	@Getter @Setter
	private FieldDimension dimension;
	@Getter
    private final SnakeManager snakeManager;
	@Getter
	private final PowerUpManager powerUpManager;
	@Getter @Setter
	private long framesCounter = 0;
	@Getter @Setter
	private long score = 0;

	private final List<PowerUpTakenObserver> powerUpTakenObservers = new ArrayList<>();
	
    private final Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * Constructor converts pixels to blocks.
	 * @param dimension contains field width and height in blocks (not pixels).
	 * @param snakeManager control snake {@link org.jeffersonairplane.model.SnakeManager}.
	 * @param powerUpManager manages power ups creation, lifecycle and death.
	 */
    public GameModelImpl(FieldDimension dimension, SnakeManager snakeManager, PowerUpManager powerUpManager) {
        this.dimension = dimension;
        this.snakeManager = snakeManager;
		this.powerUpManager = powerUpManager;
		logger.log(Level.FINE, "Model created.");
    }
	
	/**
	 * No args constructor receives all necessary data from {@link Properties}.
	 */
	public GameModelImpl() {
		try {
			Properties props = PropertiesLoader.getProperties();
			int xAxisBlocks = Integer.parseInt(props.getProperty("blocks_amount_x"));
			int yAxisBlocks = Integer.parseInt(props.getProperty("blocks_amount_y"));
			dimension = new FieldDimension(xAxisBlocks, yAxisBlocks);
			
			snakeManager = new SnakeManagerImpl();
			snakeManager.fillSnake(
				Integer.parseInt(props.getProperty("initial_snake_size")),
				new Coordinate(xAxisBlocks / 2, yAxisBlocks / 2),
                Direction.RIGHT,
				xAxisBlocks,
				yAxisBlocks);
			powerUpManager = new PowerUpManagerImpl(
				Integer.parseInt(props.getProperty("pu_number_limit")),
				Integer.parseInt(props.getProperty("pu_creation_delay_min")),
				Integer.parseInt(props.getProperty("pu_creation_delay_max")));
			logger.log(Level.FINE, "Model created.");
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage() + " " + Arrays.toString(e.getStackTrace()));
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Sets chance of creation for every power up type in the game.
	 */
	@Override
	public void setPowerUpTypesCreationChances() {
		try {
			Properties props = PropertiesLoader.getProperties();
			PowerUpTypes.APPLE.setCreationChance(
					Integer.parseInt(props.getProperty("apple_lower_limit")),
					Integer.parseInt(props.getProperty("apple_higher_limit")));
			PowerUpTypes.TAILCUTTER.setCreationChance(
					Integer.parseInt(props.getProperty("tail_cutter_lower_limit")),
					Integer.parseInt(props.getProperty("tail_cutter_higher_limit")));
			logger.log(Level.FINE, "Creation chances set.");
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage() + " " + Arrays.toString(e.getStackTrace()));
		}
	}
	
	/**
	 * Getter for snake movement frame rate.
	 * @return snake movement frame rate.
	 */
	@Override
	public int getSnakeMovementRhythm() {
		logger.log(Level.FINE, "Getter. Snake Rhythm: {0}", snakeManager.getSnakeMovementRhythm());
		return snakeManager.getSnakeMovementRhythm();
	}
	
	/**
	 * Setter for snake movement frame rate.
	 * @param rhythm snake movement frame rate.
	 */
	@Override
	public void setSnakeMovementRhythm(int rhythm) {
		logger.log(Level.FINE, "Setter. Arg: {0}", rhythm);
        snakeManager.setSnakeMovementRhythm(rhythm);
    }
	
	/**
	 * Getter for {@link org.jeffersonairplane.model.Snake} instance
	 * @return Snake instance
	 */
	@Override
	public Snake getSnake() {
		logger.log(Level.FINE, "Getter: Snake instance.");
		return snakeManager.getSnake();
	}

	/**
	 * Getter for {@link PowerUp} instance.
	 * @return List<PowerUp> collection of all existed power ups on the playing field.
	 */
	@Override
	public List<PowerUp> getPowerUps() {
		logger.log(Level.FINE, "Getter: Power Ups collection.");
		return powerUpManager.getPowerUps();
	}
	
	/**
	 * Check if snake collided with a play field borders or itself.
	 * Commonly it is a game over check.
	 * @return true if collided.
	 */
    @Override
    public boolean checkCollisions() {
        try {
			logger.log(Level.FINE, "Checking collisions.");
            return snakeManager.snakeCollideWithBorders(dimension.blocksAmountXAxis(), dimension.blocksAmountYAxis()) ||
					snakeManager.snakeSelfCollide();
        }
        catch(NullPointerException e) {
            logger.log(Level.SEVERE, e.getMessage() + " " + Arrays.toString(e.getStackTrace()));
            throw new NullPointerException("Snake head is null");
        }
    }

	/**
     * Changes current snake direction.
     * Commonly do nothing if parameter is the same as current snake direction or opposite to it.
     * @param newDirection sets the direction of snake movement.
	 * @return true if direction was changed.
     */
	@Override
	public boolean changeSnakeDirection(Direction newDirection) {
		logger.log(Level.FINE, "Changing snake direction.");
		return snakeManager.changeSnakeDirection(newDirection);
	}

	/**
	 * {@link org.jeffersonairplane.model.Snake} moves one step in its current direction
	 */
	@Override
	public void snakeMove() {
		logger.log(Level.FINE, "Snake is moving.");
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
		logger.log(Level.FINE, "Power up {0} applied on snake.", powerUp.getClass());
		return true;
    }
	
	/**
	 * Coordinate is free if there is no snake or power up exist on it.
	 * @return true if no game element on particular point at the moment.
	 */
	public boolean coordinateIsFree(Coordinate point) {
		logger.log(Level.FINE, "Coordinate is free -> power ups iteration.");
		for(PowerUp pu: powerUpManager.getPowerUps()) {
			if(pu.getPoint().equals(point)) return false;
		}
		logger.log(Level.FINE, "Coordinate is free -> Snake iteration.");
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
		logger.log(Level.FINE, "Get new free coordinate -> iteration started.");
		while(!pointIsFree) {
			point = new Coordinate(
					rnd.nextInt(dimension.blocksAmountXAxis()) + 1,
					rnd.nextInt(dimension.blocksAmountYAxis()) + 1);
			pointIsFree = coordinateIsFree(point);
		}
		logger.log(Level.FINE, "Get new free coordinate -> iteration finished. Point found: x: {0}, y: {1}", new Object[]{point.xCoord(), point.yCoord()});
		return point;
	}

	/**
	 * Runs one game frame.
	 * @return true if snake collided with border or itself.
	 */
	@Override
	public boolean oneFrameGameAction() {
		logger.log(Level.FINE, "New frame started in model.");
		if(framesCounter == Long.MAX_VALUE) {
			framesCounter = 0;
			logger.log(Level.FINE, "Frames counter nullified");
		}
		else ++framesCounter;

		powerUpManager.countdownWaitingPowerUps();
		powerUpManager.createPowerUps(this::getNewFreeCoordinate);
		powerUpManager.runNewPowerUpCountdown();
		logger.log(Level.FINE, "Power Ups has been run.");
		
		if(framesCounter % snakeManager.getSnakeMovementRhythm() == 0) {
			logger.log(Level.FINE, "Snake moving.");
			snakeMove();
			Coordinate headPoint = snakeManager.getSnake().getSnakeBlocks().getLast();
			var powerUp = powerUpManager.getPowerUpByPoint(headPoint);
			if(powerUp.isPresent()) {
				++score;
				notifyPowerUpTakenObservers(powerUp.get());
				powerUpEffect(powerUp.get());
				logger.log(Level.FINE, "Power up applied.");
			}
			return checkCollisions();
		}
		return false;
	}
	
	/**
     * Resets state to initial.
     */
	public void resetState() {
		framesCounter = 0;
		score = 0;
		snakeManager.resetState();
		powerUpManager.resetState();
		logger.log(Level.FINE, "Model state reset.");
	}
	
	/**
	 * Registers {@link PowerUpTakenObserver}.
	 *
	 * @param obs is an observer to register.
	 */
	@Override
	public void registerPowerUpTakenObserver(PowerUpTakenObserver obs) {
		powerUpTakenObservers.add(obs);
		logger.log(Level.FINE, "PowerUpTakenObserver registered.");
	}

	/**
	 * Removes {@link PowerUpTakenObserver}.
	 *
	 * @param obs is an observer to remove.
	 */
	@Override
	public void removePowerUpTakenObserver(PowerUpTakenObserver obs) {
		powerUpTakenObservers.remove(obs);
		logger.log(Level.FINE, "PowerUpTakenObserver removed.");
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
		logger.log(Level.FINE, "PowerUpTakenObservers notified.");
	}
}
