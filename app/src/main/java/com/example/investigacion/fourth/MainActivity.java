package com.example.investigacion.fourth;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.investigacion.fourth.Fragment.CameraFragment;
import com.example.investigacion.fourth.Fragment.DeleteUserFragment;
import com.example.investigacion.fourth.Fragment.ExtraFragment;
import com.example.investigacion.fourth.Fragment.ExtraPictureFragment;
import com.example.investigacion.fourth.Fragment.NewUserFragment;
import com.example.investigacion.fourth.Fragment.NewUserPictureFragment;
import com.example.investigacion.fourth.Fragment.SubjectFragment;
import com.example.investigacion.fourth.helper.ClassJSONParser;
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
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, CameraFragment.OnHeadlineSelectedListener,NewUserFragment.OnHeadlineSelectedListener,ExtraFragment.OnHeadlineSelectedListener{


    Handler handler;
    Runnable r;
    private String cookie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // TODO INICIAR SECCION PARA EXTRAER LA COOKIE
       /* try {
            cookie = new ReturnCookieSession("http://10.10.25.9:8080/users/login","username=admin&password=mandrake2505").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
     Log.i("Cookie", cookie);
*/


        openMarkFragment();



        //TODO SCREENSAVER DE NOTICIAS
        //TIMER FOR IDLE STATE
        /*
        handler = new Handler();
        r = new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), News.class);
                startActivity(intent);
            }
        };
        startHandler();*/



    }


    /*@Override
    public void onUserInteraction() {
        // TODO Auto-generated method stub
        super.onUserInteraction();
        stopHandler();//stop first and then start
        startHandler();
    }
    public void stopHandler() {
        handler.removeCallbacks(r);


    }
    public void startHandler() {
        handler.postDelayed(r, 15*1000); //for 5 minutes
    }*/



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.menu_mark) {
            // Handle the camera action
            openMarkFragment();
        } else if (id == R.id.menu_take_picture) {
            openNewUserFragment();
        } else if (id == R.id.menu_delete_users) {
            openDeleteUserFragment();
        } else if (id == R.id.menu_test_profesores) {
            openSubjectFragment();
        } else if (id == R.id.menu_extra_picture) {
            openExtraFragment();
        } /*else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    public void openTakePictureFragment(){

        NewUserPictureFragment newFragment = new NewUserPictureFragment();
        /*Bundle args = new Bundle();
        newFragment.setArguments(args);*/
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    public void openTakePictureFragment(Suggestions.UserCode person){
        NewUserPictureFragment newFragment = new NewUserPictureFragment();
        newFragment.person = person;
        /*Bundle args = new Bundle();
        newFragment.setArguments(args);*/
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }



    public void openMarkFragment(){
        CameraFragment firstFragment = new CameraFragment();
        firstFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, firstFragment).commit();
    }


    public void openExtraFragment(){
        ExtraFragment firstFragment = new ExtraFragment();
        firstFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, firstFragment).commit();
    }

    public void openExtraPictureFragment(Suggestions.UserCode person){
        ExtraPictureFragment firstFragment = new ExtraPictureFragment();
        firstFragment.person = person;
        firstFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, firstFragment).commit();
    }


    public void openDeleteUserFragment(){
        DeleteUserFragment firstFragment = new DeleteUserFragment();
        firstFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, firstFragment).commit();
    }



    public void openNewUserFragment(){
        NewUserFragment firstFragment = new NewUserFragment();
        firstFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, firstFragment).commit();
    }


    public void openSubjectFragment() {


        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        View v = getLayoutInflater().inflate(R.layout.fragment_subject, null);

        final ArrayList<CheckBox> checkBoxList = new ArrayList<>();
        final ArrayList<String> idContainer = new ArrayList<>();
        final ArrayList<String> nameContainer = new ArrayList<>();




        SubjectsResponse p = (SubjectsResponse) ClassJSONParser.json2obj("http://10.10.25.3/serviciosFacrec/materias2.php", SubjectsResponse.class);

        LinearLayout ll_materias_container = (LinearLayout) v.findViewById(R.id.ll_materias_container);
        Button bt_continue = (Button) v.findViewById(R.id.bt_subject_continue);
        bt_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!checkBoxList.isEmpty()) {
                    for (CheckBox cb : checkBoxList) {
                        if (cb.isChecked()) {
                            idContainer.add((String) cb.getTag());
                            nameContainer.add(cb.getText().toString());
                        }
                    }
                } else {
                    //mensaje de error
                }


                StringBuilder nameBuilder = new StringBuilder();
                StringBuilder idBuilder = new StringBuilder();
                for (String id : idContainer) {
                    idBuilder.append(id);
                    idBuilder.append(',');
                }
                for (String name : nameContainer) {
                    nameBuilder.append(name);
                    nameBuilder.append(',');
                }
                String listName = nameBuilder.toString();
                String listId = idBuilder.toString();

                listName = listName.substring(0, listName.length() - 1);
                listId = listId.substring(0, listId.length() - 1);

                Log.i("list-subject", listName);
                Log.i("list-subject-id", listId);


                String jsonresponse = null;
            }
        });


        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        param.setMargins(10,10,10,10);

        for (Subjects s: p.materias ) {
            CheckBox cb_subject =  new CheckBox(this);
            checkBoxList.add(cb_subject);
            ll_materias_container.addView(cb_subject,param);
            cb_subject.setText(s.nombre);
            cb_subject.setTag(s.id);
            cb_subject.setTextSize(31);
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

        mBuilder.setView(v);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();



    }




    public void onArticleSelected(final FaceResponse person){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.confirm_mark,null);
        Button btn_yes = (Button) mView.findViewById(R.id.confirm_popup_yes);
        Button btn_no = (Button) mView.findViewById(R.id.confirm_popup_no);
        TextView name = (TextView) mView.findViewById(R.id.confirm_popup_name);
        name.setText(person.face.name);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.setCancelable(false);

        btn_yes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                View mView =  getLayoutInflater().inflate(R.layout.confirm_mark_type,null);
                Button btn_in = (Button) mView.findViewById(R.id.confirm_popup_in);
                Button btn_out =  (Button) mView.findViewById(R.id.confirm_popup_out);
                Button btn_cancel = (Button) mView.findViewById(R.id.confirm_popup_cancel);
                mBuilder.setView(mView);
                final AlertDialog dialog2 = mBuilder.create();
                btn_in.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            doMark(person, "E");
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        dialog2.dismiss();
                    }
                });
                btn_out.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            doMark(person,"S");
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        dialog2.dismiss();
                    }
                });
                btn_cancel.setOnClickListener(new View.OnClickListener() {

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

    private void doMark(FaceResponse person, String s) throws ExecutionException, InterruptedException {
       String state = new ComRequest.get().execute("http://10.10.25.1/personal/wsmarcacion.php?id=" + person.face.username  + "&hash=" + person.face.hash + "&tipo="+s).get();
       Log.i("STATE-MARK-REQUEST",state);
       if(state.equals("success")){
           AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
           View mView =  getLayoutInflater().inflate(R.layout.confirm_mark_message,null);
           TextView t_in = (Button) mView.findViewById(R.id.confirm_popup_in);
           TextView btn_out =  (Button) mView.findViewById(R.id.confirm_popup_out);
           mBuilder.setView(mView);
           final AlertDialog dialog2 = mBuilder.create();
           dialog2.setCancelable(false);
       }
    }





    @Override
    public void openFivePicture(Suggestions.UserCode person) {
        openTakePictureFragment(person);

    }

    @Override
    public void openExtraPicture(Suggestions.UserCode item) {
        openExtraPictureFragment(item);
    }


    private class ReturnCookieSession extends AsyncTask<Object,Integer, String> {

        String url;
        String postData;


        public ReturnCookieSession(Object... param) {
            this.url = (String) param[0];
            this.postData = (String) param[1];

        }

        protected String doInBackground(Object... urls) {
            Log.i("EVENT", "AsyncTask post");
            String responsetxt = null;
            try {
                URL url = new URL(this.url);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                // conn.setDoOutput(true);

                conn.setRequestMethod("POST");


                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(this.postData);
                writer.close();

                int respCode = conn.getResponseCode();  // New items get NOT_FOUND on PUT


                if (respCode == HttpURLConnection.HTTP_OK || respCode == HttpURLConnection.HTTP_NOT_FOUND) {
                    //req.setAttribute("error", "");
                    /*StringBuffer response = new StringBuffer();
                    String line;

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                    Log.i("Request with modify",response.toString());*/
                    responsetxt = conn.getHeaderField("Cookie");
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


            // Log.i("PhotoOkHttpClient", r1.body().toString());
            return responsetxt;


        }
    }




}