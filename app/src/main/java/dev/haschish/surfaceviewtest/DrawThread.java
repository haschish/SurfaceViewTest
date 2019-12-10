package dev.haschish.surfaceviewtest;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class DrawThread extends Thread {
    private SurfaceHolder surfaceHolder;
    private volatile boolean running = true;

    public DrawThread(Context context, SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
    }

    public void requestStop() {
        running = false;
    }

    @Override
    public void run() {
        while(running) {
            Canvas canvas = surfaceHolder.lockCanvas();

            if (canvas != null) {
                try {
                    //рисование на canvas
                } finally {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}
