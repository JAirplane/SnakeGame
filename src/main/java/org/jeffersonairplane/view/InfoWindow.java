package org.jeffersonairplane.view;

import lombok.*;
import org.jeffersonairplane.PropertiesLoader;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
* UI element showing messages during gameplay.
*/

public class InfoWindow extends JPanel {
	@Getter
    private Color background;
	@Getter
    private Color textColor;

	private final JLabel infoLabel;
	@Getter
	private final GameMessages messages;
	@Getter
	private final Queue<String> messagesQueue;

	private final Logger logger = Logger.getLogger(getClass().getName());
	/**
	* Constructor.
	* @param infoWindowDimension contains width and height of element.
	* @param background elements background color.
	* @param textColor messages color.
	*/
	public InfoWindow(RectangleDimension infoWindowDimension, Color background, Color textColor, Font textFont) {
		this.background = background;
		this.textColor = textColor;
		this.setBackground(background);
		this.setPreferredSize(new Dimension(infoWindowDimension.width(), infoWindowDimension.height()));
		infoLabel = new JLabel();
		infoLabel.setFont(textFont);
		infoLabel.setForeground(textColor);
		this.add(infoLabel);
		
		messages = new GameMessages();
		messagesQueue = new LinkedList<>();
	}

	/**
	 * No args constructor.
	 * Receives all necessary data from {@link Properties}.
	 */
	public InfoWindow() {
		try {
			Properties props = PropertiesLoader.getProperties();
			infoLabel = new JLabel();
			this.setPreferredSize(
					new Dimension(Integer.parseInt(props.getProperty("game_window_width")),
							Integer.parseInt(props.getProperty("info_window_height"))));
			background = new Color(Integer.parseInt(props.getProperty("info_window_background_color_red")),
					Integer.parseInt(props.getProperty("info_window_background_color_green")),
					Integer.parseInt(props.getProperty("info_window_background_color_blue")));
			this.setBackground(background);
			textColor = new Color(Integer.parseInt(props.getProperty("info_text_color_red")),
					Integer.parseInt(props.getProperty("info_text_color_green")),
					Integer.parseInt(props.getProperty("info_text_color_blue")));

			infoLabel.setForeground(textColor);
			infoLabel.setFont(new Font(props.getProperty("gameplay_info_label_font"), Font.BOLD,
					Integer.parseInt(props.getProperty("gameplay_info_label_font_size"))));
			this.add(infoLabel);

			messages = new GameMessages();
			messagesQueue = new LinkedList<>();
		}
		catch(Exception e) {
			logger.log(Level.SEVERE, e.getMessage() + " " + Arrays.toString(e.getStackTrace()));
			throw new RuntimeException(e);
		}
	}
	/**
	* Sets new background color for all info window.
	* @param color to set.
	*/
	public void setBackgroundColor(Color color) {
		background = color;
		setBackground(background);
	}
	
	/**
	* Sets new text color for info messages shown.
	* @param color to set.
	*/
	public void setTextColor(Color color) {
		textColor = color;
		infoLabel.setForeground(textColor);
	}
	
	/**
	* Adds message to messages queue to show later in order.
	* @param message to show.
	*/
	public void addMessageToQueue(String message) {
		messagesQueue.offer(message);
	}
	
	/**
	* Gets message from queue.
	* @return message from queue.
	*/
	public String getMessageFromQueue() {
		return messagesQueue.poll();
	}
	
	/**
	* Gets score message.
	* @return score message.
	*/
	public String getScoreMessage() {
		return messages.getScoreMessage();
	}
	
	/**
	* Shows message on the info window.
	* @param message to show.
	*/
	public void showMessage(String message) {
		infoLabel.setText(message);
	}
	

}