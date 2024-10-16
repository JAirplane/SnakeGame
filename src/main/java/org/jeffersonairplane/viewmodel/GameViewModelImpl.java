package org.jeffersonairplane.viewmodel;

import org.jeffersonairplane.PropertiesLoader;
import org.jeffersonairplane.view.*;
import org.jeffersonairplane.model.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ViewModel of MVVM pattern.
 * Manages view and model part.
 */

public class GameViewModelImpl implements GameViewModel {
	
	private final GameView view;
	private final GameModel model;

	private final int frameMilliseconds;
	private boolean pause;
	private boolean gameOver;

	private final Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * Constructor.
	 * @param view is a view part of program.
	 * @param model is a model part of program.
	 */
	public GameViewModelImpl(GameView view, GameModel model) {
		try {
			this.view = view;
			this.model = model;
			frameMilliseconds = Integer.parseInt(PropertiesLoader.getProperties().getProperty("frame_milliseconds"));
			view.getGameWindow().registerInputObserver(this);
			model.registerPowerUpTakenObserver(this);
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage() + " " + Arrays.toString(e.getStackTrace()));
			throw new RuntimeException(e);
		}
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
            case KeyEvent.VK_R -> {
                if(gameOver) rerunAfterGameOver();
            }
        };
	}
	
	/**
	 * Converts model coordinate (means particular rectangle on the grid) to its upper left corner point.
	 * @param blockCoordinate is a particular rectangle on the grid.
	 * @return {@link org.jeffersonairplane.view.RectangleUpperLeftPoint} triangle upper left corner point.
	 */
	public RectangleUpperLeftPoint blockToPixelCoordinateConversion(Coordinate blockCoordinate) {
		RectangleDimension blockDimension = view.getGameWindow().getBlockDimension();
		int indentX = view.getGameWindow().getIndentX();
		int indentY = view.getGameWindow().getIndentY();
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
		view.getGameWindow().setSnakeShape(snakeForPainting);
	}
	
	/**
	 * Converts power up blocks to points and sends List of {@link org.jeffersonairplane.view.RectangleUpperLeftPoint} to View.
	 */
	public void setPowerUpsDataForPainting() {
		var powerUpsForPainting = new ArrayList<RectangleUpperLeftPoint>();
		for(var powerUp : model.getPowerUps()) {
			powerUpsForPainting.add(blockToPixelCoordinateConversion(powerUp.getPoint()));
		}
		view.getGameWindow().setPowerUps(powerUpsForPainting);
	}
	
	/**
	 * Redraws playing field, snake and power ups.
	 */
	@Override
	public void drawGame() {
		setSnakeDataForPainting();
		setPowerUpsDataForPainting();
		view.repaintGameWindow();
		view.repaintInfoWindow();
	}
	/**
	 * Represents one frame game iteration: logic + painting.
	 */
	@Override
	public boolean gameOneFrame() {
		try {
			if(!pause) {
				gameOver = model.oneFrameGameAction();
				if(!gameOver) {
					drawGame();
				}
				else {
					view.getGameWindow().showGameOverMessage(true);
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

	/**
	 * Runs gameplay process.
	 */
	@Override
	public void runGameplay() {
		var executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(() -> {
			boolean gameOver = gameOneFrame();
			if(gameOver) executor.shutdown();
		}, 10, frameMilliseconds, TimeUnit.MILLISECONDS);
	}

	/**
	 * Reruns gameplay process after game over.
	 */
	@Override
	public void rerunAfterGameOver() {
		gameOver = false;
		view.getGameWindow().showGameOverMessage(false);
		model.setScore(0);
		view.setScore(0);
		model.setFramesCounter(0);
		model.getPowerUpManager().getPowerUps().clear();
		model.getPowerUpManager().getPowerUpCreationCountdowns().clear();
		model.getPowerUpManager().setWaitingAndExistingPowerUpsNumber(0);
		try {
			Properties props = PropertiesLoader.getProperties();
			int xAxisBlocks = Integer.parseInt(props.getProperty("blocks_amount_x"));
			int yAxisBlocks = Integer.parseInt(props.getProperty("blocks_amount_y"));
			model.getSnakeManager().fillSnake(
					Integer.parseInt(props.getProperty("initial_snake_size")),
					new Coordinate(xAxisBlocks / 2, yAxisBlocks / 2),
					Direction.RIGHT,
					xAxisBlocks,
					yAxisBlocks);
			runGameplay();
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage() + " " + Arrays.toString(e.getStackTrace()));
			throw new RuntimeException(e);
		}
	}
	/**
	 * Observes if snake eaten power up via  {@link PowerUpTakenObservable}.
	 *
	 * @param powerUp is a power up eaten.
	 */
	@Override
	public void powerUpTakenUpdate(PowerUp powerUp) {
		view.getInfoWindow().addMessageToQueue(getPowerUpMessage(powerUp));
		view.setScore(model.getScore());
		view.setSnakeAnimation(getPowerUpAnimation(powerUp));
	}

	/**
	 * Every power up has its own message to show.
	 * @param powerUp is a particular type of power up getting message for.
	 * @return message.
	 */
	public String getPowerUpMessage(PowerUp powerUp) {
		if(powerUp instanceof Apple) {
			return view.getInfoWindow().getMessages().getPowerUpMessage(0);
		}
		return "";
	}

	/**
	 * Every power up has its own snake animation to show.
	 * @param powerUp is a particular type of power up getting animation for.
	 * @return collection of colors for snake.
	 */
	public List<Color> getPowerUpAnimation(PowerUp powerUp) {
		if(powerUp instanceof Apple) {
			return Animations.getAppleTakenSnakeAnimation();
		}
		return new ArrayList<>();
	}
}