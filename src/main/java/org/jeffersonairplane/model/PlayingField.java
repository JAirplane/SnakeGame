package org.jeffersonairplane.model;

/**
 * Playing field data stored here
 */
public class PlayingField {
	private final int blocksWidth;
    private final int blocksHeight;
	
	public PlayingField(int blocksWidth, int blocksHeight) {
		this.blocksWidth = blocksWidth;
		this.blocksHeight = blocksHeight;
	}
	/**
	 * Getter for play field width
	 * Block is a rectangle here
	 */
	int getWidthInBlocks() {
		return blocksWidth;
	}
	
	/**
	 * Getter for play field height
	 * Block is a rectangle here
	 */
	int getHeightInBlocks() {
		return blocksHeight;
	}
}