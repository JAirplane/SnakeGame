package org.jeffersonairplane.view;

import java.awt.*;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.util.List;

import lombok.*;

public class GameWindow extends JPanel implements KeyListener, UserInputObservable {

    private final RectangleDimension windowDimension;
	@Getter
	private RectangleDimension blockDimension;
	@Getter
	private int indentX;
	@Getter
	private int indentY;
	@Getter @Setter
    private Color background;
	@Getter @Setter
	private Color snakeDefaultColor;
	@Getter
	private final Queue<Color> snakeAnimationColorQueue;
	@Setter
	private List<RectangleUpperLeftPoint> snakeShape;
	@Setter
	private List<RectangleUpperLeftPoint> powerUps;
	
	private final List<InputObserver> userInputObservers = new ArrayList<>();

    public GameWindow(RectangleDimension windowDimension, int blocksAmountX, int blocksAmountY, Color background, Color snakeDefaultColor) {
        this.setPreferredSize(new Dimension(windowDimension.width(), windowDimension.height()));
        this.setBackground(background);
        this.windowDimension = windowDimension;
		this.background = background;
		this.snakeDefaultColor = snakeDefaultColor;
		snakeAnimationColorQueue = new LinkedList<>();
		
		setBlockDimension(blocksAmountX, blocksAmountY);
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

	public void setBlockDimension(int blocksAmountX, int blocksAmountY) {
		if(windowDimension == null) throw new NullPointerException();
		blockDimension = new RectangleDimension(windowDimension.width() / blocksAmountX, windowDimension.height() / blocksAmountY);
        indentX = (windowDimension.width() - blockDimension.width() * blocksAmountX) / 2;
		indentY = (windowDimension.height() - blockDimension.height() * blocksAmountY) / 2;
	}
	
    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        drawGrid(graphics);
		drawSnakeShape(graphics);
		drawPowerUps(graphics);
    }
	
	public void drawGrid(Graphics graphics) {
		for(int i = 0; i <= windowDimension.width() / blockDimension.width(); i++) {
            graphics.drawLine(indentX + i * blockDimension.width(), indentY,
					indentX + i * blockDimension.width(), windowDimension.height() - indentY);
        }
        for(int i = 0; i <= windowDimension.height() / blockDimension.height(); i++) {
            graphics.drawLine(indentX, indentY + i * blockDimension.height(),
					windowDimension.width() - indentX, indentY + i * blockDimension.height());
        }
	}
	
	public void drawSnakeShape(Graphics graphics) {
		if(snakeShape == null || snakeShape.isEmpty()) return;
		Color snakeColor = null;
		if(!snakeAnimationColorQueue.isEmpty()) {
			snakeColor = snakeAnimationColorQueue.poll();
		}
		else {
			snakeColor = snakeDefaultColor;
		}
        for(var point: snakeShape) {
            graphics.setColor(snakeColor);
            graphics.fillRect(point.x(), point.y(), blockDimension.width(), blockDimension.height());
        }
	}
	
	public void drawPowerUps(Graphics graphics) {
		if(powerUps == null || powerUps.isEmpty()) return;
        for(var point: powerUps) {
            graphics.setColor(Color.RED);
            graphics.fillOval(point.x(), point.y(), blockDimension.width(), blockDimension.height());
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
