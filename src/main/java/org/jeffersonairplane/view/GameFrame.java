package org.jeffersonairplane.view;

import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import static com.sun.java.accessibility.util.AWTEventMonitor.*;


public class GameFrame extends JFrame {

	private final GameWindow gameWindow;
	private final InfoWindow scoreWindow;

    public GameFrame(String title, GameWindow gameWindow, InfoWindow scoreWindow) {
        this.gameWindow = gameWindow;
		this.scoreWindow = scoreWindow;
		JPanel container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		container.add(this.scoreWindow);
		container.add(this.gameWindow);
        add(container);
        addKeyListener(gameWindow);
        setTitle(title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
    }
}
