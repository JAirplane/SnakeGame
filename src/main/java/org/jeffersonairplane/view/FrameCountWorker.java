package org.jeffersonairplane.view;

import javax.swing.*;

class FrameCountWorker extends SwingWorker<Integer, Integer>
{
    private GameFrame frame;
    private int frameDuration;

    FrameCountWorker(GameFrame frame, int frameDuration) {
        this.frame = frame;
        this.frameDuration = frameDuration;
    }
    protected Integer doInBackground() throws Exception
    {
        // Do a time-consuming task.
        Thread.sleep(frameDuration);
        return frameDuration;
    }

    protected void done()
    {
        try
        {
            frame.incrementElapsedTime(frameDuration);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
