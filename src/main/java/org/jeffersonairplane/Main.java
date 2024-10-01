package org.jeffersonairplane;

import org.jeffersonairplane.view.*;
import org.jeffersonairplane.model.*;
import org.jeffersonairplane.viewmodel.*;

import java.awt.*;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {

        var gameSettings = new GameSettings(600, 600, 20, 20,
                "Snake Game", Color.DARK_GRAY, 5);

        var gameWindow = new GameWindow(gameSettings.getGameWindowWidth(), gameSettings.getGameWindowHeight(),
                gameSettings.getBlockWidth(), gameSettings.getBlockHeight(), gameSettings.getGameBackgroundColor());
        GameFrameImpl view = new GameFrameImpl(gameSettings.getGameFrameTitle(), gameWindow);

        int xAxisBlocks = gameSettings.getGameWindowWidth() / gameSettings.getBlockWidth();
        int yAxisBlocks = gameSettings.getGameWindowHeight() / gameSettings.getBlockHeight();
        SnakeManager snakeManager = new SnakeManagerImpl();
        snakeManager.fillSnake(gameSettings.getSnakeSize(), new Coordinate(xAxisBlocks / 2, yAxisBlocks / 2),
                Direction.RIGHT, xAxisBlocks, yAxisBlocks);

        GameModel model = new GameModelImpl(new FieldDimension(xAxisBlocks, yAxisBlocks),
                snakeManager, Logger.getLogger("Model Logger"));

        var gameViewModel = new GameViewModelImpl(view, model, gameSettings.getBlockWidth(), gameSettings.getBlockHeight());

        boolean gameOver = false;
        while(!gameOver) {
            try{
                gameViewModel.drawSnake();
                gameViewModel.snakeMove();
                gameOver = gameViewModel.checkSnakeCollisions();
                Thread.sleep(200);
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}