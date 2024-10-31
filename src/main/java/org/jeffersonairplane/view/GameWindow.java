package org.jeffersonairplane.view;

import java.awt.*;
import javax.swing.*;
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
public class GameWindow extends JPanel {

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
	@Getter @Setter
	private List<RectangleUpperLeftPoint> snakeShape;
	@Getter @Setter
	private Map<PowerUpTypesView, List<RectangleUpperLeftPoint>> powerUps;

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
			background = new Color(Integer.parseInt(props.getProperty("playing_field_background_color_red")),
					Integer.parseInt(props.getProperty("playing_field_background_color_green")),
					Integer.parseInt(props.getProperty("playing_field_background_color_blue")));
			this.setBackground(background);
			snakeDefaultColor = new Color(
					Integer.parseInt(props.getProperty("snake_color_red")),
					Integer.parseInt(props.getProperty("snake_color_green")),
					Integer.parseInt(props.getProperty("snake_color_blue")));
			snakeAnimationColorQueue = new LinkedList<>();
			gameOverLabel = new JLabel();
			gameOverLabel.setFont(new Font(props.getProperty("game_over_label_font"), Font.BOLD,
					Integer.parseInt(props.getProperty("game_over_label_font_size"))));
			gameOverLabel.setForeground(new Color(
					Integer.parseInt(props.getProperty("game_over_text_color_red")),
					Integer.parseInt(props.getProperty("game_over_text_color_green")),
					Integer.parseInt(props.getProperty("game_over_text_color_blue"))));
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
	* @param graphics is a swing class.
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
	* Not really necessary. Matter of taste.
	* @param graphics is a swing class.
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
	* @param graphics is a swing class.
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
	 * Draws one particular power up on the playing field.
	 * @param graphics is a swing class.
	 * @param type is a type of power up.
	 * @param point is an upper left angle of plying field rectangle.
	 */
	public void drawPowerUp(Graphics graphics, PowerUpTypesView type, RectangleUpperLeftPoint point) {
		if(type == null) throw new NullPointerException("Type is null!");
		graphics.setColor(type.getColor());
		if(type == PowerUpTypesView.APPLE) {
			graphics.fillOval(point.x(), point.y(), blockDimension.width(), blockDimension.height());
		}
		else if(type == PowerUpTypesView.TAILCUTTER) {
			graphics.fillRect(point.x(), point.y(), blockDimension.width(), blockDimension.height());
		}
	}
	/**
	* Draws all power ups that exists on the playing field.
	* @param graphics is a swing class.
	*/
	public void drawPowerUps(Graphics graphics) {
		if(powerUps == null || powerUps.isEmpty()) return;
        for(var entry: powerUps.entrySet()) {
			for(var point: entry.getValue()) {
				drawPowerUp(graphics, entry.getKey(), point);
			}
        }
	}
	
	/**
	* Erase all animations, snake shape and power ups.
	*/
	public void resetState() {
		snakeAnimationColorQueue.clear();
		snakeShape.clear();
		powerUps.clear();
	}

}
