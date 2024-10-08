package org.jeffersonairplane.model;

import lombok.*;

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
	@Getter
	private FieldDimension dimension;
    private final SnakeManager snakeManager;
	@Getter
    private final List<PowerUp> powerUps;
	private final Map<PowerUpTypes, List<Integer>> powerUpCreationCountdowns;

	private long framesCounter = 0;
	@Getter @Setter
	private int snakeMovementRhythm;
	@Getter @Setter
	private int powerUpNumberLimit;
	@Getter @Setter
	private int minPowerUpCreationDelay;
	@Getter @Setter
	private int maxPowerUpCreationDelay;
	
    private final Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * Constructor converts pixels to blocks.
	 * @param dimension contains field width and height in blocks (not pixels).
	 * @param snakeManager control snake {@link org.jeffersonairplane.model.SnakeManager}.
	 * @param snakeMovementRhythm is a periodicity of snake movement in frames.
	 * @param powerUpNumberLimit is a number of power ups exists at the same time.
	 * @param minPowerUpCreationDelay is a min delay until power up created in milliseconds.
	 * @param maxPowerUpCreationDelay is a max delay until power up created in milliseconds.
	 */
    public GameModelImpl(FieldDimension dimension, SnakeManager snakeManager, int snakeMovementRhythm, int powerUpNumberLimit,
						int minPowerUpCreationDelay, int maxPowerUpCreationDelay) {
        this.dimension = dimension;
        this.snakeManager = snakeManager;
		this.snakeMovementRhythm = snakeMovementRhythm;
		this.powerUpNumberLimit = powerUpNumberLimit;
		this.minPowerUpCreationDelay = minPowerUpCreationDelay;
		this.maxPowerUpCreationDelay = maxPowerUpCreationDelay;
		powerUps = new ArrayList<>();
		powerUpCreationCountdowns = new HashMap<>();
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
	 * Checks if snake head collided with some power up
	 * Does not apply power up effect
	 * @return Optional<PowerUp> power up is present if exists in the power up collection.
	 */
    @Override
    public Optional<PowerUp> powerUpTaken() {
		for(PowerUp power: powerUps) {
			if(snakeManager.snakeHeadAt(power.getPoint())) {
				return Optional.of(power);
			}
		}
		return Optional.empty();
    }
	
	/**
	 * Applies effect of power up on snake.
	 * @param powerUp to apply.
	 * @return true if effect worked on snake.
	 */
    @Override
    public boolean powerUpEffect(PowerUp powerUp) {
		if(powerUp == null || !powerUps.contains(powerUp)) return false;
		snakeManager.changeSnakeState(powerUp::influence);
		powerUps.remove(powerUp);
		return true;
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
	
	/**
	 * One frame passed for all power ups waiting for creation.
	 */
	public void countdownWaitingPowerUps() {
		for(Map.Entry<PowerUpTypes, List<Integer>> entry: powerUpCreationCountdowns.entrySet()) {
			for(Integer framesLeft: entry.getValue()) {
				--framesLeft;
			}
		}
	}
	
	/**
	 * Creates every power up which waiting time has passed.
	 */
	public void createPowerUps() {
		for(Map.Entry<PowerUpTypes, List<Integer>> entry: powerUpCreationCountdowns.entrySet()) {
			for(Integer framesLeft: entry.getValue()) {
				if(framesLeft < 1) {
					createPowerUp(entry.getKey(), getNewFreeCoordinate());
				}
			}
		}
	}
	
	/**
	 * Adds new countdown in frames until power up created.
	 * Map key is a type of {@link org.jeffersonairplane.model.PowerUpTypes} - particular power up type.
	 * Value is a list of countdowns represented in frames.
	 * @param type particular type of PowerUp.
	 * @param framesDelay is a delay before creation.
	 */
	public void defineNewPowerUpCountdown(PowerUpTypes type, int framesDelay) {
		if(!powerUpCreationCountdowns.containsKey(type)) {
			powerUpCreationCountdowns.put(type, new ArrayList<Integer>());
		}
		powerUpCreationCountdowns.get(type).add(framesDelay);
	}
	
	/**
	 * Coordinate is free if there is no snake or power up exist on it.
	 * @return true if no game element on particular point at the moment.
	 */
	public boolean coordinateIsFree(Coordinate point) {
		for(PowerUp pu: powerUps) {
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
	
	/**
	 * Chooses type randomly using it predefined weight.
	 * @return type of power up.
	 */
	public PowerUpTypes getRandomPowerUpType() {
		Random rnd = new Random();
		int percentage = rnd.nextInt(101);
		for(PowerUpTypes type: PowerUpTypes.values()) {
			if(type.getCreationPercentage() <= percentage) {
				return type;
			}
		}
		logger.log(Level.SEVERE, "No appropriate Power Up type found.");
		throw new IllegalStateException("No appropriate Power Up type found.");
	}
	
	/**
	 * Number of power ups waiting for creation at the moment.
	 * @return number of power ups.
	 */
	public int getDuringCreationPowerUpsNumber() {
		int number = 0;
		for(var countdowns: powerUpCreationCountdowns.entrySet()) {
			number += countdowns.getValue().size();
		}
		return number;
	}

	@Override
	public boolean oneFrameGameAction() {
		++framesCounter;
		if(framesCounter == Long.MAX_VALUE) {
			framesCounter = 0;
		}
		
		countdownWaitingPowerUps();
		createPowerUps();
		
		if(powerUps.size() + getDuringCreationPowerUpsNumber() < powerUpNumberLimit) {
			Random rnd = new Random();
			int delay = rnd.nextInt(maxPowerUpCreationDelay - minPowerUpCreationDelay) + minPowerUpCreationDelay;
			defineNewPowerUpCountdown(getRandomPowerUpType(), delay);
		}
		
		if(framesCounter % snakeMovementRhythm == 0) {
			snakeMove();
			
			var powerUp = powerUpTaken();
            powerUp.ifPresent(this::powerUpEffect);
			return checkCollisions();
		}
		return false;
	}
}
