package com.example.investigacion.fourth.tools;

import android.hardware.Camera;
import android.util.Log;

public class CameraTools {

     public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(1); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
            //TODO avisar que la camara no esta disponible
            Log.d("Init Camera", "getCameraInstance: error");
        }
         return c; // returns null if camera is unavailable
    }
}
