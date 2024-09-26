package org.jeffersonairplane;

import java.awt.*;

/**
 * All game settings stored here.
 */
class GameSettings {
    /**
     * All game settings stored here.
     */
    private final int gameWindowHeight;
    private final int gameWindowWidth;
    private final int blockWidth;
    private final int blockHeight;
    private final String gameFrameTitle;
    private final Color background;
    private final int snakeSize;

    GameSettings(int windowWidth, int windowHeight, int blockWidth, int blockHeight, String gameFrameTitle, Color background,
                 int snakeSize) {
        gameWindowWidth = windowWidth;
        gameWindowHeight = windowHeight;
        this.blockWidth = blockWidth;
        this.blockHeight = blockHeight;
        this.gameFrameTitle = gameFrameTitle;
        this.background = background;
        this.snakeSize = snakeSize;
    }

    public int getGameWindowWidth() {

        return gameWindowWidth;
    }

    public int getGameWindowHeight() {

        return gameWindowHeight;
    }

    public int getBlockWidth() {

        return blockWidth;
    }

    public int getBlockHeight() {

        return blockHeight;
    }

    public String getGameFrameTitle() {

        return gameFrameTitle;
    }

    public Color getGameBackgroundColor() {

        return background;
    }

    public int getSnakeSize() {

        return snakeSize;
    }
}
