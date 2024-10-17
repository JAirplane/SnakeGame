package org.jeffersonairplane.model;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PowerUpTypesTest {
	
	private final Random rnd = new Random();
	
	@ParameterizedTest
	@EnumSource
	public void setCreationChanceTest(PowerUpTypes type) {
		int min = 0;
		int max = 50;
		type.setCreationChance(min, max);
		assertTrue(min == type.getMinChance() && max == type.getMaxChance());
	}
	
	@Test
	public void setCreationChanceBadValueTest() {
		PowerUpTypes.APPLE.setCreationChance(0, 10);
		int max = 1000;
		int min = 10;
		PowerUpTypes.APPLE.setCreationChance(min, max);
		assertTrue(0 == PowerUpTypes.APPLE.getMinChance() && 10 == PowerUpTypes.APPLE.getMaxChance());
	}

	@Test
	public void setCreationChanceMinMoreThanMaxTest() {
		PowerUpTypes.APPLE.setCreationChance(0, 10);
		int max = 10;
		int min = 90;
		PowerUpTypes.APPLE.setCreationChance(min, max);
		assertTrue(0 == PowerUpTypes.APPLE.getMinChance() && 10 == PowerUpTypes.APPLE.getMaxChance());
	}
}