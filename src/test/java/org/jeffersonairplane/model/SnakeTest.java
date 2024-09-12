package org.jeffersonairplane.model;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import static org.junit.jupiter.api.Assertions.*;

class SnakeTest {
    Snake snake;

    @BeforeEach
    void initSnakeInstance() {
        snake = new Snake(5, 10, 10);
    }

    @Test
    void grow() throws Exception {
        Snake expected = new Snake(5, 10, 10);
        Coordinate tail = expected.getSnakeBlocks().peekFirst();
        assert tail != null;
        expected.getSnakeBlocks().offerFirst(new Coordinate(tail.xCoord(), tail.yCoord()));
        snake.grow();
        assert expected.equals(snake);
    }

    @ParameterizedTest
    @NullSource
    @EnumSource(value = Direction.class)
    void step(Direction direction) {
        Snake expected = new Snake(5, 10, 10);
        expected.getSnakeBlocks().pollFirst();
        Direction newDir = expected.getCurrentDir();
        if(direction != null && ((newDir.equals(Direction.LEFT) && !direction.equals(Direction.RIGHT)) ||
                (newDir.equals(Direction.RIGHT) && !direction.equals(Direction.LEFT)) ||
                (newDir.equals(Direction.UP) && !direction.equals(Direction.DOWN)) ||
                (newDir.equals(Direction.DOWN) && !direction.equals(Direction.UP)))) {
            newDir = direction;
        }
        int headXCoord = expected.getSnakeBlocks().getLast().xCoord();
        int headYCoord = expected.getSnakeBlocks().getLast().yCoord();
        Coordinate newHead = switch(newDir) {
            case LEFT -> newHead = new Coordinate(--headXCoord, headYCoord);
            case UP -> newHead = new Coordinate(headXCoord, ++headYCoord);
            case RIGHT -> newHead = new Coordinate(++headXCoord, headYCoord);
            case DOWN -> newHead = new Coordinate(headXCoord, --headYCoord);
        };
        expected.getSnakeBlocks().offerLast(newHead);
        snake.step(direction);
        assert snake.equals(expected);
    }
}