package org.jeffersonairplane.view;

import java.awt.*;
import java.util.List;

/**
* Class stores collections of colors.
* Using this one by one creates animation.
*/
public class Animations {
	
	/**
	* Snake animation when it yas eaten apple power up.
	*/
	public static List<Color> getAppleTakenSnakeAnimation() {
		return List.of(Color.YELLOW ,Color.YELLOW,Color.YELLOW,Color.YELLOW,
				Color.GREEN,Color.GREEN,Color.GREEN,Color.GREEN,
				Color.YELLOW,Color.YELLOW,Color.YELLOW,Color.YELLOW,
				Color.GREEN,Color.GREEN,Color.GREEN,Color.GREEN,
				Color.YELLOW,Color.YELLOW,Color.YELLOW,Color.YELLOW);
	}

}