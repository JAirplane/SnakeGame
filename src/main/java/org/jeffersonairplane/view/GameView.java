package org.jeffersonairplane.view;

import java.util.List;

public interface GameView extends UserInputObservable {

    void setSnakeShape(List<RectangleUpperLeftPoint> snakeShape);
	int getIndentX();
	int getIndentY();
    RectangleDimension getBlockDimension();
    void setKeepFrameCounting(boolean countFrames);
    void runFrameCounter();
    void repaintGameWindow();
}