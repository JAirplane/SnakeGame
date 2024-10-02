package org.jeffersonairplane.model;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.*;

import org.mockito.*;
import org.mockito.junit.jupiter.*;

//import org.mockito.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.logging.*;
import java.util.stream.*;

@ExtendWith(MockitoExtension.class)
class GameModelImplTest {
	
	@Spy
	SnakeManager snakeManager = new SnakeManagerImpl();
	
	@Mock
	Logger logger;

	private GameModelImpl model;
	
	@BeforeEach
	void initializeModel() {
		for(int i = 0; i < 5; i++) {
			snakeManager.getSnake().getSnakeBlocks().offerFirst(new Coordinate(5 - i, 5));
		}
		model = new GameModelImpl(new FieldDimension(10, 10), snakeManager);
	}
	
	static Stream<Arguments> checkCollisionsTestSource() {
		return Stream.of(
			arguments(true, true, true),
			arguments(false, true, true),
			arguments(true, false, true),
			arguments(false, false, false)
		);
	}
	@ParameterizedTest
	@MethodSource("checkCollisionsTestSource")
	void checkCollisionsTest(boolean borderCollide, boolean selfCollide, boolean answer) {
		doReturn(borderCollide).when(snakeManager).snakeCollideWithBorders(anyInt(), anyInt());
		if(!borderCollide) {
			doReturn(selfCollide).when(snakeManager).snakeSelfCollide();
		}
		boolean methodAns = model.checkCollisions();

		assertEquals(methodAns, answer);
	}
	
	@Test
	void checkCollisionsExceptionTest() {
		when(snakeManager.snakeCollideWithBorders(anyInt(), anyInt())).thenThrow(new NullPointerException());
		
		assertThrows(NullPointerException.class, () -> model.checkCollisions(), "model.checkCollisions() do not throw exception");
	}
	
	@Test
	void getSnakeTest() {
		Snake snake = new Snake();
		for(int i = 0; i < 5; i++) {
			snake.getSnakeBlocks().addFirst(new Coordinate(5 - i, 5));
		}
		doReturn(snake).when(snakeManager).getSnake();
		
		assertEquals(model.getSnake(), snake);
	}
	
	@Test
	void getFieldDimensionTest() {
		FieldDimension dimension = new FieldDimension(10, 10);
		
		assertTrue(dimension.blocksAmountXAxis() == model.getFieldDimension().blocksAmountXAxis() &&
				dimension.blocksAmountYAxis() == model.getFieldDimension().blocksAmountYAxis());
	}
	
	@ParameterizedTest
	@EnumSource
	void changeSnakeDirectionTest(Direction direction) {
		Snake snake = snakeManager.getSnake();
		for(int i = 0; i < 5; i++) {
			snake.getSnakeBlocks().addFirst(new Coordinate(5 - i, 5));
		}
		
		model.changeSnakeDirection(direction);
		
		switch(direction) {
			case Direction.LEFT, Direction.RIGHT -> assertSame(snake.getDirection(), Direction.RIGHT);
			case Direction.UP -> assertSame(snake.getDirection(), Direction.UP);
            case Direction.DOWN -> assertSame(snake.getDirection(), Direction.DOWN);
		}
	}
	
	@Test
	void snakeMoveTest() {
		Snake snake = snakeManager.getSnake();
		Snake estimate = new Snake();
		for(int i = 0; i < 5; i++) {
			estimate.getSnakeBlocks().addFirst(new Coordinate(6 - i, 5));
		}
		
		model.snakeMove();
		
		assertEquals(snake, estimate);
	}
	
	@Test
	void createAndGetPowerUpTest() {
		Coordinate coordinate = new Coordinate(0, 0);
		
		model.createPowerUp(PowerUpTypes.APPLE, coordinate);
		
		assertTrue(model.getPowerUps().getFirst() instanceof Apple &&
				model.getPowerUps().getFirst().getPoint().equals(coordinate));
	}
	
	@Test
	void powerUpTakenTest() {
		Snake snake = snakeManager.getSnake();
		for(int i = 0; i < 5; i++) {
			snake.getSnakeBlocks().addFirst(new Coordinate(5 - i, 5));
		}
		Coordinate coordinate = new Coordinate(5, 5);
		
		model.createPowerUp(PowerUpTypes.APPLE, coordinate);
		
		assertTrue(model.powerUpTaken().isPresent());
	}
	
	@Test
	void powerUpEffectTest() {
		Snake snake = snakeManager.getSnake();
		Snake estimate = new Snake();
		for(int i = 0; i < 5; i++) {
			estimate.getSnakeBlocks().offerFirst(new Coordinate(5 - i, 5));
		}
		estimate.getSnakeBlocks().offerFirst(new Coordinate(1, 5));
		model.createPowerUp(PowerUpTypes.APPLE, new Coordinate(5, 5));

		if(model.powerUpTaken().isPresent()) {
			model.powerUpEffect(model.powerUpTaken().get());
		}
        assertEquals(snake, estimate);
	}
	
	@Test
	void createPowerUpTest() {
		model.createPowerUp(PowerUpTypes.APPLE, new Coordinate(1, 1));
		PowerUp p = model.getPowerUps().getFirst();
		
		assertTrue(p.getPoint().xCoord() == 1 && p.getPoint().yCoord() == 1 && p instanceof Apple);
	}
}