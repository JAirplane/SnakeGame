package org.jeffersonairplane.viewmodel;

import org.jeffersonairplane.view.*;
import org.jeffersonairplane.model.*;

import java.awt.event.KeyEvent;
import java.util.*;

/**
 * ViewModel of MVVM pattern.
 * Manages view and model part.
 */

public class GameViewModelImpl implements GameViewModel {
	
	private final GameView view;
	private final GameModel model;

	private boolean pause;
	
	/**
	 * All args constructor.
	 * @param view is a view part of program.
	 * @param model is a model part of program.
	 */
	public GameViewModelImpl(GameView view, GameModel model) {
		this.view = view;
		this.model = model;

		view.registerInputObserver(this);
	}
	
	/**
	 * {@link org.jeffersonairplane.view.InputObserver} implementation.
	 * Changes snake direction according to key pressed by user.
	 * @param key pressed.
	 */
	@Override
	public void inputUpdate(KeyEvent key) {
		switch(key.getKeyCode()) {
			case KeyEvent.VK_LEFT -> model.changeSnakeDirection(Direction.LEFT);
			case KeyEvent.VK_UP -> model.changeSnakeDirection(Direction.DOWN);
			case KeyEvent.VK_RIGHT -> model.changeSnakeDirection(Direction.RIGHT);
			case KeyEvent.VK_DOWN -> model.changeSnakeDirection(Direction.UP);
			case KeyEvent.VK_SPACE -> pause = !pause;
		};
	}
	
	/**
	 * Converts model coordinate (means particular rectangle on the grid) to its upper left corner point.
	 * @param blockCoordinate is a particular rectangle on the grid.
	 * @return {@link org.jeffersonairplane.view.RectangleUpperLeftPoint} triangle upper left corner point.
	 */
	public RectangleUpperLeftPoint blockToPixelCoordinateConversion(Coordinate blockCoordinate) {
		RectangleDimension blockDimension = view.getBlockDimension();
		int indentX = view.getIndentX();
		int indentY = view.getIndentY();
		return new RectangleUpperLeftPoint(indentX + (blockCoordinate.xCoord() - 1) * blockDimension.width(),
			indentY + (blockCoordinate.yCoord() - 1) * blockDimension.height());
	}

	/**
	 * Converts snake blocks to points and sends List of {@link org.jeffersonairplane.view.RectangleUpperLeftPoint} to View.
	 */
	public void setSnakeDataForPainting() {
		var snakeForPainting = new ArrayList<RectangleUpperLeftPoint>();
		for(Coordinate coordinate : model.getSnake().getSnakeBlocks()) {
			snakeForPainting.add(blockToPixelCoordinateConversion(coordinate));
		}
		view.setSnakeShape(snakeForPainting);
	}
	
	/**
	 * Converts power up blocks to points and sends List of {@link org.jeffersonairplane.view.RectangleUpperLeftPoint} to View.
	 */
	public void setPowerUpsDataForPainting() {
		var powerUpsForPainting = new ArrayList<RectangleUpperLeftPoint>();
		for(var powerUp : model.getPowerUps()) {
			powerUpsForPainting.add(blockToPixelCoordinateConversion(powerUp.getPoint()));
		}
		view.setPowerUps(powerUpsForPainting);
	}
	
	/**
	 * Redraws playingfield, snake and power ups.
	 */
	@Override
	public void drawGame() {
		setSnakeDataForPainting();
		setPowerUpsDataForPainting();
		view.repaintGameWindow();
	}
	/**
	 * Represents one frame game iteration: logic + painting.
	 */
	@Override
	public boolean gameOneFrame() {
		boolean gameOver = false;
		try {
			if(!pause) {
				gameOver = model.oneFrameGameAction();
				if(!gameOver) {
					drawGame();
				}
				return gameOver;
			}
			return false;
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			throw new RuntimeException();
		}
    }
}