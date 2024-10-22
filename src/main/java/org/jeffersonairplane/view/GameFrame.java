package org.jeffersonairplane.view;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import static com.sun.java.accessibility.util.AWTEventMonitor.*;

/**
* Application window class.
* Contains all the other view game elements.
*/
public class GameFrame extends JFrame {
	
	private final JPanel mainContainer;
	private final CardLayout cardLayout;
	
	private final JPanel gameplayWindowContainer;
	
	private final GameWindow gameWindow;
	private final InfoWindow scoreWindow;
	private final MenuWindow menuWindow;

	/**
	* Constructor.
	* @param title is an application title.
	* @param gameWindow is a playing field element.
	* @param scoreWindow is an element showing messages during gameplay.
	 * @param menuWindow is a game main menu element.
	*/
    public GameFrame(String title, GameWindow gameWindow, InfoWindow scoreWindow, MenuWindow menuWindow) {
        this.gameWindow = gameWindow;
		this.scoreWindow = scoreWindow;
		this.menuWindow = menuWindow;
		gameplayWindowContainer = new JPanel();
		gameplayWindowContainer.setLayout(new BoxLayout(gameplayWindowContainer, BoxLayout.Y_AXIS));
		gameplayWindowContainer.add(this.scoreWindow);
		gameplayWindowContainer.add(this.gameWindow);
		cardLayout = new CardLayout();
		mainContainer = new JPanel(cardLayout);
		mainContainer.add(gameplayWindowContainer, "gameplay");
		mainContainer.add(menuWindow, "menu");
        add(mainContainer);
		
        addActionListener(menuWindow);
        setTitle(title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
    }
	
	public void gameplay() {
		removeActionListener(menuWindow);
		addKeyListener(gameWindow);
		cardLayout.show(mainContainer, "gameplay");
	}
	
	public void menu() {
		removeKeyListener(gameWindow);
		addActionListener(menuWindow);
		cardLayout.show(mainContainer, "menu");
	}
}
