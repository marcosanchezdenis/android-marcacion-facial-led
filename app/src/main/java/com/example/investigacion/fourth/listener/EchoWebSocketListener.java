package com.example.investigacion.fourth.listener;

import android.util.Log;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class EchoWebSocketListener extends WebSocketListener {
    private static final int NORMAL_CLOSURE_STATUS = 1000;







    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        webSocket.close(NORMAL_CLOSURE_STATUS, null);
        Log.i("Error","Closing : " + code + " / " + reason);


        //crear un toast
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        Log.i("Error","Error : " + t.getMessage());


        //crear un toast
    }
}
