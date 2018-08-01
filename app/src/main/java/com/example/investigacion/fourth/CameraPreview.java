package com.example.investigacion.fourth;

/**
 * Created by investigacion on 3/22/2018.
 */

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.investigacion.fourth.helper.FaceOverlayView;

import static android.content.ContentValues.TAG;

/** A basic Camera preview class */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private final MarkFaceDetectedListener mCallback;
    private SurfaceHolder mHolder;
    private SurfaceView surface;
    private boolean preview = false;
    private Camera mCamera;
    



    private Camera.Face[] mFaces;
    public FaceOverlayView mFaceView;


    Camera.FaceDetectionListener faceDetectionListener = new Camera.FaceDetectionListener() {
        @Override
        public void onFaceDetection(Camera.Face[] faces, Camera camera) {
            if(faces.length >0)
            Log.i("onFaceDetection",
                    "Number of Faces:" + faces.length
                            + " id face "+faces[0].id
                            + " left eye " +faces[0].leftEye
                            + " mouth "+faces[0].mouth
                            + "right eye " +faces[0].rightEye
                            + "score " +faces[0].score);
            // Update the view now!
           mFaceView.setFaces(faces);
           mCallback.markWithFace(camera);
           

        }
    };





    public CameraPreview(Context context, Camera camera, SurfaceView surface, Object fragment) {
        super(context);
        mCamera = camera;
        this.surface = surface;
        mHolder = surface.getHolder();//getHolder();
        mHolder.addCallback(this);


        try {
            mCallback = (MarkFaceDetectedListener) fragment ;
        } catch (ClassCastException e) {
            throw new ClassCastException(fragment.toString()
                    + " must implement OnHeadlineSelectedListener");
        }

    }



    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.i("Preview","SurfaceCreated");


            mCamera.setFaceDetectionListener(faceDetectionListener);

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

        if(preview){
            mCamera.stopFaceDetection();
            mCamera.stopPreview();
            preview = false;
        }


        Log.i("Preview","SurfaceChanged");

        try {

            mCamera.setPreviewDisplay(surfaceHolder);
            mCamera.startPreview();
            mCamera.startFaceDetection();
            preview = true;




        } catch (Exception e){
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.i("Preview", "SurfaceDestroyed");
        try {
            mCamera.stopFaceDetection();
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
            preview = false;
        } catch (Exception e) {

        }
    }
    
    public interface MarkFaceDetectedListener{
        void markWithFace(Camera camera);
    }



}

