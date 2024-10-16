package org.jeffersonairplane.view;

import java.awt.*;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.Field;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import lombok.*;
import org.jeffersonairplane.PropertiesLoader;

/**
* UI element - game playing field.
*/
public class GameWindow extends JPanel implements KeyListener, UserInputObservable {

	private final JLabel gameOverLabel;
    private final RectangleDimension windowDimension;
	@Getter
	private RectangleDimension blockDimension;
	@Getter
	private int indentX;
	@Getter
	private int indentY;
	@Getter @Setter
    private Color background;
	@Getter @Setter
	private Color snakeDefaultColor;
	@Getter
	private final Queue<Color> snakeAnimationColorQueue;
	@Setter
	private List<RectangleUpperLeftPoint> snakeShape;
	@Setter
	private List<RectangleUpperLeftPoint> powerUps;
	
	private final List<InputObserver> userInputObservers = new ArrayList<>();
	@Getter
	private final GameMessages messages;

	private final Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	* Constructor.
	* @param windowDimension contains width and height of element.
	* @param blocksAmountX is an amount of blocks X axis.
	* @param blocksAmountY is an amount of blocks Y axis.
	* @param background elements background color.
	* @param snakeDefaultColor snake color.
	*/
    public GameWindow(RectangleDimension windowDimension, int blocksAmountX, int blocksAmountY, Color background,
					  Color snakeDefaultColor, Color gameOverTextColor, Font gameOverLabelFont) {
		messages = new GameMessages();
		this.setLayout(new GridBagLayout());
        this.setPreferredSize(new Dimension(windowDimension.width(), windowDimension.height()));
        this.setBackground(background);
        this.windowDimension = windowDimension;
		this.background = background;
		this.snakeDefaultColor = snakeDefaultColor;
		snakeAnimationColorQueue = new LinkedList<>();
		gameOverLabel = new JLabel();
		gameOverLabel.setFont(gameOverLabelFont);
		gameOverLabel.setForeground(gameOverTextColor);
		gameOverLabel.setText(messages.getGameOverMessage());
		gameOverLabel.setVisible(false);
		this.add(gameOverLabel);
		setBlockDimension(blocksAmountX, blocksAmountY);
    }

	/**
	 * No args constructor.
	 * Receives all necessary data from {@link Properties}.
	 */
	public GameWindow() {
		try {
			messages = new GameMessages();
			this.setLayout(new GridBagLayout());
			Properties props = PropertiesLoader.getProperties();
			windowDimension = new RectangleDimension(Integer.parseInt(props.getProperty("game_window_width")),
					Integer.parseInt(props.getProperty("game_window_height")));
			this.setPreferredSize(new Dimension(windowDimension.width(), windowDimension.height()));
			Field backgroundColor = Class.forName("java.awt.Color").getField(props.getProperty("playing_field_background_color"));
			background = (Color)backgroundColor.get(null);
			this.setBackground(background);
			Field snakeColor = Class.forName("java.awt.Color").getField(props.getProperty("snake_color"));
			snakeDefaultColor = (Color)snakeColor.get(null);
			snakeAnimationColorQueue = new LinkedList<>();
			gameOverLabel = new JLabel();
			gameOverLabel.setFont(new Font(props.getProperty("game_over_label_font"), Font.BOLD,
					Integer.parseInt(props.getProperty("game_over_label_font_size"))));
			Field gameOverTextColor = Class.forName("java.awt.Color").getField(props.getProperty("game_over_text_color"));
			gameOverLabel.setForeground((Color)gameOverTextColor.get(null));
			gameOverLabel.setText(messages.getGameOverMessage());
			gameOverLabel.setVisible(false);
			this.add(gameOverLabel);

			setBlockDimension(Integer.parseInt(props.getProperty("blocks_amount_x")),
					Integer.parseInt(props.getProperty("blocks_amount_y")));


		}
		catch(Exception e) {
			logger.log(Level.SEVERE, e.getMessage() + " " + Arrays.toString(e.getStackTrace()));
			throw new RuntimeException(e);
		}
	}

	/**
	* {@link org.jeffersonairplane.view.UserInputObservable} implementation allows observers to listen user inputs.
	* @param obs observer to add.
	*/
	@Override
	public void registerInputObserver(InputObserver obs) {

        userInputObservers.add(obs);
	}
	
	/**
	* {@link org.jeffersonairplane.view.UserInputObservable} implementation allows observers to listen user inputs.
	* @param obs observer to remove.
	*/
	@Override
    public void removeInputObserver(InputObserver obs) {

        userInputObservers.remove(obs);
	}
	
	/**
	* {@link org.jeffersonairplane.view.UserInputObservable} implementation allows observers to listen user inputs.
	* @param key is a users key input.
	*/
	@Override
    public void notifyInputObservers(KeyEvent key) {
		for(InputObserver obs: userInputObservers) {
			obs.inputUpdate(key);
		}
	}

	/**
	 * Sets new text color for game over message.
	 * @param color to set.
	 */
	public void setTextColor(Color color) {
		gameOverLabel.setForeground(color);
	}

	/**
	 * Shows game over message.
	 */
	public void showGameOverMessage(boolean show) {
		gameOverLabel.setVisible(show);
	}

	/**
	* Calculates width and height of one playing field block.
	* @param blocksAmountX is an amount of blocks X axis.
	* @param blocksAmountY is an amount of blocks Y axis.
	*/
	public void setBlockDimension(int blocksAmountX, int blocksAmountY) {
		if(windowDimension == null) throw new NullPointerException();
		blockDimension = new RectangleDimension(windowDimension.width() / blocksAmountX, windowDimension.height() / blocksAmountY);
        indentX = (windowDimension.width() - blockDimension.width() * blocksAmountX) / 2;
		indentY = (windowDimension.height() - blockDimension.height() * blocksAmountY) / 2;
	}
	
	/**
	* Overrides JPanel method to paint game playing field.
	* @param graphics
	*/
    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        drawGrid(graphics);
		drawSnakeShape(graphics);
		drawPowerUps(graphics);
    }
	
	/**
	* Draws grid on the playing field to show all blocks.
	* Not realy necessary. Matter of taste.
	* @param graphics
	*/
	public void drawGrid(Graphics graphics) {
		for(int i = 0; i <= windowDimension.width() / blockDimension.width(); i++) {
            graphics.drawLine(indentX + i * blockDimension.width(), indentY,
					indentX + i * blockDimension.width(), windowDimension.height() - indentY);
        }
        for(int i = 0; i <= windowDimension.height() / blockDimension.height(); i++) {
            graphics.drawLine(indentX, indentY + i * blockDimension.height(),
					windowDimension.width() - indentX, indentY + i * blockDimension.height());
        }
	}
	
	/**
	* Draws snake on the playing field.
	* @param graphics
	*/
	public void drawSnakeShape(Graphics graphics) {
		if(snakeShape == null || snakeShape.isEmpty()) return;
		Color snakeColor = null;
		if(!snakeAnimationColorQueue.isEmpty()) {
			snakeColor = snakeAnimationColorQueue.poll();
		}
		else {
			snakeColor = snakeDefaultColor;
		}
        for(var point: snakeShape) {
            graphics.setColor(snakeColor);
            graphics.fillRect(point.x(), point.y(), blockDimension.width(), blockDimension.height());
        }
	}
	
	/**
	* Draws all power ups that exists on the playing field.
	* @param graphics
	*/
	public void drawPowerUps(Graphics graphics) {
		if(powerUps == null || powerUps.isEmpty()) return;
        for(var point: powerUps) {
            graphics.setColor(Color.RED);
            graphics.fillOval(point.x(), point.y(), blockDimension.width(), blockDimension.height());
        }
	}
	
	/**
	* Swing KeyListener implementation. 
	* @param e is an key typed.
	*/
    @Override
    public void keyTyped(KeyEvent e) {}
	
	/**
	* Swing KeyListener implementation.
	* Notifies listeners about user input event.
	* @param e is an key pressed.
	*/
    @Override
    public void keyPressed(KeyEvent e) {
        notifyInputObservers(e);
    }
	
	/**
	* Swing KeyListener implementation. 
	* @param e is an key released.
	*/
    @Override
    public void keyReleased(KeyEvent e) {}
}
