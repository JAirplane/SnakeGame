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

	private final Animations animations;
	private final int frameMilliseconds;
	private boolean pause;
	private boolean gameOver;

	private final Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * Constructor.
	 * @param view is a view part of program.
	 * @param model is a model part of program.
	 */
	public GameViewModelImpl(GameView view, GameModel model, Animations animations) {
		try {
			this.animations = animations;
			this.view = view;
			this.view.setSettingsSetter(this::setSettings);
			this.view.setGameRunner(this::runGameplay);
			this.view.getFrame().setMovement(model::changeSnakeDirection);
			this.view.getFrame().setRerun(this::rerunAfterGameOver);
			this.view.getFrame().setTogglePause(this::togglePause);
			this.view.getFrame().setToMenu(this.view.getGameWindow()::showGameOverMessage);
			this.view.getFrame().setGameplayInputs();
			this.model = model;
			frameMilliseconds = Integer.parseInt(PropertiesLoader.getProperties().getProperty("frame_milliseconds"));
			model.registerPowerUpTakenObserver(this);
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage() + " " + Arrays.toString(e.getStackTrace()));
			throw new RuntimeException(e);
		}
	}

	/**
	 * Switches pause in the game.
	 */
	@Override
	public void togglePause() {
		pause = !pause;
	}

	/**
	 * Converts model coordinate (means particular rectangle on the grid) to its upper left corner point.
	 * @param blockCoordinate is a particular rectangle on the grid.
	 * @return {@link org.jeffersonairplane.view.RectangleUpperLeftPoint} rectangle upper left corner point.
	 */
	public RectangleUpperLeftPoint blockToPixelCoordinateConversion(Coordinate blockCoordinate) {
		RectangleDimension blockDimension = view.getGameWindow().getBlockDimension();
		int indentX = view.getGameWindow().getIndentX();
		int indentY = view.getGameWindow().getIndentY();
		return new RectangleUpperLeftPoint(indentX + (blockCoordinate.xCoord() - 1) * blockDimension.width(),
			indentY + (blockCoordinate.yCoord() - 1) * blockDimension.height());
	}
	
	/**
	 * Get appropriate enum type for particular power up, so view knows what type to paint.
	 * @param powerUp for conversion.
	 * @return {@link org.jeffersonairplane.view.PowerUpTypesView} for painting.
	 */
	public PowerUpTypesView powerUpTypeConversion(PowerUp powerUp) {
		if(powerUp instanceof Apple) return PowerUpTypesView.APPLE;
		else if(powerUp instanceof TailCutter) return PowerUpTypesView.TAILCUTTER;
		else throw new NullPointerException("Unknown power up.");
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
	 * Creates map of types as a keys and collection of {@link org.jeffersonairplane.view.RectangleUpperLeftPoint} as values for every exsting power up.
	 * Sends map ti View for painting.
	 */
	public void setPowerUpsDataForPainting() {
		var powerUpsForPainting = new HashMap<PowerUpTypesView, List<RectangleUpperLeftPoint>>();
		for(var powerUp : model.getPowerUps()) {
			PowerUpTypesView type = powerUpTypeConversion(powerUp);
			if(type == null) throw new NullPointerException("Power up type is null after conversion.");
			if(!powerUpsForPainting.containsKey(type)) {
				powerUpsForPainting.put(type, new ArrayList<>());
			}
			var collection = powerUpsForPainting.get(type);
			collection.add(blockToPixelCoordinateConversion(powerUp.getPoint()));
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
					view.getGameWindow().getPowerUps().clear();
					view.getGameWindow().getSnakeAnimationColorQueue().clear();
					view.repaintGameWindow();
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
	 * Sets user settings from menu.
	 */
	public void setSettings(ChosenSettingsDTO settings) {
		model.getPowerUpManager().setPowerUpNumberLimit(settings.powerUpsLimit());
		model.setDimension(new FieldDimension(settings.xAxisBlocksAmount(), settings.yAxisBlocksAmount()));
		view.getGameWindow().setBlockDimension(settings.xAxisBlocksAmount(), settings.yAxisBlocksAmount());
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
		if(gameOver) {
			gameOver = false;
			view.getGameWindow().showGameOverMessage(false);
			model.resetState();
			view.setScore(0);
			view.getInfoWindow().getMessagesQueue().clear();
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
		else if (powerUp instanceof TailCutter) {
			return view.getInfoWindow().getMessages().getPowerUpMessage(1);
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
			return animations.getAppleTakenSnakeAnimation();
		}
		else if (powerUp instanceof TailCutter) {
			return animations.getTailCutterTakenSnakeAnimation();
		}
		return new ArrayList<>();
	}
}