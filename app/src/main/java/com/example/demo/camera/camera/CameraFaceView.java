package com.example.demo.camera.camera;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

public class CameraFaceView extends SurfaceView{

    public CameraFaceView(Context context) {
        super(context);

    }


    public CameraFaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CameraFaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setCameraDisplayOrientation(Activity activity,
                                                   int cameraId, Camera camera) {

        if (camera==null){
            return;
        }

        Camera.CameraInfo info =
                new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;

        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {



            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }

        camera.setDisplayOrientation(result);
    }




}
