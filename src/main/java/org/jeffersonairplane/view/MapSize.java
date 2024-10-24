package org.jeffersonairplane.view;

import lombok.Getter;

@Getter
public enum MapSize {
	SMALL(15, 15),
	MEDIUM(20, 20),
	LARGE(25, 25);
	
	private int xAxisBlocks;
	private int yAxisBlocks;
	
	MapSize(int xAmount, int yAmount) {
		xAxisBlocks = xAmount;
		yAxisBlocks = yAmount;
	}
	
	public void setMapSize(int xAmount, int yAmount) {
		xAxisBlocks = xAmount;
		yAxisBlocks = yAmount;
	}
}