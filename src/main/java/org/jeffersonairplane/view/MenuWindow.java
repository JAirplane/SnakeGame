package org.jeffersonairplane.view;

import org.jeffersonairplane.PropertiesLoader;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
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
	private final String powerUpAmountChangerButtonMessage;
	private final JButton fieldSizeButton;
	private final String fieldSizeButtonMessage;
	private final JButton exitButton;

	@Getter @Setter
	private Color background;
	@Getter @Setter
	private Color gameTitleColor;
	@Getter @Setter
	private Color controlsExplanationColor;
	
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
			int btn_width = Integer.parseInt(props.getProperty("btn_preferred_width"));
			int btn_height = Integer.parseInt(props.getProperty("btn_preferred_height"));
			startGameButton = setButtonSettings(props.getProperty("start_game_button_title"), btn_width, btn_height);
			startGameButton.addActionListener(actionListener);
			powerUpAmountChangerButtonMessage = props.getProperty("power_ups_amount_button_title");
			powerUpAmountChangerButton = setButtonSettings(powerUpAmountChangerButtonMessage + powerUpsAmountChosenLimit,
					btn_width, btn_height);
			powerUpAmountChangerButton.addActionListener(actionListener);
			fieldSizeButtonMessage = props.getProperty("field_size_button_title");
			fieldSizeButton = setButtonSettings(fieldSizeButtonMessage + mapSize, btn_width, btn_height);
			fieldSizeButton.addActionListener(actionListener);
			exitButton = setButtonSettings(props.getProperty("exit_button_title"), btn_width, btn_height);
			exitButton.addActionListener(actionListener);

			int width = Integer.parseInt(props.getProperty("game_window_width"));
			int height = Integer.parseInt(props.getProperty("info_window_height"))
					+ Integer.parseInt(props.getProperty("game_window_height"));
			background = new Color(Integer.parseInt(props.getProperty("menu_background_red")),
					Integer.parseInt(props.getProperty("menu_background_green")),
					Integer.parseInt(props.getProperty("menu_background_blue")));
			this.setBackground(background);

			setPreferredSize(new Dimension(width, height));
			setBackground(background);

			JPanel container = new JPanel();
			container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
			JLabel gameLabel = new JLabel(props.getProperty("game_title"));
			setComponentSize(gameLabel, btn_width, Integer.parseInt(props.getProperty("game_title_preferred_height")));
			container.add(gameLabel);
			container.add(Box.createRigidArea(new Dimension(0, 20)));
			container.add(startGameButton, BorderLayout.CENTER);
			container.add(Box.createRigidArea(new Dimension(0, 20)));
			container.add(powerUpAmountChangerButton);
			container.add(Box.createRigidArea(new Dimension(0, 20)));
			container.add(fieldSizeButton);
			container.add(Box.createRigidArea(new Dimension(0, 20)));
			container.add(exitButton, BorderLayout.CENTER);
			JLabel controlsExplanation = new JLabel(props.getProperty("controls_explanation_text"));
			container.add(Box.createRigidArea(new Dimension(0, 20)));
			setComponentSize(controlsExplanation, btn_width,
					Integer.parseInt(props.getProperty("controls_explanation_preferred_height")));
			container.add(controlsExplanation);
			add(container);

			container.setBackground(background);

			gameTitleColor = new Color(Integer.parseInt(props.getProperty("game_label_foreground_red")),
					Integer.parseInt(props.getProperty("game_label_foreground_green")),
					Integer.parseInt(props.getProperty("game_label_foreground_blue")));
			gameLabel.setForeground(gameTitleColor);
			gameLabel.setFont(new Font(props.getProperty("game_label_font_title"), Font.BOLD,
					Integer.parseInt(props.getProperty("game_label_font_size"))));

			controlsExplanationColor = new Color(Integer.parseInt(props.getProperty("controls_explanation_foreground_red")),
					Integer.parseInt(props.getProperty("controls_explanation_foreground_green")),
					Integer.parseInt(props.getProperty("controls_explanation_foreground_blue")));
			controlsExplanation.setForeground(gameTitleColor);
			controlsExplanation.setFont(new Font(props.getProperty("controls_explanation_font_title"), Font.ITALIC,
					Integer.parseInt(props.getProperty("controls_explanation_font_size"))));
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
		btn.setFont(new Font(prop.getProperty("btn_font_title"),
				Integer.parseInt(prop.getProperty("btn_font_type")),
				Integer.parseInt(prop.getProperty("btn_font_size"))));
	}

	private void setComponentSize(JComponent btn, int width, int height) throws IOException {
		btn.setPreferredSize(new Dimension(width, height));
		btn.setMinimumSize(new Dimension(width, height));
		btn.setMaximumSize(new Dimension(width, height));
	}

	private JButton setButtonSettings(String title, int width, int height) throws IOException {
		JButton btn = new JButton(title);
		setButtonLook(btn);
		setComponentSize(btn, width, height);
		return btn;
	}

	public void updateCurrentPowerUpsAmount() {
		if(powerUpsAmountChosenLimit >= powerUpsAmountMaxLimit) {
			powerUpsAmountChosenLimit = 1;
		}
		else ++powerUpsAmountChosenLimit;
		powerUpAmountChangerButton.setText(powerUpAmountChangerButtonMessage + powerUpsAmountChosenLimit);
	}
	
	public void updateMapSize() {
		switch(mapSize) {
			case SMALL -> mapSize = MapSize.MEDIUM;
			case MEDIUM -> mapSize = MapSize.LARGE;
			case LARGE -> mapSize = MapSize.SMALL;
			default -> throw new IllegalStateException("Unknown MapSize state.");
		}
		fieldSizeButton.setText(fieldSizeButtonMessage + mapSize);
	}
}