package org.jeffersonairplane.view;

import java.awt.event.ActionEvent;

/**
*	Listen to actions performed and do stuff when it happens.
*/
public interface ActionPerformedObserver {
	
	/**
	* Perform logic when action happens.
	* @param event is an event happened.
	*/
	void actionUpdate(ActionEvent event);
}