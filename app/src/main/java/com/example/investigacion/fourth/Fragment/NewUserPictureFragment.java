package com.example.investigacion.fourth.Fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.investigacion.fourth.CameraPreview;
import com.example.investigacion.fourth.MainActivity;
import com.example.investigacion.fourth.R;
import com.example.investigacion.fourth.adapter.RecycleViewFivePictures;
import com.example.investigacion.fourth.adapter.RecycleViewLastUsers;
import com.example.investigacion.fourth.helper.Messages;
import com.example.investigacion.fourth.listener.EchoWebSocketListener;
import com.example.investigacion.fourth.model.Suggestions;
import com.example.investigacion.fourth.tools.ComRequest;

import org.w3c.dom.Text;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okio.ByteString;

import static com.example.investigacion.fourth.tools.CameraTools.getCameraInstance;


public class NewUserPictureFragment extends Fragment implements CameraPreview.MarkFaceDetectedListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayList<Bitmap> pictures = new ArrayList<Bitmap>();



    private Camera mCamera;
    private CameraPreview mPreview;
    private Button captureButton;
    private Boolean busy;





    private OnFragmentInteractionListener mListener;
    private RecyclerView mRecyclerView;
    private RecycleViewFivePictures mAdapter;
    private ProgressBar progress;
    private LinearLayout preview_list;





    public Suggestions.UserCode person;
    private TextView name;
    private Button finishButton;
    private Button cancelButton;
    private Request request;
    private OkHttpClient client;
    private WebSocket ws;
    private TextView picture_count_textview;

    public NewUserPictureFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewUserPicture.
     */
    // TODO: Rename and change types and number of parameters
    public static NewUserPictureFragment newInstance(String param1, String param2) {
        NewUserPictureFragment fragment = new NewUserPictureFragment();
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



        // inicializacion de conexiones - socket - request- client -
        client = new OkHttpClient();
        request = new Request.Builder().url("ws://10.10.25.9:8888/uploadphoto").addHeader("Cookie", "userid=\"2|1:0|10:1530135750|6:userid|4:MQ==|9f33b80bb688cc0230a8c43dcf212c89df9c18997b284e49d20727afcf89f28b\"; user=\"2|1:0|10:1530138580|4:user|12:TVNBTkNIRVo=|3eda4e2e45a55956416a77c3dae956b6b72b4663ae6ff115648b96fbabebe1cd\"; name=\"2|1:0|10:1530138580|4:name|20:TWFyY28gU2FuY2hleg==|7c59add4e8ed9f0acd94374af68ba7f86ab2a344026ffc8a5bf0a92faec3c5f8\"").build();
        EchoWebSocketListener listener = new EchoWebSocketListener(){
            @Override
            public void onMessage(WebSocket webSocket, String text) {
                Log.i("WebSocket-addPhoto",text);
                busy = false;
            }
        };
        ws = client.newWebSocket(request, listener);
        client.dispatcher().executorService().shutdown();




        View v =  inflater.inflate(R.layout.fragment_new_user_picture, container, false);
        captureButton = (Button) v.findViewById(R.id.button_capture);
        finishButton = (Button) v.findViewById(R.id.button_finish);
        cancelButton = (Button) v.findViewById(R.id.button_cancel);
        preview_list = (LinearLayout) v.findViewById(R.id.ll_list_preview);
        name = (TextView) v.findViewById(R.id.textview_new_user);
        picture_count_textview = (TextView) v.findViewById(R.id.textview_new_user_label);





        // LISTENERS DE LOS BOTONES
        captureButton.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  mCamera.takePicture(null,null,mPicture);
                }
            }
        );


        finishButton.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    start();


                }
            }
        );




        picture_count_textview.setText("0/5 fotos cargadas");
        name.setText(person.value);





        mCamera = getCameraInstance();

        SurfaceView preview = (SurfaceView) v.findViewById(R.id.camera_preview);
        mPreview = new CameraPreview(getActivity(), mCamera,preview,this);

        progress = (ProgressBar) v.findViewById(R.id.progressbar_new_user);
        progress.setProgress(0);


        Messages.introNewUserPicture(getActivity());


        return v;
    }



    private void start() {


        String encodedName = null;

        try {
            encodedName = URLEncoder.encode(person.value, "UTF-8");
        } catch (UnsupportedEncodingException ignored) {
            // Can be safely ignored because UTF-8 is always supported
        }

        new ComRequest.post().execute("http://10.10.25.9:8888/users/name", "user="+person.data+"&name="+encodedName);


        busy =false;






        Bitmap bmp;
        Integer photonumber=1;



        for (int i=0;i<5;i++){



            ImageView v = (ImageView) ((ViewGroup)preview_list).getChildAt(i);
            bmp = ( (BitmapDrawable)v.getDrawable() ).getBitmap();




             photonumber++;
            Log.i("PhotoNumber",photonumber.toString());

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();

             new SendPhotoTask("http://10.10.25.9:8888/users/photonumber", "photonumber=" + photonumber.toString(),ByteString.of(byteArray)).execute();
        }

        new ComRequest.post().execute("http://10.10.25.9:8888/train","");

    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mCamera.release();
    }

    @Override
    public void markWithFace(Camera camera) {
        // todo incluir mecanimos de verificacion de fotos, si no se reconoce una cara es un error
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }




    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {



            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(300 ,200);


            param.setMargins(0,0,0,0);
            //pictures.add(bitmap);
            ImageView image = new ImageView(getContext());


            image.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // get an image from the camera
                            //todo activar los botones en el caso de tener opciones
                            picture_count_textview.setText(preview_list.getChildCount()+"/5 fotos cargadas");
                            preview_list.removeView(v);


                        }
                    }
            );
            
            image.setImageBitmap(bitmap);
            preview_list.addView(image,param);



            picture_count_textview.setText(preview_list.getChildCount()+"/5 fotos cargadas");
            //mAdapter.notifyDataSetChanged();
            Float asd = preview_list.getChildCount() / 5.0f;
            progress.setProgress((int)(asd*100));
            if(preview_list.getChildCount() >= 5){

                captureButton.setEnabled(false);
                cancelButton.setEnabled(true);
                finishButton.setEnabled(true);

            }

            for (int i=0;i<preview_list.getChildCount();i++){
                ImageView v = (ImageView) ((ViewGroup)preview_list).getChildAt(i);
                ( (BitmapDrawable)v.getDrawable() ).getBitmap();
            }
            camera.startPreview();


        }
    };


    private Camera.PictureCallback jpgPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

        }


    };


    private class SendPhotoTask extends AsyncTask<Object,Integer, String> {

        String url;
        String postData;
        ByteString photo;
        Request r1;


        public SendPhotoTask(Object... param) {
            this.url = (String)param[0];
            this.postData = (String)param[1];
            this.photo = (ByteString)param[2];
        }

        protected String doInBackground(Object... urls) {
            Log.i("EVENT", "AsyncTask post");
            String responsetxt = null;
            try {
                URL url = new URL(this.url);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                // conn.setDoOutput(true);

                conn.setRequestMethod("POST");
                conn.setRequestProperty("Cookie", "userid=\"2|1:0|10:1530135750|6:userid|4:MQ==|9f33b80bb688cc0230a8c43dcf212c89df9c18997b284e49d20727afcf89f28b\"; user=\"2|1:0|10:1530138580|4:user|12:TVNBTkNIRVo=|3eda4e2e45a55956416a77c3dae956b6b72b4663ae6ff115648b96fbabebe1cd\"; name=\"2|1:0|10:1530138580|4:name|20:TWFyY28gU2FuY2hleg==|7c59add4e8ed9f0acd94374af68ba7f86ab2a344026ffc8a5bf0a92faec3c5f8\"");


                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(this.postData);
                writer.close();

                int respCode = conn.getResponseCode();  // New items get NOT_FOUND on PUT


                if (respCode == HttpURLConnection.HTTP_OK || respCode == HttpURLConnection.HTTP_NOT_FOUND) {
                    //req.setAttribute("error", "");
                    StringBuffer response = new StringBuffer();
                    String line;

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                    Log.i("Request with modify",response.toString());
                    responsetxt = response.toString();
                }

            } catch (ProtocolException e) {
                e.printStackTrace();
                //  Log.i("error",e.getMessage());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                //  Log.i("error",e.getMessage());
            } catch (MalformedURLException e) {
                e.printStackTrace();
                //  Log.i("error",e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                //Log.i("error",e.getMessage());
            }

            busy = true;
            ws.send(this.photo);
            r1 = ws.request();
            while (busy);



            return responsetxt;


        }

        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

        protected void onPostExecute(String asd) {

            //  showDialog("Downloaded " + result + " bytes");
        }
    }
}



