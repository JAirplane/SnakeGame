package org.jeffersonairplane.view;

import java.awt.*;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class GameWindow extends JPanel implements KeyListener, UserInputObservable {

    private final int windowWidth;
    private final int windowHeight;
    private final int blockWidth;
    private final int blockHeight;
    private final Color background;
	private List<RectangleUpperLeftPoint> snakeShape;
	
	private final List<InputObserver> userInputObservers = new ArrayList<>();

    public GameWindow(int windowWidth, int windowHeight, int blockWidth, int blockHeight, Color background) {
        this.setPreferredSize(new Dimension(windowWidth, windowHeight));
        this.setBackground(background);
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.blockWidth = blockWidth;
        this.blockHeight = blockHeight;
        this.background = background;
    }
	
	@Override
	public void registerInputObserver(InputObserver obs) {

        userInputObservers.add(obs);
	}
	
	@Override
    public void removeInputObserver(InputObserver obs) {

        userInputObservers.remove(obs);
	}
	
	@Override
    public void notifyInputObservers(KeyEvent key) {
		for(InputObserver obs: userInputObservers) {
			obs.inputUpdate(key);
		}
	}
	
	void setSnakeShape(List<RectangleUpperLeftPoint> snakeShape) {

        this.snakeShape = snakeShape;
	}
	
    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        drawGrid(graphics);
		drawSnakeShape(graphics);

    }
	
	void drawGrid(Graphics graphics) {
		for(int i = 0; i < windowWidth / blockWidth; i++) {
            graphics.drawLine(i * blockWidth, 0, i * blockWidth, windowHeight);
        }
        for(int i = 0; i < windowHeight / blockHeight; i++) {
            graphics.drawLine(0, i * blockHeight, windowWidth, i * blockHeight);
        }
	}
	
	void drawSnakeShape(Graphics graphics) {
		if(snakeShape == null || snakeShape.isEmpty()) return;
        for(var point: snakeShape) {
            graphics.setColor(Color.GREEN);
            graphics.fillRect(point.x(), point.y(), blockWidth, blockHeight);
        }
	}
	
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        notifyInputObservers(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}
