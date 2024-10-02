package org.jeffersonairplane;

import org.jeffersonairplane.view.*;
import org.jeffersonairplane.model.*;
import org.jeffersonairplane.viewmodel.*;

import java.awt.*;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {

        var gameSettings = new GameSettings(619, 619, 20, 20, "Snake Game", Color.DARK_GRAY, 5);
		var windowDimension = new RectangleDimension(gameSettings.getGameWindowWidth(), gameSettings.getGameWindowHeight());
		int xAxisBlocks = gameSettings.getBlocksAmountXAxis();
		int yAxisBlocks = gameSettings.getBlocksAmountYAxis();
		
        var gameWindow = new GameWindow(windowDimension, xAxisBlocks, yAxisBlocks, gameSettings.getGameBackgroundColor());
		
		var view = new GameViewImpl(gameSettings.getGameFrameTitle(), gameWindow);

        SnakeManager snakeManager = new SnakeManagerImpl();
        snakeManager.fillSnake(gameSettings.getSnakeSize(), new Coordinate(xAxisBlocks / 2, yAxisBlocks / 2),
                Direction.RIGHT, xAxisBlocks, yAxisBlocks);

        GameModel model = new GameModelImpl(new FieldDimension(xAxisBlocks, yAxisBlocks), snakeManager);

        var gameViewModel = new GameViewModelImpl(view, model);

        try (var executorService = Executors.newSingleThreadExecutor()) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    gameViewModel.runGame();
                }
            });
        }
    }
}