package com.example.investigacion.fourth.Fragment;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExtraFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExtraFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView query_request;
    private RecyclerView rv_list_employee;
    private LinearLayoutManager mLayoutManager;
    private RecycleViewSuggestions mAdapter;
    private Suggestions p  = new Suggestions();
    private OnHeadlineSelectedListener mCallback;
    private String cookie;


    public ExtraFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewUserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExtraFragment newInstance(String param1, String param2) {
        ExtraFragment fragment = new ExtraFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_new_user, container, false);

        // CREAR REFERENCIAS GRAFICAS
        query_request =  (TextView) v.findViewById(R.id.et_query_response_employee);
        rv_list_employee = (RecyclerView) v.findViewById(R.id.rv_response_employee);



        p = (Suggestions) ClassJSONParser.json2obj("http://10.10.25.9:8888/users/filter",cookie, Suggestions.class);




         query_request.addTextChangedListener(new TextWatcher() {
             @Override
             public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

             }

             @Override
             public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

             }

             @Override
             public void afterTextChanged(Editable editable) {


                 p = (Suggestions) ClassJSONParser.json2obj("http://10.10.25.9:8888/users/filter?query="+ editable.toString(),cookie, Suggestions.class);

                 mAdapter.swap(p.suggestions);

                 mAdapter.notifyDataSetChanged();





                 //crear la consulta al post que trae los nombres

                    //actualizar el recycleview

             }
         });
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        rv_list_employee.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new RecycleViewSuggestions(
                p.suggestions,
                new RecycleViewSuggestions.OnItemClickListener() {
                @Override public void onItemClick(Suggestions.UserCode item) {
                    Toast.makeText(getContext(), "Item Clicked with the code "+item.data, Toast.LENGTH_LONG).show();
                    mCallback.openExtraPicture(item);
                }
        });
        rv_list_employee.setAdapter(mAdapter);
        rv_list_employee.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        return v;
    }




    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (ExtraFragment.OnHeadlineSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }







    public interface OnHeadlineSelectedListener {

        void openExtraPicture(Suggestions.UserCode item);
    }






}
