package com.example.investigacion.fourth;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.investigacion.fourth.model.RssFeedModel;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class News extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private RecyclerView mRecyclerView;
    private EditText mEditText;
    private Button mFetchFeedButton;
    private SwipeRefreshLayout mSwipeLayout;
    private TextView mFeedTitleTextView;
    private TextView mFeedLinkTextView;
    private TextView mFeedDescriptionTextView;

    private List<RssFeedModel> mFeedModelList;
    private String mFeedTitle;
    private String mFeedLink;
    private String mFeedDescription;



    @Override
    public void onUserInteraction()
    {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        new FetchFeedTask().execute((Void) null);

       /* final ViewFlipper viewFlipper = new ViewFlipper(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(30, 30, 30, 30);
        layoutParams.gravity = Gravity.CENTER;
        viewFlipper.setLayoutParams(layoutParams);
        viewFlipper.setFlipInterval(2000);
        viewFlipper.setAutoStart(true);



        for (RssFeedModel item : mFeedModelList ) {
            LinearLayout ll_container =  new LinearLayout(this);
            TextView tv_title =  new TextView(this);
            TextView tv_description = new TextView(this);

            FrameLayout.LayoutParams params =
                    new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(30, 30, 30, 30);
            params.gravity = Gravity.CENTER;


            //TODO CREATE LAYOUTPARAM FOR LAYOUT
            ll_container.setLayoutParams(params);




            //TODO CREATE LAYOUTPARAM FOR TEXT
            tv_title.setLayoutParams(params);
            tv_description.setLayoutParams(params);
            //imageView.setLayoutParams(params);



            tv_title.setText(item.title);
            tv_description.setText(item.description);


            ll_container.addView(tv_title);
            ll_container.addView(tv_description);

            viewFlipper.addView(ll_container);

        }


        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.activity_main);
        if (linearLayout != null) {
            linearLayout.addView(viewFlipper);
        }*/




        /*mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mEditText = (EditText) findViewById(R.id.rssFeedEditText);
        mFetchFeedButton = (Button) findViewById(R.id.fetchFeedButton);
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mFeedTitleTextView = (TextView) findViewById(R.id.feedTitle);
        mFeedDescriptionTextView = (TextView) findViewById(R.id.feedDescription);
        mFeedLinkTextView = (TextView) findViewById(R.id.feedLink);*/





//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


/*

        mFetchFeedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO new FetchFeedTask().execute((Void) null);
            }
        });
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new FetchFeedTask().execute((Void) null);
            }
        });*/
    }













    public List<RssFeedModel> parseFeed(InputStream inputStream) throws XmlPullParserException, IOException {
        String title = null;
        String link = null;
        String description = null;
        boolean isItem = false;
        List<RssFeedModel> items = new ArrayList<>();

        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(inputStream, null);

            xmlPullParser.nextTag();
            while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT) {
                int eventType = xmlPullParser.getEventType();

                String name = xmlPullParser.getName();
                if(name == null)
                    continue;

                if(eventType == XmlPullParser.END_TAG) {
                    if(name.equalsIgnoreCase("item")) {
                        isItem = false;
                    }
                    continue;
                }

                if (eventType == XmlPullParser.START_TAG) {
                    if(name.equalsIgnoreCase("item")) {
                        isItem = true;
                        continue;
                    }
                }

                Log.d("MainActivity", "Parsing name ==> " + name);
                String result = "";
                if (xmlPullParser.next() == XmlPullParser.TEXT) {
                    result = xmlPullParser.getText();
                    xmlPullParser.nextTag();
                }

                if (name.equalsIgnoreCase("title")) {
                    title = result;
                } else if (name.equalsIgnoreCase("link")) {
                    link = result;
                } else if (name.equalsIgnoreCase("description")) {
                    description = result;
                }

                if (title != null && link != null && description != null) {
                    if(isItem) {
                        RssFeedModel item = new RssFeedModel(title, link, description);
                        items.add(item);
                    }
                    else {
                       /* mFeedTitle = title;
                        mFeedLink = link;
                        mFeedDescription = description;*/
                    }

                    title = null;
                    link = null;
                    description = null;
                    isItem = false;
                }
            }

            return items;
        } finally {
            inputStream.close();
        }
    }










    private class FetchFeedTask extends AsyncTask<Void, Void, Boolean> {

        private String urlLink;

        @Override
        protected void onPreExecute() {
           /* mSwipeLayout.setRefreshing(true);
            mFeedTitle = null;
            mFeedLink = null;
            mFeedDescription = null;
            mFeedTitleTextView.setText("Feed Title: " + mFeedTitle);
            mFeedDescriptionTextView.setText("Feed Description: " + mFeedDescription);
            mFeedLinkTextView.setText("Feed Link: " + mFeedLink);*/
            //urlLink = "http://www.abc.com.py/nacionales.xml";
            urlLink = "http://media.ultimahora.com/adjuntos/161/rss/UltimoMomento.xml";
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if (TextUtils.isEmpty(urlLink))
                return false;

            try {
                if(!urlLink.startsWith("http://") && !urlLink.startsWith("https://"))
                    urlLink = "http://" + urlLink;

                URL url = new URL(urlLink);
                InputStream inputStream = url.openConnection().getInputStream();
                Log.e("INPUT", inputStream.toString());
                mFeedModelList = parseFeed(inputStream);
                return true;
            } catch (IOException e) {
                Log.e(TAG, "Error", e);
            } catch (XmlPullParserException e) {
                Log.e(TAG, "Error", e);
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            //mSwipeLayout.setRefreshing(false);

            if (success) {
               /* mFeedTitleTextView.setText("Feed Title: " + mFeedTitle);
                mFeedDescriptionTextView.setText("Feed Description: " + mFeedDescription);
                mFeedLinkTextView.setText("Feed Link: " + mFeedLink);*/
                // Fill RecyclerView
                // mRecyclerView.setAdapter(new RssFeedListAdapter(mFeedModelList));






                final ViewFlipper viewFlipper = new ViewFlipper(getApplicationContext());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                // layoutParams.setMargins(30, 30, 30, 30);
                //layoutParams.gravity = Gravity.CENTER;
                viewFlipper.setLayoutParams(layoutParams);
                viewFlipper.setFlipInterval(2000);
                viewFlipper.setAutoStart(true);



                for (RssFeedModel item : mFeedModelList ) {
                    LinearLayout ll_container =  new LinearLayout(getApplicationContext());
                    TextView tv_title =  new TextView(getApplicationContext());
                    TextView tv_description = new TextView(getApplicationContext());

                    FrameLayout.LayoutParams params =
                            new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    //params.setMargins(30, 30, 30, 30);
                    //params.gravity = Gravity.CENTER;
                    FrameLayout.LayoutParams params_text =
                            new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    //params.setMargins(30, 30, 30, 30);
                    //params.gravity = Gravity.CENTER;




                    //TODO CREATE LAYOUTPARAM FOR LAYOUT
                    ll_container.setLayoutParams(params);
                    ll_container.setOrientation(LinearLayout.VERTICAL);





                    //TODO CREATE LAYOUTPARAM FOR TEXT
                    tv_title.setLayoutParams(params_text);
                    tv_description.setLayoutParams(params_text);
                    tv_description.setTextColor(Color.BLACK);
                    tv_title.setTextColor(Color.BLACK);

                    //imageView.setLayoutParams(params);






                    tv_title.setTextSize(70);
                    tv_description.setTextSize(50);
                    tv_title.setText(item.title);
                    tv_description.setText(Html.fromHtml(item.description));


                    ll_container.addView(tv_title);
                    ll_container.addView(tv_description);

                    viewFlipper.addView(ll_container);

                }


                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.activity_news);
                if (linearLayout != null) {
                    linearLayout.addView(viewFlipper);
                }





            } else {
                Toast.makeText(News.this,
                        "Enter a valid Rss feed url",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}
