package com.example.investigacion.fourth.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.investigacion.fourth.MainActivity;
import com.example.investigacion.fourth.R;
import com.example.investigacion.fourth.adapter.RecycleViewSuggestions;
import com.example.investigacion.fourth.helper.ClassJSONParser;
import com.example.investigacion.fourth.model.Suggestions;
import com.example.investigacion.fourth.tools.ComRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.concurrent.ExecutionException;


public class DeleteUserFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView query_request;
    private RecyclerView rv_list_employee;
    private Suggestions p;
    private RecycleViewSuggestions mAdapter;
    private String cookie;


    public DeleteUserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DeleteUserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DeleteUserFragment newInstance(String param1, String param2) {
        DeleteUserFragment fragment = new DeleteUserFragment();
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
        try {
            cookie =  new MainActivity.ReturnCookieSession("http://10.10.25.9:8888/users/login","username=admin&password=mandrake2505").execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       





        View v = inflater.inflate(R.layout.fragment_new_user, container, false);

        // CREAR REFERENCIAS GRAFICAS
        query_request =  (TextView) v.findViewById(R.id.et_query_response_employee);
        rv_list_employee = (RecyclerView) v.findViewById(R.id.rv_response_employee);




        if (cookie != null) {
            p = (Suggestions) ClassJSONParser.json2obj("http://10.10.25.9:8888/users/filter", cookie, Suggestions.class);


            query_request.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {


                    p = (Suggestions) ClassJSONParser.json2obj("http://10.10.25.9:8888/users/filter?query=" + editable.toString(), cookie, Suggestions.class);

                    mAdapter.swap(p.suggestions);

                    mAdapter.notifyDataSetChanged();


                }
            });
            // use a linear layout manager
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            rv_list_employee.setLayoutManager(mLayoutManager);

            // specify an adapter (see also next example)
            mAdapter = new RecycleViewSuggestions(
                    p.suggestions,
                    new RecycleViewSuggestions.OnItemClickListener() {
                        @Override
                        public void onItemClick(Suggestions.UserCode item) {
                            Toast.makeText(getContext(), "Item Clicked with the code " + item.data, Toast.LENGTH_LONG).show();
                            //mCallback.openFivePicture(item);
                            confirmation_delete_popup(item);
                        }
                    });
            rv_list_employee.setAdapter(mAdapter);
            rv_list_employee.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        }
        return v;
    }
    

    // TODO: Rename method, update argument and hook method into UI event
   /* public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

    /*@Override
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
    /*public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/


    private void confirmation_delete_popup(final Suggestions.UserCode item){

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
        View mView = getLayoutInflater().inflate(R.layout.confirm_mark,null);
        TextView title_textview = (TextView) mView.findViewById(R.id.confirm_popup_title);
        TextView short_title = (TextView) mView.findViewById(R.id.confirm_popup_short_title);

        Button btn_yes = (Button) mView.findViewById(R.id.confirm_popup_yes);
        Button btn_no = (Button) mView.findViewById(R.id.confirm_popup_no);
        TextView name = (TextView) mView.findViewById(R.id.confirm_popup_name);

        // todo  cambiar la informacion de confirmacion titulo de botones, contenido de textviews title short title
        btn_no.setText("Cancelar");
        btn_yes.setText("Si");
        short_title.setText(R.string.foto_confirm);
        title_textview.setText(R.string.foto_title);
        name.setText(item.value);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.setCancelable(false);

        btn_yes.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {

                //TODO CREAR LA COMUNICACION PARA BORRAR EL USUARIO

                //new DirectJSONTaskPost().execute("http://10.10.25.9:8888/users/del","username="+item.data);

                //TODO NOTIFICAR AL USUARIO

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                View mView =  getLayoutInflater().inflate(R.layout.confirm_mark_message,null);

                Button btn_ok = (Button) mView.findViewById(R.id.confirm_mark_message_ok);
                TextView body_textview =  (TextView) mView.findViewById(R.id.confirm_mark_message_body);
                TextView title_textview = (TextView) mView.findViewById(R.id.confirm_mark_message_title);
                mBuilder.setView(mView);
                final AlertDialog dialog2 = mBuilder.create();
                title_textview.setText(R.string.foto_title);
                body_textview.setText(R.string.foto_message_ok);


                btn_ok.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        dialog2.dismiss();
                    }
                });

                dialog.dismiss();
                dialog2.show();
            }
        });
        btn_no.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
