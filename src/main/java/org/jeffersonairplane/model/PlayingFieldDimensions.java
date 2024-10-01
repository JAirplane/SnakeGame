package org.jeffersonairplane.model;

public class PlayingFieldDimensions {
	
	private int fieldWidth;
	private int fieldHeight;
	private int blockWidth;
	private int blockHeight;
	
	public PlayingFieldDimensions(int width, int height, int blockWidth, int blockHeight) {
		fieldWidth = width;
		fieldHeight = height;
		this.blockWidth = width / blockWidth;
		this.blockHeight = height / blockHeight;
	}
	
	
}