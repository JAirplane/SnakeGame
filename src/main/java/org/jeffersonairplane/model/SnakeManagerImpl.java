package org.jeffersonairplane.model;

import lombok.*;
import org.jeffersonairplane.PropertiesLoader;
import org.jeffersonairplane.viewmodel.Direction;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.*;

/**
 * Controls and manages snake instance.
 */
@Getter
public class SnakeManagerImpl implements SnakeManager {
	
	private final Snake snake;
	@Setter
	private int snakeMovementRhythm;

	private final Logger logger = Logger.getLogger(getClass().getName());
	/**
	 * No args constructor.
	 * Creates empty {@link org.jeffersonairplane.model.Snake} with 0 blocks.
	 * Receives snake movement rhythm from properties.
	 */
    public SnakeManagerImpl() {
		try {
			snake = new Snake();

			Properties props = PropertiesLoader.getProperties();
			snakeMovementRhythm = Integer.parseInt(props.getProperty("snake_move_delay"));
			logger.log(Level.FINE, "Snake manager created successfully.");
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage() + " " + Arrays.toString(e.getStackTrace()));
			throw new RuntimeException(e);
		}
    }
	
	/**
	 * Constructor
	 * @param snake is an option to create snake externally.
	 * @param movementRhythm snake movement frame rate.
	 */
    public SnakeManagerImpl(Snake snake, int movementRhythm) {
		
		Snake snakeToSet = snake;
		if(snakeToSet == null) {
			snakeToSet = new Snake();
		}
		this.snake = snakeToSet;

		snakeMovementRhythm = movementRhythm;
		logger.log(Level.FINE, "Snake manager created successfully.");
    }
	
	private Coordinate getNextCoordinateToFillSnake(Coordinate current, Direction direction) {
		if(current == null || direction == null) {
			logger.log(Level.SEVERE, "Snake filling failed. Current coordinate or direction is null.");
			throw new NullPointerException("Coordinate or direction is null");
		} 
		int x = 0, y = 0;
		if(direction == Direction.UP || direction == Direction.DOWN) {
			x = current.xCoord();
			if(direction == Direction.UP) {
				y = current.yCoord() - 1;
			}
			else {
				y = current.yCoord() + 1;
			}
		}
		else {
			y = current.yCoord();
			if(direction == Direction.LEFT) {
				x = current.xCoord() + 1;
			}
			else {
                x = current.xCoord() - 1;
            }
		}
		logger.log(Level.FINE, "Next snake coordinate: x:{0}, y:{1}.", new Object[]{x, y});
		return new Coordinate(x, y);
	}
	private int getActualSnakeSizeToFill(int snakeSize, Coordinate head, Direction direction, int fieldWidthInBlocks, int fieldHeightInBlocks) {
		if(head == null || direction == null) {
			logger.log(Level.SEVERE, "Snake actual size undefined. Snake head or direction is null.");
			throw new NullPointerException("Snake head or direction is null.");
		} 
        return switch(direction) {
			case LEFT -> Math.min(snakeSize, fieldWidthInBlocks - head.xCoord() + 1);
			case RIGHT -> Math.min(snakeSize, head.xCoord());
			case UP -> Math.min(snakeSize, fieldHeightInBlocks - head.yCoord() + 1);
			case DOWN -> Math.min(snakeSize, head.yCoord());
		};
	}
    /**
     * <p>Fills {@link org.jeffersonairplane.model.Snake} blocks, commonly on initialization.</p>
     * @param snakeSize           amount of snake blocks to fill.
     * @param head                initial head coordinate on a play field.
     * @param direction           initial snake direction, tail should be created opposite to it.
     * @param fieldWidthInBlocks  need to check that snake blocks inside the field.
     * @param fieldHeightInBlocks need to check that snake blocks inside the field.
	 * @return true if snake filled and ready.
     */
    @Override
    public boolean fillSnake(int snakeSize, Coordinate head, Direction direction, int fieldWidthInBlocks, int fieldHeightInBlocks) {
		if(head == null || direction == null || snakeSize < 1 || fieldWidthInBlocks < 1 || fieldHeightInBlocks < 1 ||
			head.xCoord() < 1 || head.xCoord() > fieldWidthInBlocks || head.yCoord() < 1 || head.yCoord() > fieldHeightInBlocks) {
			return false;
		}
		int actualSnakeSize = getActualSnakeSizeToFill(snakeSize, head, direction, fieldWidthInBlocks, fieldHeightInBlocks);
		snake.setDirection(direction);
		snake.getSnakeBlocks().clear();
		Coordinate current = new Coordinate(head.xCoord(), head.yCoord());
		snake.getSnakeBlocks().offerFirst(current);
		Coordinate next = null;
		
		logger.log(Level.FINE, "Processing fill snake in a loop. Blocks amount: {0}", actualSnakeSize);
        for(int i = 0; i < actualSnakeSize - 1; i++) {
			logger.log(Level.FINER, "Processing {0}", i+1);
			next = getNextCoordinateToFillSnake(current, direction);
            snake.getSnakeBlocks().offerFirst(next);
            current = next;
        }
		return true;
    }

    /**
     * <p>Moves snake one step forward in a current snake direction.</p>
     */
    @Override
    public void snakeStep() {
		Coordinate head = snake.getSnakeBlocks().peekLast();
		snake.getSnakeBlocks().pollFirst();
		if(head == null) {
			logger.log(Level.SEVERE, "Snake step failed. Snake head is null.");
			throw new NullPointerException("Snake head is null");
		}
        int headXCoord = head.xCoord();
        int headYCoord = head.yCoord();
        Coordinate newHead = switch(snake.getDirection()) {
            case LEFT -> new Coordinate(--headXCoord, headYCoord);
            case UP -> new Coordinate(headXCoord, ++headYCoord);
            case RIGHT -> new Coordinate(++headXCoord, headYCoord);
            case DOWN -> new Coordinate(headXCoord, --headYCoord);
        };
		logger.log(Level.FINE, "Processing snake step. New head point: x:{0}, y:{1}", new Object[]{newHead.xCoord(), newHead.yCoord()});
		snake.getSnakeBlocks().offerLast(newHead);
    }

    /**
     * <p>Changes current snake direction.</p>
     * Commonly do nothing if parameter is the same as current snake direction or opposite to it.
     * @param newDirection sets the direction of snake movement.
	 * @return true if direction has changed.
     */
    @Override
    public boolean changeSnakeDirection(Direction newDirection) {
		Direction currentDir = snake.getDirection();
		if(currentDir == newDirection) return false;
        if(newDirection != null && newDirection != snake.getForbidenDirection()) {

            snake.setDirection(newDirection);
			logger.log(Level.FINE, "Snake direction changed to {0}", newDirection);
			return true;
        }
        return false;
    }

	/**
	 * Sets forbidden direction according to current snake direction.
	 * It prevents bad snake direction inside one game frame when user make a few inputs between snake movements.
	 */
	@Override
	public void setForbiddenSnakeDirection() {
		switch(snake.getDirection()) {
			case LEFT -> snake.setForbidenDirection(Direction.RIGHT);
			case UP -> snake.setForbidenDirection(Direction.DOWN);
			case RIGHT -> snake.setForbidenDirection(Direction.LEFT);
			case DOWN -> snake.setForbidenDirection(Direction.UP);
		}
	}

    /**
     * <p>Applies any possible effects on snake (grow, speed up, etc...)</p>
     * @param powerUpEffect lambda that takes snake as arg and applies effect on it.
     */
    @Override
    public void changeSnakeState(Consumer<Snake> powerUpEffect) {
		if(powerUpEffect == null) return;
		powerUpEffect.accept(snake);
		logger.log(Level.FINE, "Power up applied");
    }
	
	/**
     * <p>Checks if snake collided with play field borders.</p>
     * @param fieldWidth is amount of blocks on X axis.
	 * @param fieldHeight is amount of blocks on Y axis.
	 * @return true if snake collided with border.
     */
	@Override
	public boolean snakeCollideWithBorders(int fieldWidth, int fieldHeight) {
		logger.log(Level.FINE, "Snake collision with borders check.");
		Coordinate head = snake.getSnakeBlocks().peekLast();
        if(head == null) {
			logger.log(Level.SEVERE, "Snake border collision failed. Snake head is null.");
			throw new NullPointerException("Snake border collision failed. Snake head is null.");
		}
        return head.xCoord() < 1 || head.xCoord() > fieldWidth
                || head.yCoord() < 1 || head.yCoord() > fieldHeight;
	}
	
	/**
     * <p>Checks if snake collided with itself.</p>
	 * @return true if snake collided with itself.
     */
	@Override
	public boolean snakeSelfCollide() {
		logger.log(Level.FINE, "Snake collision with itself check.");
		Coordinate head = snake.getSnakeBlocks().peekLast();
        if(head == null) {
			logger.log(Level.SEVERE, "Snake self collide check failed. Snake head is null.");
			throw new NullPointerException("Snake self collide check failed. Snake head is null.");
		}
		logger.log(Level.FINE, "Processing snake self collision check in a loop. Iterations number: {0}", snake.getSnakeBlocks().size());
        for(Coordinate snakeBlock: snake.getSnakeBlocks()) {
			logger.log(Level.FINER, "Head point: x:{0}, y:{1}. Snake block point: x:{2}, y:{3}", new Object[]{head.xCoord(), head.yCoord(), snakeBlock.xCoord(), snakeBlock.yCoord()});
            if(head.xCoord() == snakeBlock.xCoord() && head.yCoord() == snakeBlock.yCoord() && snakeBlock != head) {
                return true;
            }
        }
        return false;
	}
	
	/**
     * <p>Checks if snake head at the particular coordinate.</p>
	 * @param coordinate where head should be.
	 * @return true if snake's head at coordinate indeed.
     */
	@Override
	public boolean snakeHeadAt(Coordinate coordinate) {
		logger.log(Level.FINE, "Snake head at check.");
		if(coordinate == null) return false;
		Coordinate head = snake.getSnakeBlocks().peekLast();
		if(head == null) {
			logger.log(Level.SEVERE, "Snake head at check failed. Snake head is null.");
			throw new NullPointerException("Snake head at check failed. Snake head is null.");
		}
		logger.log(Level.FINE, "Head point: x:{0}, y:{1}. Arg point: x:{2}, y:{3}", new Object[]{head.xCoord(), head.yCoord(), coordinate.xCoord(), coordinate.yCoord()});
		return head.xCoord() == coordinate.xCoord() && head.yCoord() == coordinate.yCoord();
	}
	
	/**
     * <p>Resets state to initial.</p>
     */
	@Override
	public void resetState() {
        try {
			Properties props = PropertiesLoader.getProperties();
			snakeMovementRhythm = Integer.parseInt(props.getProperty("snake_move_delay"));
			snake.getSnakeBlocks().clear();
			snake.setDirection(Direction.RIGHT);
			logger.log(Level.FINE, "Snake manager state reset.");
        } catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage() + " " + Arrays.toString(e.getStackTrace()));
            throw new RuntimeException(e);
        }

	}
}
