package org.jeffersonairplane.view;

import java.util.List;

public interface GameView extends UserInputObservable {

    void setSnakeShape(List<RectangleUpperLeftPoint> snakeShape);
    void setPowerUps(List<RectangleUpperLeftPoint> powerUpsPoints);
	int getIndentX();
	int getIndentY();
    RectangleDimension getBlockDimension();
    void repaintGameWindow();
}