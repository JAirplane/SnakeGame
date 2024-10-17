package org.jeffersonairplane.model;

import java.util.*;
import java.util.stream.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.*;

class PowerUpManagerImplTest {
	
	private PowerUpManagerImpl puManager;

	@BeforeAll
    static void setPowerUpCreationChances() {
		PowerUpTypes.APPLE.setCreationChance(0, 49);
		PowerUpTypes.TAILCUTTER.setCreationChance(50, 100);
	}

	@BeforeEach
	void initialization() {
		puManager = new PowerUpManagerImpl(3, 35, 70);
	}
	
	@Test
	void constructorTest() {
		puManager = new PowerUpManagerImpl(23, 24, 25);
		assertTrue(
			puManager.getPowerUps().isEmpty() && 
			puManager.getPowerUpCreationCountdowns().isEmpty() &&
			puManager.getPowerUpNumberLimit() == 23 &&
			puManager.getMinPowerUpCreationDelay() == 24 &&
			puManager.getMaxPowerUpCreationDelay() == 25);
	}
	
	@Test
	void constructorNegativeArgsTest() {
		puManager = new PowerUpManagerImpl(-100, -100, -100);
		assertTrue(
			puManager.getPowerUps().isEmpty() && 
			puManager.getPowerUpCreationCountdowns().isEmpty() &&
			puManager.getPowerUpNumberLimit() == 0 &&
			puManager.getMinPowerUpCreationDelay() == 0 &&
			puManager.getMaxPowerUpCreationDelay() == 0);
	}
	
	@Test
	void getRandomPowerUpTypeTest() {
		Object obj = puManager.getRandomPowerUpType();
        assertInstanceOf(PowerUpTypes.class, obj);
	}
	
	@Test
	void runNewPowerUpCountdownTest() {
		puManager.runNewPowerUpCountdown();
		
		for(var entry: puManager.getPowerUpCreationCountdowns().entrySet())
		{
			for(Integer countdown: entry.getValue()) {
				assertTrue(countdown >= 35 && countdown <= 70);
			}
		}
	}
	
	@Test
	void runNewPowerUpCountdownFewRunsTest() {
		puManager.runNewPowerUpCountdown();
		puManager.runNewPowerUpCountdown();
		puManager.runNewPowerUpCountdown();

		int size = 0;
		for(var entry: puManager.getPowerUpCreationCountdowns().entrySet()) {
			size += entry.getValue().size();
		}

        assertEquals(3, size);
	}
	
	@Test
	void runNewPowerUpCountdownAboveLimitTest() {
		puManager.runNewPowerUpCountdown();
		puManager.runNewPowerUpCountdown();
		puManager.runNewPowerUpCountdown();
		
		puManager.runNewPowerUpCountdown();
		int size = 0;
		for(var entry: puManager.getPowerUpCreationCountdowns().entrySet()) {
			size += entry.getValue().size();
		}
        assertEquals(3, size);
	}
	
	@Test
	void countdownWaitingPowerUpsTest() {
		puManager.runNewPowerUpCountdown();
		puManager.runNewPowerUpCountdown();
		puManager.runNewPowerUpCountdown();
		
		List<Integer> expectedCountdowns = new ArrayList<>();
		
		for(var entry: puManager.getPowerUpCreationCountdowns().entrySet())
		{
			for(Integer countdown: entry.getValue()) {
				expectedCountdowns.add(countdown - 1);
			}
		}
		
		puManager.countdownWaitingPowerUps();
		
		List<Integer> actualCountdowns = new ArrayList<>();
		
		for(var entry: puManager.getPowerUpCreationCountdowns().entrySet())
		{
            actualCountdowns.addAll(entry.getValue());
		}
		assertEquals(expectedCountdowns, actualCountdowns);
	}
	
	static Stream<Arguments> createPowerUpTestSource() {
		return Stream.of(
			arguments(PowerUpTypes.APPLE, new Coordinate(0, 0), true),
			arguments(null, new Coordinate(0, 0), false),
			arguments(PowerUpTypes.APPLE, null, false)
		);
	}
	@ParameterizedTest
	@MethodSource("createPowerUpTestSource")
	void createPowerUpTest(PowerUpTypes type, Coordinate point, boolean expectedReturn) {
		boolean created = puManager.createPowerUp(type, point);
		int expectedCollectionSize = expectedReturn ? 1 : 0;
		assertTrue(created == expectedReturn && puManager.getPowerUps().size() == expectedCollectionSize);
	}
	
	@Test
	void getPowerUpByPointTest() {
		Coordinate point = new Coordinate(5, 5);
		puManager.createPowerUp(PowerUpTypes.APPLE, point);
		var pu = puManager.getPowerUpByPoint(point);
		assertTrue(pu.isPresent() && pu.get() instanceof Apple && pu.get().getPoint().equals(point));
	}
	
	@Test
	void getPowerUpByPointNotFoundTest() {
		Coordinate point = new Coordinate(5, 5);
		puManager.createPowerUp(PowerUpTypes.APPLE, point);
		var pu = puManager.getPowerUpByPoint(new Coordinate(0, 0));
        assertFalse(pu.isPresent());
	}
	
	@Test
	void createPowerUpsTest() {
		var countdowns = puManager.getPowerUpCreationCountdowns();
		countdowns.put(PowerUpTypes.APPLE, new ArrayList<Integer>());
		countdowns.get(PowerUpTypes.APPLE).add(0);
		countdowns.get(PowerUpTypes.APPLE).add(0);
		
		puManager.createPowerUps(() -> new Coordinate(0, 0));
		
		assertTrue(countdowns.get(PowerUpTypes.APPLE).isEmpty() && puManager.getPowerUps().size() == 2);
	}
	
	@Test
	void removePowerUpTest() {
		puManager.createPowerUp(PowerUpTypes.APPLE, new Coordinate(0, 0));
		puManager.createPowerUp(PowerUpTypes.APPLE, new Coordinate(0, 0));
		var pu = puManager.getPowerUpByPoint(new Coordinate(0, 0));
		assertTrue(pu.isPresent());
		boolean removed = puManager.removePowerUp(pu.get());
		assertTrue(puManager.getPowerUps().size() == 1 && removed);
	}
	
	@Test
	void waitingAndExistsPowerUpsNumberTest1() {
		puManager.runNewPowerUpCountdown();
		puManager.runNewPowerUpCountdown();
		
		assertEquals(puManager.getWaitingAndExistingPowerUpsNumber(), 2);
	}
	
	@Test
	void waitingAndExistsPowerUpsNumberTest2() {
		puManager.runNewPowerUpCountdown();
		puManager.runNewPowerUpCountdown();
		puManager.getPowerUpCreationCountdowns().get(PowerUpTypes.APPLE).set(0, 0);
		puManager.createPowerUps(() -> new Coordinate(0, 0));
		
		assertEquals(2, puManager.getWaitingAndExistingPowerUpsNumber());
	}
	
	@Test
	void waitingAndExistsPowerUpsNumberTest3() {
		puManager.runNewPowerUpCountdown();
		puManager.runNewPowerUpCountdown();
		puManager.runNewPowerUpCountdown();
		puManager.runNewPowerUpCountdown();
		
		assertEquals(puManager.getWaitingAndExistingPowerUpsNumber(), 3);
	}
}