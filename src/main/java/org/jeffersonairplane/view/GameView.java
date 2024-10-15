package org.jeffersonairplane.view;

import java.awt.*;
import java.util.Collection;
import java.util.List;

public interface GameView extends UserInputObservable {

    void setSnakeShape(List<RectangleUpperLeftPoint> snakeShape);
    void setPowerUps(List<RectangleUpperLeftPoint> powerUpsPoints);
    void setSnakeAnimation(Collection<Color> colors);
    void repaintInfoWindow();
	int getIndentX();
	int getIndentY();
    RectangleDimension getBlockDimension();
    void repaintGameWindow();
    PowerUpMessages getPowerUpMessages();
    void setScore(long score);
    void addMessageToShow(String message);
}