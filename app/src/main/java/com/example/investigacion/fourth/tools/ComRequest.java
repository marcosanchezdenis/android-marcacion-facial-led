package com.example.investigacion.fourth.tools;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class ComRequest {
    public static class get extends AsyncTask<String,Integer, String> {


        protected String doInBackground(String... urls) {

            String responsetxt = null;
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                // conn.setDoOutput(true);

                //conn.setRequestMethod("POST");
                conn.setRequestProperty("Cookie", "userid=\"2|1:0|10:1530135750|6:userid|4:MQ==|9f33b80bb688cc0230a8c43dcf212c89df9c18997b284e49d20727afcf89f28b\"; user=\"2|1:0|10:1530138580|4:user|12:TVNBTkNIRVo=|3eda4e2e45a55956416a77c3dae956b6b72b4663ae6ff115648b96fbabebe1cd\"; name=\"2|1:0|10:1530138580|4:name|20:TWFyY28gU2FuY2hleg==|7c59add4e8ed9f0acd94374af68ba7f86ab2a344026ffc8a5bf0a92faec3c5f8\"");


                //OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                //writer.write("");
                //writer.close();

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
                    Log.i("ComRequest>>get>> ",response.toString());
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

            return responsetxt;
        }

        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

        protected void onPostExecute(Long result) {
            //  showDialog("Downloaded " + result + " bytes");
        }
    }

    public static class post extends AsyncTask<String,Integer, String> {


        protected String doInBackground(String... urls) {

            String responsetxt = null;
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                // conn.setDoOutput(true);

                conn.setRequestMethod("POST");
                conn.setRequestProperty("Cookie", "userid=\"2|1:0|10:1530135750|6:userid|4:MQ==|9f33b80bb688cc0230a8c43dcf212c89df9c18997b284e49d20727afcf89f28b\"; user=\"2|1:0|10:1530138580|4:user|12:TVNBTkNIRVo=|3eda4e2e45a55956416a77c3dae956b6b72b4663ae6ff115648b96fbabebe1cd\"; name=\"2|1:0|10:1530138580|4:name|20:TWFyY28gU2FuY2hleg==|7c59add4e8ed9f0acd94374af68ba7f86ab2a344026ffc8a5bf0a92faec3c5f8\"");


                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(urls[1]);
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
                    Log.i("ComRequest>>post>> ",response.toString());
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

            return responsetxt;
        }

        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

        protected void onPostExecute(Long result) {
            //  showDialog("Downloaded " + result + " bytes");
        }
    }
}
