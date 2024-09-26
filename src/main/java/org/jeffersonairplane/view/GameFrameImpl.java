package org.jeffersonairplane.view;

import java.awt.event.KeyEvent;
import java.util.*;
import javax.swing.*;


public class GameFrameImpl extends JFrame implements GameFrame {

	private final GameWindow gameWindow;
	
    public GameFrameImpl(String title, GameWindow gameWindowImpl) {
        gameWindow = gameWindowImpl;
        add(gameWindow);
        addKeyListener(gameWindow);
        setTitle(title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setVisible(true);
        setLocationRelativeTo(null);

    }
	
	@Override
	public void registerInputObserver(InputObserver obs) {
		gameWindow.registerInputObserver(obs);
	}
	
	@Override
	public void removeInputObserver(InputObserver obs) {
		gameWindow.removeInputObserver(obs);
	}
	
	@Override
	public void notifyInputObservers(KeyEvent key) {
		gameWindow.notifyInputObservers(key);
	}
	
	public void setSnakeShape(List<RectangleUpperLeftPoint> snakeShape) {
		gameWindow.setSnakeShape(snakeShape);
	}

	@Override
	public void repaintGameWindow() {
		gameWindow.repaint();
	}
}
