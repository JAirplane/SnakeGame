package org.jeffersonairplane.view;

/**
* Transfers settings, chosen by user, to ViewModel before gameplay started.
*/
public record ChosenSettingsDTO(int powerUpsLimit, int xAxisBlocksAmount, int yAxisBlocksAmount) {}