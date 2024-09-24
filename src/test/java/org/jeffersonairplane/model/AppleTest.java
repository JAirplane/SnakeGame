package org.jeffersonairplane.model;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class AppleTest {
	
	Apple apple;
	Snake snake;
	
	@BeforeEach
	void initialization() {
		apple = new Apple(new Coordinate(0,0));
		snake = new Snake();
	}
	
	@Test
	void influenceTest() {
		Snake estimate = new Snake();
		for(int i = 10; i > 0; i--) {
			estimate.getSnakeBlocks().offerFirst(new Coordinate(i, 1));
			snake.getSnakeBlocks().offerFirst(new Coordinate(i, 1));
		}
		Coordinate tail = estimate.getSnakeBlocks().peekFirst();
		if(tail == null) throw new NullPointerException("Estimate snake tail is null");
		estimate.getSnakeBlocks().offerFirst(new Coordinate(tail.xCoord(), tail.yCoord()));
		
		apple.influence(snake);
		
		assertEquals(snake, estimate);
	}
}