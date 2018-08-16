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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ComRequest {
    public static class get extends AsyncTask<String,Integer, String> {


        protected String doInBackground(String... urls) {

            String responsetxt = null;
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(1000);
                // conn.setDoOutput(true);

                //conn.setRequestMethod("POST");
                if(urls[1] != null){
                    conn.setRequestProperty("Cookie", urls[1]);
                }



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
                Log.i("ComRequest.get error p",e.getMessage());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                Log.i("ComRequest.get error u",e.getMessage());
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.i("ComRequest.get error m",e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("ComRequest.get error i",e.getMessage());
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
                conn.setConnectTimeout(1000);
                conn.setDoOutput(true);

                conn.setRequestMethod("POST");
                if(urls[1]!=null) {
                    conn.setRequestProperty("Cookie", urls[1]);
                }

                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(urls[2]);
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

                Map<String, List<String>> maps= conn.getHeaderFields();
                if(maps.get("Set-Cookie")!= null ) {
                    List<String> coolist = maps.get("Set-Cookie");

                    Iterator<String> it = coolist.iterator();
                    StringBuffer sbu = new StringBuffer();
                    String[] splited;
                    while (it.hasNext()) {
                        splited = it.next().split(";");
                        sbu.append(splited[0] + " ");
                    }
                    String cookies = sbu.toString();
                    Log.i("Cookies-comRequest-post", cookies);
                    return cookies;
                }
                return responsetxt;

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
