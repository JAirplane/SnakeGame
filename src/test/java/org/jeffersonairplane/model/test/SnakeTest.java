package org.jeffersonairplane.model.test;

import org.jeffersonairplane.model.Coordinate;
import org.jeffersonairplane.model.Direction;
import org.jeffersonairplane.model.Snake;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class SnakeTest {

    @Test
    void changeDirectionTest() {
        Snake snake = new Snake(5, new Coordinate(5,5), 10);
        Random rnd = new Random();
        Direction[] allDirs = Direction.values();
        for(int i = 0; i < 1000; i++) {
            Direction snakeDirection = allDirs[rnd.nextInt(allDirs.length)];

            Direction newDirection = allDirs[rnd.nextInt(allDirs.length)];

        }

    }
}
