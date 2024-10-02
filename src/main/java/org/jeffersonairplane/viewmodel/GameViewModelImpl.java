package org.jeffersonairplane.viewmodel;

import org.jeffersonairplane.view.*;
import org.jeffersonairplane.model.*;

import java.awt.event.KeyEvent;
import java.util.*;

/**
 * ViewModel of MVVM pattern.
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
	private RectangleUpperLeftPoint blockToPixelCoordinateConversion(Coordinate blockCoordinate) {
		RectangleDimension blockDimension = view.getBlockDimension();
		int indentX = view.getIndentX();
		int indentY = view.getIndentY();
		return new RectangleUpperLeftPoint(indentX + (blockCoordinate.xCoord() - 1) * blockDimension.width(),
			indentY + (blockCoordinate.yCoord() - 1) * blockDimension.height());
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

	@Override
	public void runGame() {
		boolean gameOver = false;
        while(!gameOver) {
			try {
				if(!pause) {
					drawSnake();
					snakeMove();
					gameOver = checkSnakeCollisions();
				}
				Thread.sleep(200);
			}
			catch (InterruptedException ignored) {}
			catch (Exception e) {
				System.out.println(e.getMessage());
			}
        }
	}
}