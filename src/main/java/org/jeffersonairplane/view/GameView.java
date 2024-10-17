package org.jeffersonairplane.view;

import java.awt.*;
import java.util.Collection;

/**
* View part of MVVM pattern.
*/
public interface GameView {
	
	GameWindow getGameWindow();
    InfoWindow getInfoWindow();
    void setSnakeAnimation(Collection<Color> colors);
    void repaintInfoWindow();
    void repaintGameWindow();
    void setScore(long score);
	void resetState();
    void setPowerUpColors();
}