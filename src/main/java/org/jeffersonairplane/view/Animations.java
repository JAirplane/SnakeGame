package org.jeffersonairplane.view;

import org.jeffersonairplane.PropertiesLoader;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
* Class stores collections of colors.
* Using this one by one creates animation.
*/
public class Animations {
	
	/**
	* Snake animation when it has eaten apple power up.
	*/
	public List<Color> getAppleTakenSnakeAnimation() {
        try {
            Properties props = PropertiesLoader.getProperties();
			Color c1 = new Color(Integer.parseInt(props.getProperty("snake_color_red")),
					Integer.parseInt(props.getProperty("snake_color_green")),
					Integer.parseInt(props.getProperty("snake_color_blue")));
			Color c2 = new Color(Integer.parseInt(props.getProperty("snake_eat_apple_color_red")),
					Integer.parseInt(props.getProperty("snake_eat_apple_color_green")),
					Integer.parseInt(props.getProperty("snake_eat_apple_color_blue")));
			return List.of(c2,c2,c2,c2,
						c1,c1,c1,c1,
						c2,c2,c2,c2,
						c1,c1,c1,c1,
						c2,c2,c2,c2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
	}

	/**
	 * Snake animation when it has eaten tail cutter power up.
	 */
	public List<Color> getTailCutterTakenSnakeAnimation() {
		try {
			Properties props = PropertiesLoader.getProperties();
			Color c1 = new Color(Integer.parseInt(props.getProperty("snake_color_red")),
					Integer.parseInt(props.getProperty("snake_color_green")),
					Integer.parseInt(props.getProperty("snake_color_blue")));
			Color c2 = new Color(Integer.parseInt(props.getProperty("snake_eat_tailcutter_color_red")),
					Integer.parseInt(props.getProperty("snake_eat_tailcutter_color_green")),
					Integer.parseInt(props.getProperty("snake_eat_tailcutter_color_blue")));
			return List.of(c2,c2,c2,c2,
						c1,c1,c1,c1,
						c2,c2,c2,c2,
						c1,c1,c1,c1,
						c2,c2,c2,c2);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}