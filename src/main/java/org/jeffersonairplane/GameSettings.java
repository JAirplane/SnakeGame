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
    private final int blocksAmountXAxis;
    private final int blocksAmountYAxis;
    private final String gameFrameTitle;
    private final Color background;
    private final int snakeSize;

    GameSettings(int windowWidth, int windowHeight, int blocksAmountXAxis, int blocksAmountYAxis, String gameFrameTitle, Color background,
                 int snakeSize) {
        gameWindowWidth = windowWidth;
        gameWindowHeight = windowHeight;
        this.blocksAmountXAxis = blocksAmountXAxis;
        this.blocksAmountYAxis = blocksAmountYAxis;
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

    public int getBlocksAmountXAxis() {

        return blocksAmountXAxis;
    }

    public int getBlocksAmountYAxis() {

        return blocksAmountYAxis;
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
