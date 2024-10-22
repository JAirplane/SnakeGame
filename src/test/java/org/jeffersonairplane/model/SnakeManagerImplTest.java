package org.jeffersonairplane.model;

import java.util.function.*;
import java.util.stream.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.*;

class SnakeManagerImplTest {
	
	private SnakeManagerImpl snakeManager;
	
	@BeforeEach
	void initialization() {
		snakeManager = new SnakeManagerImpl(new Snake(), 7);
	}
	
	@Test
	void noArgsConstructionTest() {
		snakeManager = new SnakeManagerImpl();
		assertEquals(snakeManager.getSnake(), new Snake());
	}
	
	@Test
	void allArgsConstructionTest() {
		Snake estimate = new Snake();
		Snake snakeArg = new Snake();
		for(int i = 5; i > 0; i--) {
			estimate.getSnakeBlocks().offerFirst(new Coordinate(i, 1));
			snakeArg.getSnakeBlocks().offerFirst(new Coordinate(i, 1));
		}
		snakeManager = new SnakeManagerImpl(snakeArg, 7);
		assertTrue(snakeManager.getSnake().equals(estimate) && snakeManager.getSnakeMovementRhythm() == 7);
	}
	
	static Stream<Arguments> fillSnakeTestSource() {
		Snake normalEstimate = new Snake();
		for(int i = 0; i < 4; i++) {
			normalEstimate.getSnakeBlocks().offerFirst(new Coordinate(5 + i, 5));
			normalEstimate.setDirection(Direction.LEFT);
		}
		Snake tooLongEstimate = new Snake();
		for(int i = 0; i < 6; i++) {
			tooLongEstimate.getSnakeBlocks().offerFirst(new Coordinate(5 + i, 5));
			tooLongEstimate.setDirection(Direction.LEFT);
		}
		
		return Stream.of(
			arguments(4, new Coordinate(5, 5), Direction.LEFT, 10, 10, normalEstimate),
			arguments(10, new Coordinate(5, 5), Direction.LEFT, 10, 10, tooLongEstimate),
			arguments(4, new Coordinate(-1, 5), Direction.LEFT, 10, 10, new Snake()),
			arguments(4, new Coordinate(11, 5), Direction.LEFT, 10, 10, new Snake()),
			arguments(4, new Coordinate(5, -1), Direction.LEFT, 10, 10, new Snake()),
			arguments(4, new Coordinate(5, 11), Direction.LEFT, 10, 10, new Snake()),
			arguments(0, new Coordinate(5, 11), Direction.LEFT, 10, 10, new Snake()),
			arguments(4, null, Direction.LEFT, 10, 10, new Snake()),
			arguments(4, new Coordinate(5, 5), null, 10, 10, new Snake()),
			arguments(4, new Coordinate(5, 5), Direction.LEFT, 0, 10, new Snake()),
			arguments(4, new Coordinate(5, 5), Direction.LEFT, 10, 0, new Snake())
		);
	}
	@ParameterizedTest
	@MethodSource("fillSnakeTestSource")
	void fillSnakeTest(int snakeSize, Coordinate head, Direction direction, int fieldWidthInBlocks, int fieldHeightInBlocks, Snake estimate) {
		snakeManager.fillSnake(snakeSize, head, direction, fieldWidthInBlocks, fieldHeightInBlocks);
		assertEquals(snakeManager.getSnake(), estimate);
	}
	
	@Test
	void snakeStepTest() {
		snakeManager.fillSnake(5, new Coordinate(5, 5), Direction.RIGHT, 10, 10);
		Snake estimate = new Snake();
		for(int i = 0; i < 5; i++) {
			estimate.getSnakeBlocks().offerFirst(new Coordinate(6 - i, 5));
		}
		
		snakeManager.snakeStep();
		
        assertEquals(snakeManager.getSnake(), estimate);
	}
	
	@ParameterizedTest
	@EnumSource
	void changeSnakeDirectionTest(Direction newDirection) {
		Direction previous = snakeManager.getSnake().getDirection();
		snakeManager.changeSnakeDirection(newDirection);
		if(newDirection == Direction.LEFT || newDirection == Direction.RIGHT) {
			assertEquals(previous, snakeManager.getSnake().getDirection());
		}
		else {
			assertEquals(newDirection, snakeManager.getSnake().getDirection());
		}
	}
	
	static Stream<Consumer<Snake>> changeSnakeStateTestSource() {
		Apple apple = new Apple(new Coordinate(0,0));
		return Stream.of(
			apple::influence
		);
	}
	@ParameterizedTest
	@MethodSource("changeSnakeStateTestSource")
	void changeSnakeStateTest(Consumer<Snake> powerUp) {
		
		Snake estimate = new Snake();
		Snake test = snakeManager.getSnake();
		for(int i = 0; i < 5; i++) {
			estimate.getSnakeBlocks().offerFirst(new Coordinate(5 - i, 5));
			test.getSnakeBlocks().offerFirst(new Coordinate(5 - i, 5));
		}
		estimate.getSnakeBlocks().offerFirst(new Coordinate(1, 5));
		
		powerUp.accept(test);
		
		assertEquals(test, estimate);
	}
	
	static Stream<Arguments> snakeCollideWithBordersTestSource() {
		return Stream.of(
			arguments(10, 10, false),
			arguments(3, 3, true)
		);
	}
	@ParameterizedTest
	@MethodSource("snakeCollideWithBordersTestSource")
	void snakeCollideWithBordersTest(int fieldWidth, int fieldHeight, boolean answer) {
		Snake test = snakeManager.getSnake();
		for(int i = 0; i < 5; i++) {
			test.getSnakeBlocks().offerFirst(new Coordinate(5 - i, 5));
		}
		
		assertEquals(snakeManager.snakeCollideWithBorders(fieldWidth, fieldHeight), answer);
	}
	
	@Test
	void snakeSelfCollideFalseTest() {
		Snake test = snakeManager.getSnake();
		for(int i = 0; i < 6; i++) {
			test.getSnakeBlocks().offerFirst(new Coordinate(5 - i, 5));
		}
		
		assertFalse(snakeManager.snakeSelfCollide());
	}
	
	@Test
	void snakeSelfCollideTrueTest() {
		Snake test = snakeManager.getSnake();
		test.getSnakeBlocks().offerFirst(new Coordinate(5, 5));
		test.getSnakeBlocks().offerFirst(new Coordinate(4, 5));
		test.getSnakeBlocks().offerFirst(new Coordinate(4, 4));
		test.getSnakeBlocks().offerFirst(new Coordinate(5, 4));
		test.getSnakeBlocks().offerFirst(new Coordinate(5, 5));
		
		assertTrue(snakeManager.snakeSelfCollide());
	}
	
	static Stream<Arguments> snakeHeadAtTestSource() {
		return Stream.of(
			arguments(new Coordinate(5, 5), true),
			arguments(new Coordinate(4, 5), false),
			arguments(new Coordinate(0, 0), false),
			arguments(null, false)
		);
	}
	@ParameterizedTest
	@MethodSource("snakeHeadAtTestSource")
	void snakeHeadAtTest(Coordinate coordinate, boolean answer) {
		Snake test = snakeManager.getSnake();
		for(int i = 0; i < 5; i++) {
			test.getSnakeBlocks().offerFirst(new Coordinate(5 - i, 5));
		}
		
		assertEquals(snakeManager.snakeHeadAt(coordinate), answer);
	}
	
	@Test
	void resetStateTest() {
		Snake test = snakeManager.getSnake();
		for(int i = 0; i < 5; i++) {
			test.getSnakeBlocks().offerFirst(new Coordinate(5 - i, 5));
		}
		test.setDirection(Direction.UP);
		snakeManager.setSnakeMovementRhythm(1000);
		snakeManager.resetState();
		
		assertTrue(snakeManager.getSnake().getSnakeBlocks().isEmpty() && 
				snakeManager.getSnake().getDirection() == Direction.RIGHT && 
				snakeManager.getSnakeMovementRhythm() == 7);
	}
}