package org.jeffersonairplane.view;

import java.util.List;

public interface GameFrame extends UserInputObservable {

    void setSnakeShape(List<RectangleUpperLeftPoint> snakeShape);
    void repaintGameWindow();
}
