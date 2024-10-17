package org.jeffersonairplane.model;

/**
 * power up: snake tail cut for 2 blocks.
 * @see org.jeffersonairplane.model.PowerUp
 */
public class TailCutter extends PowerUp {
    /**
     * Constructor.
     *
     * @param point is a coordinate of power up.
     * @see Coordinate
     */
    TailCutter(Coordinate point) {
        super(point);
    }

    /**
     * changes snake state.
     *
     * @param snake is a snake itself.
     */
    @Override
    public void influence(Snake snake) {
        if(snake == null) throw new NullPointerException("Snake is null");
        for(int i = 0; i < 2; i++) {
            if(snake.getSnakeBlocks().size() > 2) {
                snake.getSnakeBlocks().pollFirst();
            }
        }
    }
}
