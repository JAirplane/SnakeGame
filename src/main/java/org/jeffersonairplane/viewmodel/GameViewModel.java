package org.jeffersonairplane.viewmodel;

import org.jeffersonairplane.model.PowerUpTakenObserver;
import org.jeffersonairplane.view.*;

/**
 * ViewModel of MVVM pattern.
 * Input observer registered on view side to listen to user key inputs.
 */
public interface GameViewModel extends InputObserver, PowerUpTakenObserver {
	
	/**
     * Draws playing field, snake and power ups.
     */
	void drawGame();

	/**
     * Represents one frame game iteration: logic + painting.
     */
	boolean gameOneFrame();

	/**
	 * Runs gameplay process.
	 */
	void runGameplay();

	/**
	 * Reruns gameplay process after game over.
	 */
	void rerunAfterGameOver();
}
