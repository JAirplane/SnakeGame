package org.jeffersonairplane.view;

import org.jeffersonairplane.PropertiesLoader;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.function.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import lombok.*;

public class MenuWindow extends JPanel {

	private final int powerUpsAmountMaxLimit;
	private int powerUpsAmountChosenLimit;
	
	private MapSize mapSize;
	
	private final JButton startGameButton;
	private final JButton powerUpAmountChangerButton;
	private final JButton fieldSizeButton;
	private final JButton exitButton;



	JLabel powerUpAmountChangerLabel;
	JLabel fieldSizeLabel;
	
	@Setter
	private Consumer<WindowEvent> exitGame;
	@Setter
	private Consumer<ChosenSettingsDTO> settingsSetter;
	@Setter
	private Runnable gameRunner;
	@Setter
	private Runnable switchToGameplay;
	@Setter
	WindowEvent exitEvent;
	private final Logger logger = Logger.getLogger(getClass().getName());

	ActionListener actionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent event) {
			Object source = event.getSource();
			if(source == startGameButton) {
				settingsSetter.accept(new ChosenSettingsDTO(powerUpsAmountChosenLimit,
						mapSize.getXAxisBlocks(), mapSize.getYAxisBlocks()));
				switchToGameplay.run();
				gameRunner.run();
			}
			else if(source == powerUpAmountChangerButton) {
				updateCurrentPowerUpsAmount();
			}
			else if(source == fieldSizeButton) {
				updateMapSize();
			}
			else if(source == exitButton) {
				exitGame.accept(exitEvent);
			}
		}
	};

	public MenuWindow() {
		try {
			Properties props = PropertiesLoader.getProperties();
			powerUpsAmountChosenLimit = Integer.parseInt(props.getProperty("pu_number_limit"));
			powerUpsAmountMaxLimit = Integer.parseInt(props.getProperty("pu_number_limit_cap"));
			mapSize = MapSize.MEDIUM;
			
			startGameButton = new JButton(props.getProperty("start_game_button_title"));
			startGameButton.addActionListener(actionListener);
			powerUpAmountChangerButton = new JButton(props.getProperty("power_ups_amount_button_title"));
			powerUpAmountChangerButton.addActionListener(actionListener);
			fieldSizeButton = new JButton(props.getProperty("field_size_button_title"));
			fieldSizeButton.addActionListener(actionListener);
			exitButton = new JButton(props.getProperty("exit_button_title"));
			exitButton.addActionListener(actionListener);
			
			int width = Integer.parseInt(props.getProperty("game_window_width"));
			int height = Integer.parseInt(props.getProperty("info_window_height"))
					+ Integer.parseInt(props.getProperty("game_window_height"));
			setPreferredSize(new Dimension(width, height));
			GridLayout layout = new GridLayout(4,2);
			layout.setHgap(10);
			layout.setVgap(10);
			setLayout(layout);
			
			add(startGameButton);
			add(new JLabel());
			add(powerUpAmountChangerButton);
			powerUpAmountChangerLabel = new JLabel(String.valueOf(powerUpsAmountChosenLimit));
			add(powerUpAmountChangerLabel);
			add(fieldSizeButton);
			fieldSizeLabel = new JLabel(String.valueOf(mapSize));
			add(fieldSizeLabel);
			add(exitButton);
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage() + " " + Arrays.toString(e.getStackTrace()));
			throw new RuntimeException(e);
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
		fieldSizeLabel.setText(String.valueOf(mapSize));
	}
}