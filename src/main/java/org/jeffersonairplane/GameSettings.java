package org.jeffersonairplane;

class GameSettings {
    private final int pixelHeight;
    private final int pixelWidth;
    private final int blockSize;
    GameSettings(int height, int width, int blockSize){
        pixelHeight = height;
        pixelWidth = width;
        this.blockSize = blockSize;
    }

    public int getPixelWidth() {
        return pixelWidth;
    }

    public int getPixelHeight() {
        return pixelHeight;
    }

    public int getBlockSize() {
        return blockSize;
    }
}
