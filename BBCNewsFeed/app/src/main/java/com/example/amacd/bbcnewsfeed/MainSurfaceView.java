package com.example.amacd.bbcnewsfeed;

import android.content.Context;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by amacd on 23/11/2016.
 */

//bobbys surface view set up code
public class MainSurfaceView extends SurfaceView implements SurfaceHolder.Callback
{
    SurfaceHolder surfaceHolder;
    MainCanvasThread drawingThread = null;

    public MainSurfaceView(Context context)
    {
        super(context);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        drawingThread = new MainCanvasThread(getHolder(), this);
        setFocusable(true);
    }

    public MainCanvasThread getDrawingThread()
    {
        return drawingThread;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        drawingThread.setRunning(true);
        drawingThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        drawingThread.setSurfaceSize(width, height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        boolean retry = true;
        drawingThread.setRunning(false);
        while (retry)
        {
            try
            {
                drawingThread.join();
                retry = false;
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}
