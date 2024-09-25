package org.jeffersonairplane.model;

/**
 * Playing field data stored here.
 * Width and height are integer numbers of blocks field consist of starting from 1.
 */
public class PlayingField {
	private int blocksWidth;
    private int blocksHeight;
	
	/**
	 * All args constructor
	 * @param blocksWidth integer number of blocks X-axis
	 * @param blocksHeight integer number of blocks Y-axis
	 * Block is a rectangle here
	 */
	public PlayingField(int blocksWidth, int blocksHeight) {
		this.blocksWidth = blocksWidth;
		this.blocksHeight = blocksHeight;
	}
	
	/**
	 * Getter for play field width
	 * Block is a rectangle here
	 * @return integer number of blocks X-axis
	 */
	int getFieldWidth() {
		return blocksWidth;
	}
	
	/**
	 * Setter for play field width
	 * Block is a rectangle here
	 * @param width is a new playing field width in blocks
	 */
	void setFieldWidth(int width) {
		blocksWidth = width;
	}
	
	/**
	 * Getter for play field height
	 * Block is a rectangle here
	 * @return integer number of blocks Y-axis
	 */
	int getFieldHeight() {
		return blocksHeight;
	}
	
	/**
	 * Setter for play field height
	 * Block is a rectangle here
	 * @param height is a new playing field height in blocks
	 */
	void setFieldHeight(int height) {
		blocksHeight = height;
	}
}