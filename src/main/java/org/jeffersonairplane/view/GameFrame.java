package org.jeffersonairplane.view;

import lombok.Setter;
import org.jeffersonairplane.viewmodel.Direction;

import java.awt.*;
import java.awt.event.*;
import java.util.function.Consumer;
import javax.swing.*;

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

	@Setter
	private Consumer<Direction> movement;
	@Setter
	private Runnable rerun;
	@Setter
	private Runnable togglePause;
	@Setter
	private Consumer<Boolean> toMenu;

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
		this.menuWindow.setExitGame(this::processWindowEvent);
		this.menuWindow.setExitEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		this.menuWindow.setSwitchToGameplay(this::gameplay);
		gameplayWindowContainer = new JPanel();
		gameplayWindowContainer.setLayout(new BoxLayout(gameplayWindowContainer, BoxLayout.Y_AXIS));
		gameplayWindowContainer.add(this.scoreWindow);
		gameplayWindowContainer.add(this.gameWindow);
		cardLayout = new CardLayout();
		mainContainer = new JPanel(cardLayout);
		mainContainer.add(menuWindow, "menu");
		mainContainer.add(gameplayWindowContainer, "gameplay");
        add(mainContainer);

        setTitle(title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
    }

	public void setGameplayInputs() {
        String moveUp = "Move_Up";
        gameWindow.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), moveUp);
        String moveDown = "Move_Down";
        gameWindow.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), moveDown);
        String moveLeft = "Move_Left";
        gameWindow.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), moveLeft);
        String moveRight = "Move_Right";
        gameWindow.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), moveRight);
		String restart = "Restart";
		gameWindow.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), restart);
		String pause = "Pause";
		gameWindow.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), pause);
		String returnToMenu = "Return To Menu";
		gameWindow.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), returnToMenu);

		gameWindow.getActionMap().put(moveUp, new MoveAction(Direction.UP));
		gameWindow.getActionMap().put(moveDown, new MoveAction(Direction.DOWN));
		gameWindow.getActionMap().put(moveLeft, new MoveAction(Direction.LEFT));
		gameWindow.getActionMap().put(moveRight, new MoveAction(Direction.RIGHT));
		gameWindow.getActionMap().put(restart, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				rerun.run();
			}
		});
		gameWindow.getActionMap().put(pause, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				togglePause.run();
			}
		});
		gameWindow.getActionMap().put(returnToMenu, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				toMenu.accept(false);
				menu();
			}
		});
	}

	/**
	 * Prepare input listener and switches layout to show gameplay.
	 */
	public void gameplay() {
		cardLayout.show(mainContainer, "gameplay");
	}

	/**
	 * Removes gameplay input listener and switches layout to show menu.
	 */
	public void menu() {
		cardLayout.show(mainContainer, "menu");
	}

	private class MoveAction extends AbstractAction {

		Direction direction;

		MoveAction(Direction direction) {
			this.direction = direction;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			movement.accept(direction);
		}
	}


}
