package org.jeffersonairplane.model;

import java.util.function.Consumer;

/**
 * Controls and manage snake.
 */
public interface SnakeManager {
	
	/**
     * <p>Getter for {@link org.jeffersonairplane.model.Snake} instance.</p>
	 * @return current snake instance.
     */
	public Snake getSnake();
	
    /**
     * <p>Fills snake blocks, commonly on initialization.</p>
     * @param snakeSize amount of snake blocks to fill.
     * @param head initial head coordinate on a field.
     * @param direction initial snake direction, tail should be created opposite to it.
     * @param fieldWidthInBlocks need to check that snake blocks inside the field.
     * @param fieldHeightInBlocks need to check that snake blocks inside the field.
     */
    boolean fillSnake(int snakeSize, Coordinate head, Direction direction, int fieldWidthInBlocks, int fieldHeightInBlocks);
    
	/**
     * <p>Moves snake one step forward in a current snake direction.</p>
     */
    void snakeStep();
    
	/**
     * <p>Changes current snake direction.</p>
     * Commonly do nothing if parameter is the same as current snake direction or opposite to it.
     * @param newDirection sets the direction of snake movement.
	 * @return true if direction was actually changed.
     */
    boolean changeSnakeDirection(Direction newDirection);
   
   /**
     * <p>Applies any possible effects on snake (grow, speed up, etc...)</p>
     * @param powerUpEffect lambda that takes snake as arg and applies effect on it.
     */
    void changeSnakeState(Consumer<Snake> powerUpEffect);
	
	/**
     * <p>Checks if snake collided with play field borders.</p>
     * @param fieldWidth is amount of blocks on X axis.
	 * @param fieldHeight is amount of blocks on Y axis.
	 * @return true if snake collided with border.
     */
	boolean snakeCollideWithBorders(int fieldWidth, int fieldHeight);
	
	/**
     * <p>Checks if snake collided with itself.</p>
	 * @return true if snake collided with itself.
     */
	boolean snakeSelfCollide();
	
	/**
     * <p>Checks if snake head at the particular coordinate.</p>
	 * @param coordinate where head should be.
	 * @return true if snake's head at coordinate indeed.
     */
	boolean snakeHeadAt(Coordinate coordinate);
}
