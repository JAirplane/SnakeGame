package org.jeffersonairplane.view;

import org.jeffersonairplane.PropertiesLoader;

import java.awt.event.*;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class MenuWindow extends JPanel implements ActionListener, ActionPerformedObservable {
	
	private final List<ActionPerformedObserver> observers = new ArrayList<>();
	
	private int powerUpsAmount;
	private int xAxisBlocks;
	private int yAxisBlocks;
	
	JButton startGame;
	JButton powerUpAmountChanger;
	JButton fieldSize = new JButton("Field size");
	JButton exit = new JButton("Exit");

	private final Logger logger = Logger.getLogger(getClass().getName());

	public MenuWindow() {
		try {
			Properties props = PropertiesLoader.getProperties();
			powerUpsAmount = Integer.parseInt(props.getProperty("pu_number_limit"));
			xAxisBlocks = Integer.parseInt(props.getProperty("blocks_amount_x"));
			yAxisBlocks = Integer.parseInt(props.getProperty("blocks_amount_y"));
			startGame = new JButton(props.getProperty("start_game_button_title"));
			powerUpAmountChanger = new JButton(props.getProperty("power_ups_amount_button_title"));
			startGame = new JButton(props.getProperty("start_game_button_title"));
			startGame = new JButton(props.getProperty("start_game_button_title"));
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage() + " " + Arrays.toString(e.getStackTrace()));
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		if(source == startGame) {
			notifyActionObservers(event);
		}
	}

	/**
	 * Registers {@link org.jeffersonairplane.view.ActionPerformedObserver}.
	 *
	 * @param obs is observer.
	 */
	@Override
	public void registerActionObserver(ActionPerformedObserver obs) {
		if(!observers.contains(obs)) {
			observers.add(obs);
		}
	}

	/**
	 * Removes {@link org.jeffersonairplane.view.ActionPerformedObserver}.
	 *
	 * @param obs is observer.
	 */
	@Override
	public void removeActionObserver(ActionPerformedObserver obs) {
		observers.remove(obs);
	}

	/**
	 * Notifies all registered {@link org.jeffersonairplane.view.ActionPerformedObserver}.
	 *
	 * @param event is an event happened.
	 */
	@Override
	public void notifyActionObservers(ActionEvent event) {
		for(var obs: observers) {
			obs.actionUpdate(event);
		}
	}
}