package org.jeffersonairplane.view;

import lombok.*;

import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.*;

@Getter
public class InfoWindow extends JPanel {
	@Setter
    private Color background;
	@Setter
    private Color textColor;

	private final JLabel infoLabel;
	
	public InfoWindow(RectangleDimension infoWindowDimension, Color background, Color textColor) {
		this.background = background;
		this.textColor = textColor;
		this.setBackground(background);
		this.setPreferredSize(new Dimension(infoWindowDimension.width(), infoWindowDimension.height()));
		infoLabel = new JLabel();
		infoLabel.setFont(new Font("Serif", Font.BOLD, 30));
		infoLabel.setForeground(Color.lightGray);
		this.add(infoLabel);

	}
	
}