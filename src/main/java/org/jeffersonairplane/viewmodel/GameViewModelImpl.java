package org.jeffersonairplane.viewmodel;

import org.jeffersonairplane.view.*;
import org.jeffersonairplane.model.*;

import java.awt.event.KeyEvent;
import java.util.*;

/**
 * ViewModel of MVVM pattern.
 */

public class GameViewModelImpl implements GameViewModel {
	
	private final int blockWidth;
    private final int blockHeight;
	
	private final GameFrameImpl view;
	private final GameModel model;
	
	/**
	 * All args constructor.
	 * @param view is a view part of program.
	 * @param model is a model part of program.
	 * @param blockWidth is a minimum field block width in pixels.
	 * @param blockHeight is a minimum field block height in pixels.
	 */
	public GameViewModelImpl(GameFrameImpl view, GameModel model, int blockWidth, int blockHeight) {
		this.blockWidth = blockWidth;
		this.blockHeight = blockHeight;
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
		Direction snakeDirection = switch(key.getKeyCode()) {
			case KeyEvent.VK_LEFT -> Direction.LEFT;
			case KeyEvent.VK_UP -> Direction.DOWN;
			case KeyEvent.VK_RIGHT -> Direction.RIGHT;
			case KeyEvent.VK_DOWN -> Direction.UP;
			default -> null;
		};
		model.changeSnakeDirection(snakeDirection);
	}
	
	/**
	 * Converts model coordinate (means particular rectangle on the grid) to its upper left corner point.
	 * @param blockCoordinate is a particular rectangle on the grid.
	 * @return {@link org.jeffersonairplane.view.RectangleUpperLeftPoint} triangle upper left corner point.
	 */
	private RectangleUpperLeftPoint blockToPixelCoordinateConversion(Coordinate blockCoordinate) {
		return new RectangleUpperLeftPoint((blockCoordinate.xCoord() - 1) * blockWidth, (blockCoordinate.yCoord() - 1) * blockHeight);
	}

	/**
	 * Converts snake blocks to points and sends List of {@link org.jeffersonairplane.view.RectangleUpperLeftPoint} to View.
	 */
	@Override
	public void drawSnake() {
		var snakeForPainting = new ArrayList<RectangleUpperLeftPoint>();
		for(Coordinate coordinate : model.getSnake().getSnakeBlocks()) {
			snakeForPainting.add(blockToPixelCoordinateConversion(coordinate));
		}
		view.setSnakeShape(snakeForPainting);
		view.repaintGameWindow();
	}
	/**
	 * Moves snake instance one step forward
	 */
	@Override
	public void snakeMove() {
		model.snakeMove();
	}

	/**
	 * Checks if {@link Snake} does not collide with borders or itself.
	 */
	@Override
	public boolean checkSnakeCollisions() {
		return model.checkCollisions();
	}


}