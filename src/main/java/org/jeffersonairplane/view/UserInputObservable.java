package org.jeffersonairplane.view;

import java.awt.event.KeyEvent;

/**
 * Registers, removes and notifies {@link org.jeffersonairplane.view.InputObserver} about user keyboard inputs.
 */
interface UserInputObservable {
	
	/**
	 * Registers {@link org.jeffersonairplane.view.InputObserver}.
	 * @param obs is an observer to register.
	 */
	void registerInputObserver(InputObserver obs);
	
	/**
	 * Removes {@link org.jeffersonairplane.view.InputObserver}.
	 * @param obs is an observer to remove.
	 */
	void removeInputObserver(InputObserver obs);
	
	/**
	 * Notifies {@link org.jeffersonairplane.view.InputObserver} about user keyboard inputs.
	 */
	void notifyInputObservers(KeyEvent key);
}