package com.example.investigacion.fourth.helper;

import android.util.Log;

import com.example.investigacion.fourth.tools.ComRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.ExecutionException;

public class ClassJSONParser {
    public static Object json2obj(String url,String cookie, Class typeClass){
        String jsonresponse = null;
        try {
            jsonresponse = new ComRequest.get().execute(url,cookie).get();
        } catch (InterruptedException e) {
            e.printStackTrace();

            //Log.i("error",e.getMessage());
        } catch (ExecutionException e) {
            e.printStackTrace();
            //Log.i("error",e.getMessage());
        }
        Log.i("RESPUESTA DE JSON", jsonresponse);

        Gson gson = new GsonBuilder().create();
        Object p = gson.fromJson(jsonresponse, typeClass);
        return p;

    }
}
