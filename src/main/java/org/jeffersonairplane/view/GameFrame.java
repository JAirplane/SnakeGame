package org.jeffersonairplane.view;

import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import static com.sun.java.accessibility.util.AWTEventMonitor.*;


public class GameFrame extends JFrame {

	private final GameWindow gameWindow;

    public GameFrame(String title, GameWindow gameWindow) {
        this.gameWindow = gameWindow;
        add(gameWindow);
        addKeyListener(gameWindow);
        setTitle(title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
    }

    public void repaintGameWindow() {
		gameWindow.repaint();
	}
}
