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
import java.util.*;

@ExtendWith(MockitoExtension.class)
class GameModelImplTest {
	
	@Spy
	SnakeManager snakeManager = new SnakeManagerImpl();

	@Spy
	PowerUpManager powerUpManager = new PowerUpManagerImpl(3, 35, 70);
	
	@Mock
	Logger logger;

	private GameModelImpl model;
	
	@BeforeEach
	void initializeModel() {
		for(int i = 0; i < 5; i++) {
			snakeManager.getSnake().getSnakeBlocks().offerFirst(new Coordinate(5 - i, 5));
		}
		model = new GameModelImpl(
				new FieldDimension(10, 10),
				snakeManager, powerUpManager);
	}
	
	@Test
	void getSnakeTest() {
		Snake snake = new Snake();
		for(int i = 0; i < 5; i++) {
			snake.getSnakeBlocks().offerFirst(new Coordinate(5 - i, 5));
		}
		doReturn(snake).when(snakeManager).getSnake();
		
		assertEquals(model.getSnake(), snake);
	}
	
	@Test
	void getPowerUpsTest() {
		List<PowerUp> powerUpCollection = new ArrayList<>();
		powerUpCollection.add(new Apple(new Coordinate(0, 0)));
		doReturn(powerUpCollection).when(powerUpManager).getPowerUps();
		
		assertTrue(powerUpCollection.equals(model.getPowerUps()));
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
		when(snakeManager.snakeCollideWithBorders(anyInt(), anyInt())).thenThrow(new NullPointerException("OK Test Exception"));
		
		assertThrows(NullPointerException.class, () -> model.checkCollisions(), "model.checkCollisions() do not throw exception");
	}
	
	
	
	@ParameterizedTest
	@EnumSource
	void changeSnakeDirectionTest(Direction direction) {
		Snake snake = snakeManager.getSnake();
		
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
	void powerUpEffectTest() {
		Snake snake = snakeManager.getSnake();
		Snake estimate = new Snake();
		for(int i = 0; i < 5; i++) {
			estimate.getSnakeBlocks().offerFirst(new Coordinate(5 - i, 5));
		}
		estimate.getSnakeBlocks().offerFirst(new Coordinate(1, 5));

		model.powerUpEffect(new Apple(new Coordinate(5, 5)));
        assertEquals(snake, estimate);
	}

	static Stream<Arguments> coordinateIsFreeTestSource() {
		return Stream.of(
				arguments(new Coordinate(0, 0), true),
				arguments(new Coordinate(5, 5), false),
				arguments(new Coordinate(7, 7), false)
		);
	}
	@ParameterizedTest
	@MethodSource("coordinateIsFreeTestSource")
	void coordinateIsFreeTest(Coordinate point, boolean answer) {
		powerUpManager.createPowerUp(PowerUpTypes.APPLE, new Coordinate(7, 7));

		assertEquals(model.coordinateIsFree(point), answer);
	}

	@Test
	void getNewFreeCoordinateTest() {
		powerUpManager.createPowerUp(PowerUpTypes.APPLE, new Coordinate(7, 7));

		Coordinate free = model.getNewFreeCoordinate();
		assertTrue(model.coordinateIsFree(free));
	}

	@ParameterizedTest
	@ValueSource(longs = {0, Long.MAX_VALUE, Long.MIN_VALUE, 100})
	void oneFrameGameActionFramesCounterTest(long frames) {
		long expected = 0;
		if(frames != Long.MAX_VALUE) expected = frames + 1;

		model.setFramesCounter(frames);
		model.oneFrameGameAction();

		assertEquals(expected, model.getFramesCounter());
	}
}