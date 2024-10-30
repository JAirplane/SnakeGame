package org.jeffersonairplane.view;

import org.jeffersonairplane.PropertiesLoader;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.lang.reflect.Field;
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

	@Getter @Setter
	private Color background;
	@Getter @Setter
	private Color textColor;

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
			
			startGameButton = setButtonSettings(props.getProperty("start_game_button_title"));
			startGameButton.addActionListener(actionListener);
			powerUpAmountChangerButton = setButtonSettings(props.getProperty("power_ups_amount_button_title"));
			powerUpAmountChangerButton.addActionListener(actionListener);
			fieldSizeButton = setButtonSettings(props.getProperty("field_size_button_title"));
			fieldSizeButton.addActionListener(actionListener);
			exitButton = setButtonSettings(props.getProperty("exit_button_title"));
			exitButton.addActionListener(actionListener);

			int width = Integer.parseInt(props.getProperty("game_window_width"));
			int height = Integer.parseInt(props.getProperty("info_window_height"))
					+ Integer.parseInt(props.getProperty("game_window_height"));
			Field backgroundColor = Class.forName("java.awt.Color").getField(props.getProperty("playing_field_background_color"));
			background = (Color)backgroundColor.get(null);
			this.setBackground(background);

			setPreferredSize(new Dimension(width, height));
			setBackground(background);
			//setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

			JPanel container = new JPanel();
			container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
			JLabel gameLabel = new JLabel("Snake Game");
			container.add(gameLabel);
			container.add(Box.createRigidArea(new Dimension(0, 20)));
			container.add(startGameButton, BorderLayout.CENTER);
			container.add(Box.createRigidArea(new Dimension(0, 20)));
			container.add(powerUpAmountChangerButton);
			container.add(Box.createRigidArea(new Dimension(0, 20)));
			container.add(fieldSizeButton);
			container.add(Box.createRigidArea(new Dimension(0, 20)));
			container.add(exitButton, BorderLayout.CENTER);
			add(container);

			container.setBackground(background);

			powerUpAmountChangerLabel = new JLabel(String.valueOf(powerUpsAmountChosenLimit));
			setComponentSize(powerUpAmountChangerLabel);
			add(powerUpAmountChangerLabel);
			fieldSizeLabel = new JLabel(String.valueOf(mapSize));
			setComponentSize(fieldSizeLabel);
			add(fieldSizeLabel);


			Field infoTextColor = Class.forName("java.awt.Color").getField(props.getProperty("info_text_color"));
			textColor = (Color)infoTextColor.get(null);

			powerUpAmountChangerLabel.setForeground(textColor);
			powerUpAmountChangerLabel.setFont(new Font(props.getProperty("gameplay_info_label_font"), Font.BOLD,
					Integer.parseInt(props.getProperty("gameplay_info_label_font_size"))));
			fieldSizeLabel.setForeground(textColor);
			fieldSizeLabel.setFont(new Font(props.getProperty("gameplay_info_label_font"), Font.BOLD,
					Integer.parseInt(props.getProperty("gameplay_info_label_font_size"))));
			gameLabel.setForeground(textColor);
			gameLabel.setFont(new Font(props.getProperty("gameplay_info_label_font"), Font.BOLD,
					Integer.parseInt(props.getProperty("gameplay_info_label_font_size"))));
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage() + " " + Arrays.toString(e.getStackTrace()));
			throw new RuntimeException(e);
		}
	}

	private void setButtonLook(JButton btn) throws IOException {
		Properties prop = PropertiesLoader.getProperties();
		btn.setFocusPainted(Boolean.parseBoolean(prop.getProperty("btn_focus_painted")));
		btn.setBackground(new Color(Integer.parseInt(prop.getProperty("btn_background_red")),
				Integer.parseInt(prop.getProperty("btn_background_green")),
				Integer.parseInt(prop.getProperty("btn_background_blue"))));
		btn.setForeground(new Color(Integer.parseInt(prop.getProperty("btn_foreground_red")),
				Integer.parseInt(prop.getProperty("btn_foreground_green")),
				Integer.parseInt(prop.getProperty("btn_foreground_blue"))));
		setFont(new Font(prop.getProperty("btn_font_title"),
				Integer.parseInt(prop.getProperty("btn_font_type")),
				Integer.parseInt(prop.getProperty("btn_font_size"))));
	}

	private void setComponentSize(JComponent btn) throws IOException {
		Properties prop = PropertiesLoader.getProperties();
		btn.setPreferredSize(new Dimension(Integer.parseInt(prop.getProperty("btn_preferred_width")),
				Integer.parseInt(prop.getProperty("btn_preferred_height"))));
		btn.setMinimumSize(new Dimension(Integer.parseInt(prop.getProperty("btn_preferred_width")),
				Integer.parseInt(prop.getProperty("btn_preferred_height"))));
		btn.setMaximumSize(new Dimension(Integer.parseInt(prop.getProperty("btn_preferred_width")),
				Integer.parseInt(prop.getProperty("btn_preferred_height"))));
	}

	private JButton setButtonSettings(String title) throws IOException {
		JButton btn = new JButton(title);
		setButtonLook(btn);
		setComponentSize(btn);
		return btn;
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