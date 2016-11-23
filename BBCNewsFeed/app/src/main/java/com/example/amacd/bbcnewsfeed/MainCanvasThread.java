package com.example.amacd.bbcnewsfeed;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;

/**
 * Created by amacd on 23/11/2016.
 */

public class MainCanvasThread extends Thread
{
    int canvasWidth;
    int canvasHeight;

    float halfHeight;
    float halfWidth;
    float thirdWidth;

    float boxSize;

    boolean first = true;
    boolean run = false;

    SurfaceHolder surfaceHolder;
    Paint paint;
    MainSurfaceView mainSurfaceView;

    public MainCanvasThread(SurfaceHolder surfaceHolder, MainSurfaceView SurfaceView)
    {
        this.surfaceHolder = surfaceHolder;
        paint = new Paint();
        mainSurfaceView = SurfaceView;
    }

    public void doStart()
    {
        synchronized (surfaceHolder)
        {
            first = false;
        }
    }

    public void run()
    {
        while (run)
        {
            Canvas c = null;
            try
            {
                c = surfaceHolder.lockCanvas(null);
                synchronized (surfaceHolder)
                {
                    draw(c);
                }
            }
            finally
            {
                if (c != null)
                {
                    surfaceHolder.unlockCanvasAndPost(c);
                }
            }
        }
    }

    public void setRunning(boolean b){run = b;}

    public void setSurfaceSize(int width, int height)
    {
        synchronized (surfaceHolder)
        {
            canvasWidth = width;
            canvasHeight = height;
            halfHeight = canvasHeight / 2;
            halfWidth = canvasWidth / 2;
            thirdWidth = canvasWidth / 3;
            boxSize = 300;
            doStart();
        }
    }

    void draw(Canvas canvas)
    {
        if (run)
        {
            canvas.save();
            canvas.restore();
            //background
            canvas.drawColor(Color.RED);
            paint.setStyle(Paint.Style.FILL);
            //boxes
            paint.setColor(Color.WHITE);
            canvas.drawRect(0, halfHeight - (boxSize/2), boxSize, halfHeight + (boxSize/2), paint);                                         //B
            canvas.drawRect(halfWidth - (boxSize/2), halfHeight - (boxSize/2), halfWidth + (boxSize/2), halfHeight + (boxSize/2), paint);   //B
            canvas.drawRect(canvasWidth - boxSize, halfHeight - (boxSize/2), canvasWidth, halfHeight + (boxSize/2), paint);                 //C
            //text
            paint.setColor(Color.RED);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize(boxSize);
            canvas.drawText("B", boxSize/2, halfHeight + (boxSize/2) - 40 , paint);                 //B
            canvas.drawText("B", halfWidth, halfHeight + (boxSize/2) - 40 , paint);                 //B
            canvas.drawText("C", canvasWidth - boxSize/2, halfHeight + (boxSize/2) - 40 , paint);   //C
            paint.setColor(Color.WHITE);
            paint.setTextSize(400);
            canvas.drawText("NEWS", halfWidth, halfHeight + 600, paint);                 //NEWS

        }
    }
}
