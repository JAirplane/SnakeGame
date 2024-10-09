package org.jeffersonairplane;

import org.jeffersonairplane.view.*;
import org.jeffersonairplane.model.*;
import org.jeffersonairplane.viewmodel.*;

import java.awt.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.*;

public class Main {
	
	public static Properties getProperties() {
		try {
			var classLoader = Thread.currentThread().getContextClassLoader().getResource("");
			if(classLoader == null) throw new NullPointerException("Class loader is null");
            String rootPath = classLoader.getPath();
			String appConfigPath = rootPath + "application.properties";
			Properties props = new Properties();
			props.load(new FileInputStream(appConfigPath));
			return props;
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			throw new RuntimeException();
		}
    }
	
	public static void setPowerUpTypesWeights(Properties props) {
		PowerUpTypes.APPLE.setCreationChance(Integer.parseInt(props.getProperty("pu_apple")));
	}
	
    public static void main(String[] args) {

		Properties props = getProperties();
		setPowerUpTypesWeights(props);
		
		var windowDimension = new RectangleDimension(
				Integer.parseInt(props.getProperty("window_width")),
				Integer.parseInt(props.getProperty("window_height")));
		int xAxisBlocks = Integer.parseInt(props.getProperty("blocks_amount_x"));
		int yAxisBlocks = Integer.parseInt(props.getProperty("blocks_amount_y"));

		GameViewImpl view = null;
		try {
			Field field = Class.forName("java.awt.Color").getField(props.getProperty("background_color"));
			var gameWindow = new GameWindow(windowDimension, xAxisBlocks, yAxisBlocks, (Color)field.get(null));
			view = new GameViewImpl(props.getProperty("game_frame_title"), gameWindow);
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

        var gameViewModel = new GameViewModelImpl(view, model);

		int frameMilliseconds = Integer.parseInt(props.getProperty("frame_milliseconds"));

		try(var executor = Executors.newScheduledThreadPool(1)) {
			executor.scheduleAtFixedRate(gameViewModel::gameOneFrame, 10, frameMilliseconds, TimeUnit.MILLISECONDS);
		};
    }
}