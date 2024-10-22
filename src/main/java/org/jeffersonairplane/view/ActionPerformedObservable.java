package org.jeffersonairplane.view;

import java.awt.event.ActionEvent;

/**
*	Registers, removes and notifies {@link org.jeffersonairplane.view.ActionPerformedObserver} about actions performed.
*/
public interface ActionPerformedObservable {
	
	/**
	* Registers {@link org.jeffersonairplane.view.ActionPerformedObserver}.
	* @param obs is observer.
	*/
	void registerActionObserver(ActionPerformedObserver obs);
	
	/**
	* Removes {@link org.jeffersonairplane.view.ActionPerformedObserver}.
	* @param obs is observer.
	*/
	void removeActionObserver(ActionPerformedObserver obs);
	
	/**
	* Notifies all registered {@link org.jeffersonairplane.view.ActionPerformedObserver}.
	* @param event is an event happened.
	*/
	void notifyActionObservers(ActionEvent event);
}