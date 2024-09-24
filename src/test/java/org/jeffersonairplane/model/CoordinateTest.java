package org.jeffersonairplane.model;

import java.util.*;
import java.util.stream.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class CoordinateTest {

	static Stream<Arguments> equalsTestSource() {
		return Stream.of(
			arguments(true, Arrays.asList(Integer.MIN_VALUE, Integer.MIN_VALUE), Arrays.asList(Integer.MIN_VALUE, Integer.MIN_VALUE)),
			arguments(true, Arrays.asList(Integer.MAX_VALUE, Integer.MAX_VALUE), Arrays.asList(Integer.MAX_VALUE, Integer.MAX_VALUE)),
			arguments(true, Arrays.asList(0, 0), Arrays.asList(0, 0)),
			arguments(true, Arrays.asList(-12345, 12345), Arrays.asList(-12345, 12345)),
			arguments(false, Arrays.asList(555, 555), Arrays.asList(555, 554)),
			arguments(false, Arrays.asList(554, 556), Arrays.asList(555, 555))
		);
	}
	@ParameterizedTest
	@MethodSource("equalsTestSource")
	void equalsTest(boolean ans, List<Integer> firstCoordSource, List<Integer> secondCoordSource) {
		Coordinate coord1 = new Coordinate(firstCoordSource.get(0), firstCoordSource.get(1));
		Coordinate coord2 = new Coordinate(secondCoordSource.get(0), secondCoordSource.get(1));
		
		assertEquals(ans, coord1.equals(coord2));
	}
}