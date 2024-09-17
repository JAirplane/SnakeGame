package org.jeffersonairplane.model;

public class Apple extends PowerUp {
    Apple(Coordinate point) {
        super(point);
    }

    @Override
    public void influence(Snake snake) {
        snake.grow();
    }
}
