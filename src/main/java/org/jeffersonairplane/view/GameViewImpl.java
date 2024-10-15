package org.jeffersonairplane.view;

import lombok.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.logging.*;

public class GameViewImpl implements GameView {
	
	private final GameFrame frame;
	private final GameWindow gameWindow;
	private final InfoWindow infoWindow;

	@Getter
	private final PowerUpMessages powerUpMessages;
	@Getter @Setter
	private long score;
	@Setter
	private String scoreMessage = "Current score: ";
	@Getter @Setter
	private int messageShowFramesDuration;
	@Getter @Setter
	private int messageCountdown;
	@Getter
	private final Queue<String> messagesQueue;

	private final Logger logger = Logger.getLogger(getClass().getName());
	
	public GameViewImpl(String frameTitle, GameWindow gameWindow, InfoWindow infoWindow, int messageShowFramesDuration) {
        if(gameWindow == null) {
			String msg = "GameFrame creation failed. GameWindow arg is null.";
			logger.log(Level.SEVERE, msg);
			throw new NullPointerException(msg);
		}
		this.gameWindow = gameWindow;
		this.infoWindow = infoWindow;
		this.messageShowFramesDuration = messageShowFramesDuration;
		frame = new GameFrame(frameTitle, gameWindow, infoWindow);
		powerUpMessages = new PowerUpMessages();
		messagesQueue = new LinkedList<>();
	}
	
	@Override
	public void registerInputObserver(InputObserver obs) {
		gameWindow.registerInputObserver(obs);
	}
	
	@Override
	public void removeInputObserver(InputObserver obs) {
		gameWindow.removeInputObserver(obs);
	}
	
	@Override
	public void notifyInputObservers(KeyEvent key) {
		gameWindow.notifyInputObservers(key);
	}
	
	@Override
	public void setSnakeShape(List<RectangleUpperLeftPoint> snakeShape) {
		gameWindow.setSnakeShape(snakeShape);
	}

	@Override
	public void setPowerUps(List<RectangleUpperLeftPoint> powerUps) {
		gameWindow.setPowerUps(powerUps);
	}
	
	@Override
	public int getIndentX() {
		return gameWindow.getIndentX();
	}
	
	@Override
	public int getIndentY() {
		return gameWindow.getIndentY();
	}

	@Override
	public RectangleDimension getBlockDimension() {
		return gameWindow.getBlockDimension();
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
			if(!messagesQueue.isEmpty()) {
				infoWindow.getInfoLabel().setText(messagesQueue.poll());
				messageCountdown = messageShowFramesDuration;
			}
			else {
				infoWindow.getInfoLabel().setText(scoreMessage + score);
			}
		}
		else {
			--messageCountdown;
		}
	}

	@Override
	public void addMessageToShow(String message) {
		messagesQueue.offer(message);
	}
}