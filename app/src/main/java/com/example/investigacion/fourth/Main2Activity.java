package com.example.investigacion.fourth;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.io.IOException;

public class Main2Activity extends AppCompatActivity implements SurfaceHolder.Callback {

    public SurfaceView sv;
    public SurfaceHolder sh;
    public Camera camera;
    private boolean preview = false;
    private boolean faceDetectionRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


         sv = (SurfaceView) findViewById(R.id.surface_view);
         sh = sv.getHolder();
         sh.addCallback(this);

    }




    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

        Log.i("camera","surfacecreated");
        int n = Camera.getNumberOfCameras();
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < n; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                camera = Camera.open(i);

                if (camera.getParameters().getMaxNumDetectedFaces() <= 0) {
                    Log.e("ERROR", "Face Detection not supported");
                    return ;
                }
                camera.setFaceDetectionListener(new FaceCatcher());
                return;
            }
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        if(preview){
            camera.stopFaceDetection();
            camera.stopPreview();
            preview = false;
        }

        if (camera != null){
            try {
                camera.setPreviewDisplay(surfaceHolder);

                camera.startPreview();


                camera.startFaceDetection();

                faceDetectionRunning = true;
                preview = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    class FaceCatcher implements Camera.FaceDetectionListener {
        private boolean light = false;

        @Override
        public void onFaceDetection(Camera.Face[] faces, Camera camera) {


            if (faces != null && faces.length > 0) {
                for (Camera.Face face : faces) {
                    System.out.println(face.score);
                }
                if (!this.light) {
                    this.light = true;
                    System.out.println("Detected!");

                } else {
                    System.out.println("The light is already on");
                    System.out.println(faces.toString());
                }
            } else {
                if (this.light) {

                    this.light = false;
                } else {
                    System.out.println("The light is off...");
                }
            }
        }
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        try {
            camera.stopFaceDetection();
            camera.stopPreview();
            camera.release();
            camera = null;
            preview = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
