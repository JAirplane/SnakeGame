package org.jeffersonairplane.view;

import java.awt.event.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.*;

public class GameViewImpl implements GameView, Runnable {
	
	private final GameFrame frame;
	private final GameWindow window;

	private final int frameInMilliseconds;
	private boolean keepFrameCounting;

	BlockingQueue<Integer> frameStorage;
	private final Logger logger = Logger.getLogger(getClass().getName());
	
	public GameViewImpl(String frameTitle, GameWindow window, int frameInMilliseconds, BlockingQueue<Integer> frameStorage) {
        this.frameInMilliseconds = frameInMilliseconds;
        this.frameStorage = frameStorage;
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
	public void setKeepFrameCounting(boolean countFrames) {
		keepFrameCounting = countFrames;
	}

	@Override
	public void repaintGameWindow() {
		frame.repaintGameWindow();
	}

	@Override
	public void runFrameCounter() {
		while(keepFrameCounting) {
			try {
				Thread.sleep(frameInMilliseconds);
				frameStorage.put(1);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * Runs this operation.
	 */
	@Override
	public void run() {
		while(keepFrameCounting) {
			try {
				Thread.sleep(frameInMilliseconds);
				frameStorage.put(1);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}
}