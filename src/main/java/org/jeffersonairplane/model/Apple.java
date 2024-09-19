package org.jeffersonairplane.model;

/**
* power up: snake grows for one block.
* @see org.jeffersonairplane.model.PowerUp
*/
public class Apple extends PowerUp {
	
	/**
	* Constructor.
	* @param point is a coordinate of power up.
	*/
    Apple(Coordinate point) {

		super(point);
    }
	/**
	 * Changes snake state.
	 * Snake grows by one block. This block coordinates are equal to tail at first.
	 * @param snake is a snake itself.
	*/
    @Override
    public void influence(Snake snake) {
		if(snake == null) throw new NullPointerException("Snake is null");
		Coordinate tail = snake.getSnakeBlocks().getFirst();
		Coordinate newTail = new Coordinate(tail.xCoord(), tail.yCoord());
		snake.getSnakeBlocks().offerFirst(newTail);
    }
}
