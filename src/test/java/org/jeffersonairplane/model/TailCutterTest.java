package org.jeffersonairplane.model;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TailCutterTest {
    TailCutter tailCutter;
    Snake snake;

    @BeforeEach
    void initialization() {
        tailCutter = new TailCutter(new Coordinate(0,0));
        snake = new Snake();
    }

    @Test
    void influenceTest() {
        Snake estimate = new Snake();
        for(int i = 10; i > 0; i--) {
            estimate.getSnakeBlocks().offerFirst(new Coordinate(i, 1));
            snake.getSnakeBlocks().offerFirst(new Coordinate(i, 1));
        }
        estimate.getSnakeBlocks().pollFirst();
        estimate.getSnakeBlocks().pollFirst();

        tailCutter.influence(snake);

        assertEquals(snake, estimate);
    }
}
