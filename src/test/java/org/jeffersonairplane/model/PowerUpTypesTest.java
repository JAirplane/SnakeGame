package org.jeffersonairplane.model;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.*;

class PowerUpTypesTest {
	
	private final Random rnd = new Random();
	
	@ParameterizedTest
	@EnumSource
	public void setCreationChanceTest(PowerUpTypes types) {
		int chance = rnd.nextInt(101);
		types.setCreationChance(chance);
		assertEquals(chance, types.getCreationChance());
	}
	
	@Test
	public void setCreationChanceBadValueTest() {
		PowerUpTypes.APPLE.setCreationChance(0);
		int chance = 1000;
		PowerUpTypes.APPLE.setCreationChance(chance);
		assertEquals(0, PowerUpTypes.APPLE.getCreationChance());
	}
}