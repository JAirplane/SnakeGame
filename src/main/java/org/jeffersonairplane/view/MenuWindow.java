package org.jeffersonairplane.view;

import org.jeffersonairplane.PropertiesLoader;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class MenuWindow extends JPanel implements ActionListener, ActionPerformedObservable {
	
	private final List<ActionPerformedObserver> observers = new ArrayList<>();
	
	private int powerUpsAmountMaxLimit;
	private int powerUpsAmountChosenLimit;
	
	private MapSize mapSize;
	
	JButton startGame;
	JButton powerUpAmountChanger;
	JButton fieldSize;
	JButton exit;
	
	JLabel powerUpAmountChangerLabel;
	JLabel fieldSizeLabel;

	private final Logger logger = Logger.getLogger(getClass().getName());

	public MenuWindow() {
		try {
			Properties props = PropertiesLoader.getProperties();
			powerUpsAmountChosenLimit = Integer.parseInt(props.getProperty("pu_number_limit"));
			powerUpsAmountMaxLimit = Integer.parseInt(props.getProperty("pu_number_limit_cap"));
			mapSize = MapSize.MEDIUM;
			
			startGame = new JButton(props.getProperty("start_game_button_title"));
			powerUpAmountChanger = new JButton(props.getProperty("power_ups_amount_button_title"));
			fieldSize = new JButton(props.getProperty("start_game_button_title"));
			exit = new JButton(props.getProperty("start_game_button_title"));
			
			int width = Integer.parseInt(props.getProperty("game_window_width"));
			int height = Integer.parseInt(props.getProperty("info_window_height"))
					+ Integer.parseInt(props.getProperty("game_window_height"));
			setPreferredSize(new Dimension(width, height));
			GridLayout layout = new GridLayout(4,2);
			layout.setHgap(10);
			layout.setVgap(10);
			setLayout(layout);
			
			add(startGame);
			add(new JLabel());
			add(powerUpAmountChanger);
			powerUpAmountChangerLabel = new JLabel(String.valueOf(powerUpsAmountChosenLimit));
			add(powerUpAmountChanger);
			add(fieldSize);
			fieldSizeLabel = new JLabel(String.valueOf(mapSize));
			add(fieldSizeLabel);
			add(exit);
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
	
	public void updateCurrentPowerUpsAmount() {
		if(powerUpsAmountChosenLimit >= powerUpsAmountMaxLimit) {
			powerUpsAmountChosenLimit = 1;
		}
		else ++powerUpsAmountChosenLimit;
		powerUpAmountChangerLabel.setText(String.valueOf(powerUpsAmountChosenLimit));
	}
	
	public void updateMapSize() {
		switch(mapSize) {
			case SMALL -> mapSize = MapSize.MEDIUM;
			case MEDIUM -> mapSize = MapSize.LARGE;
			case LARGE -> mapSize = MapSize.SMALL;
			default -> throw new IllegalStateException("Unknown MapSize state.");
		}
	}
}