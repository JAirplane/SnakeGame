package org.jeffersonairplane.view;

import lombok.*;
import org.jeffersonairplane.PropertiesLoader;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.*;

/**
* View part of MVVM pattern.
*/
public class GameViewImpl implements GameView {
	
	private final GameFrame frame;
	@Getter
	private final GameWindow gameWindow;
	@Getter
	private final InfoWindow infoWindow;
	@Getter
	private final MenuWindow menuWindow;
	@Getter @Setter
	private long score;
	@Getter @Setter
	private int messageShowFramesDuration;
	@Getter @Setter
	private int messageCountdown;

	private final Logger logger = Logger.getLogger(getClass().getName());
	
	public GameViewImpl(String frameTitle, GameWindow gameWindow, InfoWindow infoWindow,
						MenuWindow menuWindow, int messageShowFramesDuration) {
        if(gameWindow == null || infoWindow == null) {
			String msg = "GameFrame creation failed.";
			logger.log(Level.SEVERE, msg);
			throw new NullPointerException(msg);
		}
		this.gameWindow = gameWindow;
		this.infoWindow = infoWindow;
		this.menuWindow = menuWindow;
		this.messageShowFramesDuration = messageShowFramesDuration;
		frame = new GameFrame(frameTitle, gameWindow, infoWindow, menuWindow);
	}

	public GameViewImpl() {
		try {
			gameWindow = new GameWindow();
			infoWindow = new InfoWindow();
			menuWindow = new MenuWindow();
			Properties props = PropertiesLoader.getProperties();
			messageShowFramesDuration = Integer.parseInt(props.getProperty("message_show_duration_frames"));
			frame = new GameFrame(props.getProperty("game_frame_title"), gameWindow, infoWindow, menuWindow);
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage() + " " + Arrays.toString(e.getStackTrace()));
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setSnakeAnimation(Collection<Color> colors) {
		var animationSet = gameWindow.getSnakeAnimationColorQueue();
		for(Color color: colors) {
			animationSet.offer(color);
		}
	}
	
	@Override
	public void repaintGameWindow() {
		gameWindow.repaint();
	}
	
	@Override
	public void repaintInfoWindow() {
		if(messageCountdown == 0) {
			String queueMessage = infoWindow.getMessageFromQueue();
			if(queueMessage != null && !queueMessage.isEmpty()) {
				infoWindow.showMessage(queueMessage);
				messageCountdown = messageShowFramesDuration;
			}
			else {
				infoWindow.showMessage(infoWindow.getScoreMessage() + score);
			}
		}
		else {
			--messageCountdown;
		}
	}

	@Override
	public void setPowerUpColors() {
		try {
			Properties props = PropertiesLoader.getProperties();
			Field appleColor = Class.forName("java.awt.Color")
					.getField(props.getProperty("apple_color"));
			PowerUpTypesView.APPLE.setColor((Color)appleColor.get(null));
			Field tailCutterColor = Class.forName("java.awt.Color")
					.getField(props.getProperty("tail_cutter_color"));
			PowerUpTypesView.TAILCUTTER.setColor((Color)tailCutterColor.get(null));
			logger.log(Level.FINE, "Creation chances set.");
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage() + " " + Arrays.toString(e.getStackTrace()));
		}
	}

	@Override
	public void resetState() {
		score = 0;
		messageCountdown = 0;
		gameWindow.resetState();
	}
}