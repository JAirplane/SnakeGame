package org.jeffersonairplane.view;

import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import static com.sun.java.accessibility.util.AWTEventMonitor.*;


public class GameFrame extends JFrame {

	private final GameWindow gameWindow;
	private long elapsedTime;
    private FrameCountWorker frameCounter;

    public GameFrame(String title, GameWindow gameWindow) {
        frameCounter = new FrameCountWorker(GameFrame.this, 33);
        this.gameWindow = gameWindow;
        add(gameWindow);
        addKeyListener(gameWindow);
        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frameCounter.execute();
            }
        });
        setTitle(title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
    }

    protected synchronized void incrementElapsedTime(int milliseconds) {
        if(elapsedTime > Long.MAX_VALUE - 1000) {
            elapsedTime = milliseconds;
        }
        else {
            elapsedTime += milliseconds;
        }
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public void repaintGameWindow() {
		gameWindow.repaint();
	}
}
