package org.jeffersonairplane.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MainGamePanel extends JPanel implements KeyListener {

    private final int panelWidth;
    private final int panelHeight;
    private final int blockWidth;
    private final int blockHeight;
    private final Color background;

    MainGamePanel(int panelWidth, int panelHeight, int blockWidth, int blockHeight, Color background) {
        this.setPreferredSize(new Dimension(panelWidth, panelHeight));
        this.setBackground(background);
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;
        this.blockWidth = blockWidth;
        this.blockHeight = blockHeight;
        this.background = background;
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        for(int i = 0; i < panelWidth / blockWidth; i++) {
            graphics.drawLine(i * blockWidth, 0, i * blockWidth, panelHeight);
        }
        for(int i = 0; i < panelHeight / blockHeight; i++) {
            graphics.drawLine(0, i * blockHeight, panelWidth, i * blockHeight);
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {
        System.out.println("TYPED");
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("PRESSED");
    }

    @Override
    public void keyReleased(KeyEvent e) {
        System.out.println("RELEASED");
    }
}
