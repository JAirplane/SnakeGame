package org.jeffersonairplane.model;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class FieldDimensionTest {
	
	@Test
	void ConstructorTest() {
		int x = Integer.MAX_VALUE;
		int y = Integer.MIN_VALUE;
		FieldDimension dimension = new FieldDimension(x, y);
		
		assertTrue(dimension.blocksAmountXAxis() == x && dimension.blocksAmountYAxis() == y);
	}

}