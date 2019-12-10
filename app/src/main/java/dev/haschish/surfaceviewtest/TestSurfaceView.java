package dev.haschish.surfaceviewtest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

class TestSurfaceView extends SurfaceView implements SurfaceHolder.Callback{
    private Thread thread;
    private boolean running = true;
    private int cx;
    private int cy;
    private long start = System.currentTimeMillis();
    private boolean touched = false;

    private Paint paint = new Paint();
    {
        paint.setColor(Color.YELLOW);
    }

    public TestSurfaceView(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        thread = new Thread() {
            @Override
            public void run() {
                while (running) {
                    long now = System.currentTimeMillis();
                    long seconds = (now - start) / 1000;
                    int radius = (touched) ? (int)seconds * 5 : 0;
                    Canvas canvas = getHolder().lockCanvas();

                    canvas.drawColor(Color.BLUE);
                    canvas.drawCircle(cx, cy, radius, paint);

                    getHolder().unlockCanvasAndPost(canvas);
                }
            }
        };
        thread.start();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        running = false;
        boolean retry = true;
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch(InterruptedException e) {
                //
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        start = System.currentTimeMillis();
        cx = (int)event.getX();
        cy = (int)event.getY();
        touched = true;
        return super.onTouchEvent(event);
    }
}