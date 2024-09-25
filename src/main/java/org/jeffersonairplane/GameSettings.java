package org.jeffersonairplane;

import java.awt.*;

class GameSettings {
    private final int gameWindowHeight;
    private final int gameWindowWidth;
    private final int blockWidth;
    private final int blockHeight;
    private final String gameFrameTitle;
    private final Color background;

    GameSettings(int width, int height, int blockWidth, int blockHeight, String gameFrameTitle, Color background){
        gameWindowWidth = width;
        gameWindowHeight = height;
        this.blockWidth = blockWidth;
        this.blockHeight = blockHeight;
        this.gameFrameTitle = gameFrameTitle;
        this.background = background;
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
}
