package dev.haschish.surfaceviewtest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

class TestSurfaceView extends SurfaceView implements SurfaceHolder.Callback{
    private TextDrawThread thread;

    public TestSurfaceView(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    private class TextDrawThread extends Thread {
        private boolean running = true;
        private boolean touched = false;

        private int cx = 0;
        private int cy = 0;
        private long start = 0;

        private Paint paint = new Paint();
        {
            paint.setColor(Color.YELLOW);
        }

        @Override
        public void run() {
            while (running) {
                try {
                    long now = System.currentTimeMillis();
                    long seconds = (now - start) / 1000;
                    int radius = (touched) ? (int)seconds * 5 : 0;
                    Canvas canvas = getHolder().lockCanvas();

                    canvas.drawColor(Color.BLUE);
                    canvas.drawCircle(cx, cy, radius, paint);

                    getHolder().unlockCanvasAndPost(canvas);
                    thread.sleep(200);
                } catch (Exception e) {
                    //
                }
            }
        }

        public void requestStop() {
            running = false;
        }

        public void setTouchCoords(int x, int y) {
            cx = x;
            cy = y;
            touched = true;
            start = System.currentTimeMillis();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new TextDrawThread();
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        thread.requestStop();
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
        thread.setTouchCoords((int)event.getX(), (int)event.getY());
        return false;
    }
}