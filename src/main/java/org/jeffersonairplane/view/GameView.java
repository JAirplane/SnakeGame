package org.jeffersonairplane.view;

import java.awt.*;
import java.util.Collection;

/**
* View part of MVVM pattern.
*/
public interface GameView {
	
	/**
	* Getter
	* @return game window (shows playing field).
	*/
	GameWindow getGameWindow();
	
	/**
	* Getter
	* @return info window (shows gameplay info: score, tips etc...).
	*/
    InfoWindow getInfoWindow();
	
	/**
	* Sequence of colors to paint snake.
	* @param colors is a collection of colors to paint snake with.
	*/
    void setSnakeAnimation(Collection<Color> colors);
	
	/**
	* Repaints info window.
	*/
    void repaintInfoWindow();
	
	/**
	* Repaints game window.
	*/
    void repaintGameWindow();
	
	/**
	* Sets player score.
	* @param score to set.
	*/
    void setScore(long score);
	
	/**
	* Resets view state to initial.
	*/
	void resetState();
	
	/**
	* Sets color to every Power Up in the game.
	*/
    void setPowerUpColors();
}