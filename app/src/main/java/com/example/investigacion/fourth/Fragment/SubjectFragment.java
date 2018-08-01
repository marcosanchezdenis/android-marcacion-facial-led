package com.example.investigacion.fourth.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.example.investigacion.fourth.R;
import com.example.investigacion.fourth.model.FaceResponse;
import com.example.investigacion.fourth.model.Subjects;
import com.example.investigacion.fourth.model.SubjectsResponse;
import com.example.investigacion.fourth.model.Suggestions;
import com.example.investigacion.fourth.tools.ComRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class SubjectFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String username;
    private List<CheckBox> checkBoxList;
    private List<String> idContainer;
    private List<String> nameContainer;
    public FaceResponse faceData;


    //private OnFragmentInteractionListener mListener;

    public SubjectFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SubjectFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SubjectFragment newInstance(String param1, String param2) {
        SubjectFragment fragment = new SubjectFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_subject, container, false);

        checkBoxList = new ArrayList<>();
        idContainer = new ArrayList<>();
        nameContainer =  new ArrayList<>();


        String jsonresponse = null;
        try {
            jsonresponse = new ComRequest.get().execute("http://10.10.25.3/serviciosFacrec/materias2.php").get();
        } catch (InterruptedException e) {
            e.printStackTrace();

            //Log.i("error",e.getMessage());
        } catch (ExecutionException e) {
            e.printStackTrace();
            //Log.i("error",e.getMessage());
        }
        Log.i("RESPUESTA DE JSON", jsonresponse);





        Gson gson = new GsonBuilder().create();
        SubjectsResponse p = gson.fromJson(jsonresponse, SubjectsResponse.class);

        LinearLayout ll_materias_container = (LinearLayout) v.findViewById(R.id.ll_materias_container);
        Button bt_continue = (Button) v.findViewById(R.id.bt_subject_continue);
        bt_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


        if (!checkBoxList.isEmpty()){
            for (CheckBox cb: checkBoxList){
                if (cb.isChecked()){
                    idContainer.add((String)cb.getTag());
                    nameContainer.add(cb.getText().toString());
                }
            }
        }else{
            //mensaje de error
        }


        StringBuilder nameBuilder =  new StringBuilder();
        StringBuilder idBuilder = new StringBuilder();
        for(String id: idContainer){
            idBuilder.append(id);
            idBuilder.append(',');
        }
        for(String name : nameContainer){
            nameBuilder.append(name);
            nameBuilder.append(',');
        }
        String listName = nameBuilder.toString();
        String listId =  idBuilder.toString();

        listName =  listName.substring(0,listName.length()- 1);
        listId =  listId.substring(0,listId.length()- 1);

        Log.i("list-subject",listName);
        Log.i("list-subject-id" ,listId);


        String jsonresponse = null;


                /***
                 *
                 *
                 * ENDPOINT OF THE SERVER
                 * http://'+IPADDR_BD+'/serviciosFacrec/wsmarcacion.php?id=' + USERNAME   + '&hash=' + HASH     + '&listaMaterias=' + LISTAMATERIAS   + '&listaIDMaterias=' + LISTAIDMATERIAS   + '&tipo=' + TIPO
                 *
                 * Las informaciones necesarias del proceso de reconocimiento facial deben ser el ID de usuario, el hash que devuelve el servidor de reconocmiento como respuesta
                 *
                 *
                 *  por el momento uso mi usuario 436
                 *  y copiare la logica del hash
                 *  1E32F967CF6744526A617AFEC2064FE9
                 *  1e32f967cf6744526a617afec2064fe9
                 *
                 *
                 *
                 */


                String idUser = "436";
                String hashUser ="1e32f967cf6744526a617afec2064fe9";

                try {
                    listName = URLEncoder.encode(listName,"UTF-8");
                    listId = URLEncoder.encode(listId,"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


                String debug = "http://10.10.25.3/serviciosFacrec/wsmarcacion.php?id="+idUser+"&hash="+hashUser+"&listaMaterias="+listName+"&listaIDMaterias="+listId+"&tipo=E";

                Log.i("RESPUESTA DE string get", debug);

                try {
                    jsonresponse = new ComRequest.get().execute(debug).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();

                    //Log.i("error",e.getMessage());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    //Log.i("error",e.getMessage());
                }
                Log.i("RESPUESTA DE JSON", jsonresponse);

            }







        });


        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        param.setMargins(10,10,10,10);


        for (Subjects s: p.materias ) {
            CheckBox cb_subject =  new CheckBox(getContext());
            checkBoxList.add(cb_subject);
            ll_materias_container.addView(cb_subject,param);
            cb_subject.setText(s.nombre);
            cb_subject.setTag(s.id);
            cb_subject.setTextSize(40);
            cb_subject.setPadding(10,10,10,10);

            cb_subject.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                    if(arg1){
                        arg0.setBackgroundColor(Color.GREEN);
                    }else{
                        arg0.setBackgroundColor(0);
                    }
                }
            });

        }

        return v;


    }

    // TODO: Rename method, update argument and hook method into UI event
    /*public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

   /* @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    /*@Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }*/

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
   /* public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/



    /*public static class DirectJSONTask extends AsyncTask<String,Integer, String> {


        protected String doInBackground(String... urls) {

            String jsonString="";

            try {
                URL url = new URL(urls[0]);


                BufferedReader in = new BufferedReader(new InputStreamReader( url.openStream())); //problem here

                String inputLine;

                while ((inputLine = in.readLine()) != null) {

                    jsonString = inputLine;

                }

                in.close();

                Log.d("json string", "String is : "+jsonString);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return jsonString;
        }

        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

        protected void onPostExecute(Long result) {
            //  showDialog("Downloaded " + result + " bytes");
        }
    }*/
}
