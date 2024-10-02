package org.jeffersonairplane.view;

import java.awt.event.*;
import java.util.*;
import java.util.logging.*;

public class GameViewImpl implements GameView {
	
	private final GameFrame frame;
	private final GameWindow window;
	
	private Logger logger = Logger.getLogger(getClass().getName());
	
	public GameViewImpl(String frameTitle, GameWindow window) {
		if(window == null) {
			String msg = "GameFrame creation failed. GameWindow arg is null.";
			logger.log(Level.SEVERE, msg);
			throw new NullPointerException(msg);
		}
		this.window = window;
		frame = new GameFrame(frameTitle, window);
	}
	
	@Override
	public void registerInputObserver(InputObserver obs) {
		window.registerInputObserver(obs);
	}
	
	@Override
	public void removeInputObserver(InputObserver obs) {
		window.removeInputObserver(obs);
	}
	
	@Override
	public void notifyInputObservers(KeyEvent key) {
		window.notifyInputObservers(key);
	}
	
	@Override
	public void setSnakeShape(List<RectangleUpperLeftPoint> snakeShape) {
		window.setSnakeShape(snakeShape);
	}
	
	@Override
	public int getIndentX() {
		return window.getIndentX();
	}
	
	@Override
	public int getIndentY() {
		return window.getIndentY();
	}

	@Override
	public RectangleDimension getBlockDimension() {
		return window.getBlockDimension();
	}
	
	@Override
	public void repaintGameWindow() {
		frame.repaintGameWindow();
	}
}