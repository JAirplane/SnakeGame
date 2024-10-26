package org.jeffersonairplane.model;

import java.util.*;
import java.util.stream.*;

import org.jeffersonairplane.viewmodel.Direction;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class SnakeTest {
	
	private Snake snake;
	
	@BeforeEach
	void snakeInitialize() {

		snake = new Snake();
	}
	
	@Test
	void noArgsConstructorTest() {
		boolean ans = snake.getSnakeBlocks().isEmpty() && snake.getDirection().equals(Direction.RIGHT);
		assertTrue(ans);
	}
	
	@ParameterizedTest
	@EnumSource
	void getAndSetDirectionTest(Direction direction) {
		snake.setDirection(direction);
		assertEquals(snake.getDirection(), direction);
	}
	
	@Test
	void getSnakeBlocksTest() {
		Deque<Coordinate> collectionToCompare = new LinkedList<>();
		Deque<Coordinate> snakeBlocks = snake.getSnakeBlocks();
		for(int i = 0; i < 10; i++) {
			Coordinate coord = new Coordinate(i, i + 1);
			collectionToCompare.offerFirst(coord);
			snakeBlocks.offerFirst(coord);
		}
        assertEquals(snake.getSnakeBlocks(), collectionToCompare);
	}

	static Stream<Arguments> equalsTestSource() {
		Snake badDirectionSnake = new Snake();
		badDirectionSnake.setDirection(Direction.LEFT);

		Snake otherBlocksSnake = new Snake();
		otherBlocksSnake.getSnakeBlocks().offerFirst(new Coordinate(100, 100));

		Snake estimateEquals = new Snake();
		for(int i = 0; i < 5; i++) {
			estimateEquals.getSnakeBlocks().offerFirst(new Coordinate(5 - i, 5));
		}
		return Stream.of(
				arguments(estimateEquals, true),
				arguments(badDirectionSnake, false),
				arguments(otherBlocksSnake, false)
		);
	}
	@ParameterizedTest
	@MethodSource("equalsTestSource")
	void equalsTest(Snake other, boolean ans) {
		for(int i = 0; i < 5; i++) {
			snake.getSnakeBlocks().offerFirst(new Coordinate(5 - i, 5));
		}
		assertEquals(snake.equals(other), ans);
	}
}