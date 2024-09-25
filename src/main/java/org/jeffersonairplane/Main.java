package org.jeffersonairplane;

import org.jeffersonairplane.view.*;

import java.awt.*;

public class Main {
    public static void main(String[] args) {

        var gameSettings = new GameSettings(600, 600, 20, 20, "Snake Game", Color.DARK_GRAY);
        var gameFrame = new GameFrame(gameSettings.getGameFrameTitle(), gameSettings.getGameWindowWidth(), gameSettings.getGameWindowHeight(),
                gameSettings.getBlockWidth(), gameSettings.getBlockHeight(), gameSettings.getGameBackgroundColor());
        var GameView = new GameView(gameFrame);
    }
}