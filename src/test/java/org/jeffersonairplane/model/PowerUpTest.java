package org.jeffersonairplane.model;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class PowerUpTest {
	
	static class PowerUpTestImpl extends PowerUp {
		PowerUpTestImpl(Coordinate point) {
			super(point);
		}

		@Override
		public void influence(Snake snake) {}
	}

    @Test
	void constructorAndGetterTest() {
		Coordinate point = new Coordinate(-10, 10);
        PowerUpTestImpl powerUp = new PowerUpTestImpl(point);
        assertEquals(point, powerUp.getPoint());
	}
}