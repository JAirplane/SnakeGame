package org.jeffersonairplane.view;

import java.util.*;

/**
 * Stores messages shown when power up taken
 */
public class PowerUpMessages {
    private final List<String> messages = new ArrayList<>();

    /**
     * Adds default messages for every power up type in the game.
     */
    public PowerUpMessages() {
        messages.add("Snake grows!");
    }

    /**
     * Rewrites default message for particular power up index.
     * 0 is Apple.
     */
    public void setMessage(int index, String message) {
        messages.set(index, message);
    }

    /**
     * Gets message for particular power up index.
     * 0 is Apple.
     */
    public String getMessage(int index) {
        return messages.get(index);
    }
}
