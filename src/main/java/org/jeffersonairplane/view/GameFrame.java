package org.jeffersonairplane.view;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    public GameFrame(String title, int panelWidth, int panelHeight, int blockWidth, int blockHeight, Color background) {
        var mainGamePanel = new MainGamePanel(panelWidth, panelHeight, blockWidth, blockHeight, background);
        add(mainGamePanel);
        addKeyListener(mainGamePanel);
        setTitle(title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setVisible(true);
        setLocationRelativeTo(null);

    }
}
