package org.jeffersonairplane.model;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class PlayingFieldTest {
	
	@Test
	void ConstructorTest() {
		int x = Integer.MAX_VALUE;
		int y = Integer.MIN_VALUE;
		PlayingField field = new PlayingField(x, y);
		
		assertTrue(field.getFieldWidth() == x && field.getFieldHeight() == y);
	}
	
	@Test
	void GetterSetterTest() {
		
		PlayingField field = new PlayingField(0, 0);
		int x = Integer.MAX_VALUE;
		int y = Integer.MIN_VALUE;
		field.setFieldWidth(x);
		field.setFieldHeight(y);
		
		assertTrue(field.getFieldWidth() == x && field.getFieldHeight() == y);
	}
}