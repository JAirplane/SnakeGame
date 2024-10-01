package org.jeffersonairplane.model;

import java.util.function.Consumer;

/**
 * Controls and manages snake instance.
 */
public class SnakeManagerImpl implements SnakeManager {
	
    private final Snake snake;
	
	/**
	 * No args constructor.
	 * Creates empty {@link org.jeffersonairplane.model.Snake} with 0 blocks.
	 */
    public SnakeManagerImpl() {

		snake = new Snake();
    }
	
	/**
	 * Constructor
	 * @param snake is an option to create snake externally.
	 */
    public SnakeManagerImpl(Snake snake) {
		
		Snake snakeToSet = snake;
		if(snakeToSet == null) {
			snakeToSet = new Snake();
		}
		this.snake = snakeToSet;
    }
	
	/**
	 * Getter for {@link org.jeffersonairplane.model.Snake} instance
	 * @return current {@link org.jeffersonairplane.model.Snake} instance.
	 */
	@Override
	public Snake getSnake() {
		return snake;
	}
	
	private Coordinate getNextCoordinateToFillSnake(Coordinate current, Direction direction) {
		if(current == null) throw new NullPointerException("Coordinate is null");
		if(direction == null) throw new NullPointerException("Direction is null");
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
		return new Coordinate(x, y);
	}
	private int getActualSnakeSizeToFill(int snakeSize, Coordinate head, Direction direction, int fieldWidthInBlocks, int fieldHeightInBlocks) {
		if(head == null) throw new NullPointerException("Head coordinate is null");
		if(direction == null) throw new NullPointerException("Direction is null");
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
		
        for(int i = 0; i < actualSnakeSize - 1; i++) {
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
        snake.getSnakeBlocks().pollFirst();
		Coordinate head = snake.getSnakeBlocks().peekLast();
		if(head == null) throw new NullPointerException("Snake head is null");
        int headXCoord = head.xCoord();
        int headYCoord = head.yCoord();
        Coordinate newHead = switch(snake.getDirection()) {
            case LEFT -> new Coordinate(--headXCoord, headYCoord);
            case UP -> new Coordinate(headXCoord, ++headYCoord);
            case RIGHT -> new Coordinate(++headXCoord, headYCoord);
            case DOWN -> new Coordinate(headXCoord, --headYCoord);
        };
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
        if(newDirection != null && ((newDirection.equals(Direction.LEFT) && !currentDir.equals(Direction.RIGHT)) ||
                (newDirection.equals(Direction.RIGHT) && !currentDir.equals(Direction.LEFT)) ||
                (newDirection.equals(Direction.UP) && !currentDir.equals(Direction.DOWN)) ||
                (newDirection.equals(Direction.DOWN) && !currentDir.equals(Direction.UP)))) {

            snake.setDirection(newDirection);
			return true;
        }
        return false;
    }

    /**
     * <p>Applies any possible effects on snake (grow, speed up, etc...)</p>
     * @param powerUpEffect lambda that takes snake as arg and applies effect on it.
     */
    @Override
    public void changeSnakeState(Consumer<Snake> powerUpEffect) {
		if(powerUpEffect == null) return;
		powerUpEffect.accept(snake);
    }
	
	/**
     * <p>Checks if snake collided with play field borders.</p>
     * @param fieldWidth is amount of blocks on X axis.
	 * @param fieldHeight is amount of blocks on Y axis.
	 * @return true if snake collided with border.
     */
	@Override
	public boolean snakeCollideWithBorders(int fieldWidth, int fieldHeight) {
		Coordinate head = snake.getSnakeBlocks().peekLast();
        if(head == null) throw new NullPointerException();
        return head.xCoord() < 1 || head.xCoord() > fieldWidth
                || head.yCoord() < 1 || head.yCoord() > fieldHeight;
	}
	
	/**
     * <p>Checks if snake collided with itself.</p>
	 * @return true if snake collided with itself.
     */
	@Override
	public boolean snakeSelfCollide() {
		Coordinate head = snake.getSnakeBlocks().peekLast();
        if(head == null) throw new NullPointerException();
        for(Coordinate snakeBlock: snake.getSnakeBlocks()) {
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
	public boolean snakeHeadAt(Coordinate coordinate) {
		if(coordinate == null) return false;
		Coordinate head = snake.getSnakeBlocks().peekLast();
		if(head == null) throw new NullPointerException("Snake head is null");
		return head.xCoord() == coordinate.xCoord() && head.yCoord() == coordinate.yCoord();
	}
}
