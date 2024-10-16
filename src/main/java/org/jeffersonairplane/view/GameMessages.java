package org.jeffersonairplane.view;

import lombok.*;
import org.jeffersonairplane.PropertiesLoader;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Stores messages shown when power up taken
 */
public class GameMessages {
    private final List<String> messages = new ArrayList<>();
	@Getter @Setter
	private String scoreMessage;
    @Getter @Setter
    private String gameOverMessage;

    private final Logger logger = Logger.getLogger(getClass().getName());
    /**
     * Adds default messages for every power up type in the game.
     */
    public GameMessages() {
        try {
            Properties props = PropertiesLoader.getProperties();
            scoreMessage = props.getProperty("score_message");
            messages.add(props.getProperty("apple_message"));
            gameOverMessage = props.getProperty("game_over_message");
        }
        catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage() + " " + Arrays.toString(e.getStackTrace()));
            throw new RuntimeException(e);
        }

    }

    /**
     * Rewrites default message for particular power up index.
     * 0 index is Apple.
     */
    public void setPowerUpMessage(int index, String message) {
        messages.set(index, message);
    }

    /**
     * Gets message for particular power up index.
     * 0 is Apple.
     */
    public String getPowerUpMessage(int index) {
        return messages.get(index);
    }
}
