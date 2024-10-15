package org.jeffersonairplane;

import org.jeffersonairplane.view.*;
import org.jeffersonairplane.model.*;
import org.jeffersonairplane.viewmodel.*;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.*;

public class Main {
	
	public static void setPowerUpTypesWeights(Properties props) {
		PowerUpTypes.APPLE.setCreationChance(Integer.parseInt(props.getProperty("pu_apple")));
	}
	
    public static void main(String[] args) {

		Properties props = null;
		try {
			props = PropertiesLoader.getProperties();
		}
		catch (IOException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException(e);
		}

		setPowerUpTypesWeights(props);
		
		var windowDimension = new RectangleDimension(
				Integer.parseInt(props.getProperty("game_window_width")),
				Integer.parseInt(props.getProperty("game_window_height")));
		var infoWindowDimension = new RectangleDimension(
				Integer.parseInt(props.getProperty("game_window_width")),
				Integer.parseInt(props.getProperty("info_window_height")));
		int xAxisBlocks = Integer.parseInt(props.getProperty("blocks_amount_x"));
		int yAxisBlocks = Integer.parseInt(props.getProperty("blocks_amount_y"));

		GameViewImpl view = null;
		try {
			Field backgroundColor = Class.forName("java.awt.Color").getField(props.getProperty("background_color"));
			Field snakeColor = Class.forName("java.awt.Color").getField(props.getProperty("snake_color"));
			Field infoTextColor = Class.forName("java.awt.Color").getField(props.getProperty("info_text_color"));
			var gameWindow = new GameWindow(windowDimension, xAxisBlocks, yAxisBlocks, (Color)backgroundColor.get(null),
					(Color)snakeColor.get(null));
			var infoWindow = new InfoWindow(infoWindowDimension, (Color)backgroundColor.get(null), (Color)infoTextColor.get(null));
			view = new GameViewImpl(props.getProperty("game_frame_title"), gameWindow, infoWindow,
					Integer.parseInt(props.getProperty("message_show_duration_frames")));
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new RuntimeException(e);
		}
        SnakeManager snakeManager = new SnakeManagerImpl();
        snakeManager.fillSnake(
				Integer.parseInt(props.getProperty("initial_snake_size")),
				new Coordinate(xAxisBlocks / 2, yAxisBlocks / 2),
                Direction.RIGHT,
				xAxisBlocks,
				yAxisBlocks);
		PowerUpManager powerUpManager = new PowerUpManagerImpl(
				Integer.parseInt(props.getProperty("pu_number_limit")),
				Integer.parseInt(props.getProperty("pu_creation_delay_min")),
				Integer.parseInt(props.getProperty("pu_creation_delay_max")));
        GameModel model = new GameModelImpl(
				new FieldDimension(xAxisBlocks, yAxisBlocks),
				snakeManager,
				Integer.parseInt(props.getProperty("snake_move_delay")),
				powerUpManager);

        GameViewModel gameViewModel = new GameViewModelImpl(view, model);

		int frameMilliseconds = Integer.parseInt(props.getProperty("frame_milliseconds"));

		var executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(() -> {
			boolean gameOver = gameViewModel.gameOneFrame();
			if(gameOver) executor.shutdown();
		}, 10, frameMilliseconds, TimeUnit.MILLISECONDS);
    }
}