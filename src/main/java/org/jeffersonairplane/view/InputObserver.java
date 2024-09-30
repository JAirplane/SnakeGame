package org.jeffersonairplane.view;

import java.awt.event.KeyEvent;

/**
 * Observes user inputs via  {@link org.jeffersonairplane.view.UserInputObservable}.
 */
public interface InputObserver {
	
	/**
	 * Observes user inputs via  {@link org.jeffersonairplane.view.UserInputObservable}.
	 * @param key is a key input.
	 */
	void inputUpdate(KeyEvent key);
}