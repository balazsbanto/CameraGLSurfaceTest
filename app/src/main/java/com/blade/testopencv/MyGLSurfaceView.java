package com.blade.testopencv;

import org.opencv.android.CameraGLSurfaceView;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.widget.TextView;
import android.widget.Toast;

public class MyGLSurfaceView extends CameraGLSurfaceView implements CameraGLSurfaceView.CameraTextureListener {

    static final String LOGTAG = "MyGLSurfaceView";
    protected boolean frontFacing = false;

    public MyGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if(e.getAction() == MotionEvent.ACTION_DOWN)
            ((Activity)getContext()).openOptionsMenu();
        return true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        super.surfaceCreated(holder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        super.surfaceDestroyed(holder);
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        ((Activity) getContext()).runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getContext(), "onCameraViewStarted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCameraViewStopped() {
        ((Activity) getContext()).runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getContext(), "onCameraViewStopped", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setFrontFacing(boolean frontFacing) {
        this.frontFacing = frontFacing;
    }

    @Override
    public boolean onCameraTexture(int texIn, int texOut, int width, int height) {

        processFrame(texIn, texOut, width, height, frontFacing);
        return true;
    }

    private static native void processFrame(int tex1, int tex2, int w, int h, boolean frontFacing);


}
