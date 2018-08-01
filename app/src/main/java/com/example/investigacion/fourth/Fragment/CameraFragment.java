package com.example.investigacion.fourth.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.investigacion.fourth.CameraPreview;
import com.example.investigacion.fourth.R;
import com.example.investigacion.fourth.adapter.MessagesAdapter;
import com.example.investigacion.fourth.helper.FaceOverlayView;
import com.example.investigacion.fourth.helper.Messages;
import com.example.investigacion.fourth.listener.EchoWebSocketListener;
import com.example.investigacion.fourth.model.FaceResponse;
import com.example.investigacion.fourth.model.LastUserResponse;
import com.example.investigacion.fourth.tools.ComRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;

import java.util.concurrent.ExecutionException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okio.ByteString;

import static com.example.investigacion.fourth.tools.CameraTools.getCameraInstance;


public class CameraFragment extends Fragment implements CameraPreview.MarkFaceDetectedListener {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;


    private View rootView;
    private Button start;
    private TextView output;
    private OkHttpClient client;
    private Camera mCamera;
    private CameraPreview mPreview;
    OnHeadlineSelectedListener mCallback;
    private Request request;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
   // private RecycleViewLastUsers mAdapter;
    private MessagesAdapter mAdapter;
    private String jsonresponse;
    private FaceOverlayView mFaceView;
    private FaceResponse person;
    private WebSocket ws;
    private ProgressDialog progress;
    private boolean faceDetected = false;


    public CameraFragment() {
        // Required empty public constructor
    }








    public static CameraFragment newInstance(String param1, String param2) {
        CameraFragment fragment = new CameraFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }





        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = (View) inflater.inflate(R.layout.fragment_camera, container, false);

        /*
        * Inicializar conexiones
        * el request
        * el client
        * la conexion al webservice, esta instancia puede terminar, TODO pero debe mantener en conexion
        *
        * */
        client = new OkHttpClient();
        request = new Request.Builder().url("ws://10.10.25.9:8888/predict").build();


        EchoWebSocketListener listener = new EchoWebSocketListener(){
        @Override
        public void onMessage(WebSocket webSocket, String text) {
            output2( text);
                progress.dismiss();
            }
        };
        ws = client.newWebSocket(request, listener);
        client.dispatcher().executorService().shutdown();




            mCamera = getCameraInstance();

            Button captureButton = (Button) rootView.findViewById(R.id.button_capture);


            captureButton.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progress = Messages.waitSendPhoto2Mark(getContext());
                    mCamera.takePicture(null, null, mPicture);
                }
            }
    );








        SurfaceView preview = (SurfaceView) rootView.findViewById(R.id.camera_preview);
        preview.getParent();
        mPreview = new CameraPreview( getActivity(), mCamera,preview,this);
        mPreview.mFaceView = new FaceOverlayView(getContext());

        ((ViewGroup)preview.getParent()).addView(mPreview.mFaceView);



        //todo verificar respuesta de conexiones
        try {
            // todo spot necesita hacer un request get y returna el JSON
            jsonresponse = new ComRequest.get().execute("http://10.10.25.1/personal/wslistmarcacion.php").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        jsonresponse= jsonresponse.substring(1, jsonresponse.length() - 1);

        Gson gson = new GsonBuilder().create();
        LastUserResponse p = gson.fromJson(jsonresponse, LastUserResponse.class);





        Log.i("LastUserResponse ",p.toString() );




        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));


        mRecyclerView.setHasFixedSize(true);


        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);


        mAdapter = new MessagesAdapter(getContext(),p.data);
        mRecyclerView.setAdapter(mAdapter);
        return rootView;

    }




    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


        try {
            mCallback = (OnHeadlineSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }






    @Override
    public void markWithFace(Camera camera) {
        if (!faceDetected) {
            progress = Messages.waitSendPhoto2Mark(getContext());
            mCamera.takePicture(null, null, mPicture);
            faceDetected = true;
        }
    }


    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            camera.stopFaceDetection();
            camera.stopPreview();
            ws.send(ByteString.of(data));
            camera.startPreview();
            camera.startFaceDetection();
            faceDetected = false;

        }
    };

    // Container Activity must implement this interface
    public interface OnHeadlineSelectedListener {
        public void onArticleSelected(FaceResponse person);
    }

    // Todo mejorar el proceso de captura de los datos pos server, debe realizarse en un thread
    private void output2(final String txt) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                try {
                    handleJSON(txt);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }



    // CONFIRMACION DE MARCACION


    private void handleJSON(String text) throws JSONException {
        Toast.makeText(getContext(), "data: " + text, Toast.LENGTH_LONG).show();


        Gson gson = new GsonBuilder().create();
        person = gson.fromJson(text, FaceResponse.class);
        System.out.println(person);

        if(person == null){
            Messages.noFaceDetected(getActivity());
        }else{

            Log.i("FACE", person.toString());

            mCallback.onArticleSelected(person);
        }




    }

}
