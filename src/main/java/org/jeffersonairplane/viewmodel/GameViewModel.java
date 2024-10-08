package org.jeffersonairplane.viewmodel;

import org.jeffersonairplane.view.*;

/**
 * ViewModel of MVVM pattern.
 * Input observer registered on view side to listen to user key inputs.
 */
public interface GameViewModel extends InputObserver {
	
	/**
     * Draws playingfield, snake and power ups.
     */
	void drawGame();

	/**
     * Represents one frame game iteration: logic + painting.
     */
	boolean gameOneFrame();
}
