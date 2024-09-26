package org.jeffersonairplane.model;

/**
 * Playing field data stored here.
 * @param blocksAmountWidth integer number of blocks X-axis
 * @param blocksAmountHeight integer number of blocks Y-axis
 */
public record PlayingField(int blocksAmountWidth, int blocksAmountHeight) {}