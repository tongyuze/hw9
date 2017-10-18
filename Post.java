package com.example.yuzetong.myfb;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by yuzetong on 4/25/17.
 */

public class Post extends Fragment
{
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.post, container, false);
        String id = this.getArguments().getString("id");
        String type = this.getArguments().getString("type");
        String link = "http://lowcost-env.s4vmfzggp4.us-west-2.elasticbeanstalk.com/index.php?id="+id;
        rootView = inflater.inflate(R.layout.post, container, false);
        if(type.equals("event"))
        {
            TextView none = (TextView)rootView.findViewById(R.id.nodata);
            none.setVisibility(View.VISIBLE);
        }
        else {
            new JsonTask().execute(link);
        }
        return rootView;
    }

    private class JsonTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuilder sb = new StringBuilder();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String res = sb.toString();
                return res;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            JSONObject jb = null;
            try {
                jb = new JSONObject(result);
                if (!jb.has("posts")) {
                    TextView none = (TextView) rootView.findViewById(R.id.nodata);
                    none.setVisibility(View.VISIBLE);
                } else {
                    JSONArray postJArray = jb.getJSONArray("posts");
                    String icon = jb.getJSONObject("picture").getString("url");
                    String author = jb.getString("name");
                    ArrayList<PostObj> postArray = new ArrayList<PostObj>();
                    for(int i=0; i<postJArray.length(); i++)
                    {
                        JSONObject temp = postJArray.getJSONObject(i);
                        if(!temp.isNull("message")) {
                            String time = temp.getJSONObject("created_time").getString("date");
                            time = time.substring(0,time.length()-7);
                            /*SimpleDateFormat formatBefore = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                            SimpleDateFormat formatAfter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date = formatBefore.parse(time);
                            String newTime = formatAfter.format(date);*/
                            String des = temp.getString("message");
                            postArray.add(new PostObj(icon, author, time, des));
                        }
                    }
                    ListView list = (ListView)rootView.findViewById(R.id.postView);
                    PostListAdapter adapter = new PostListAdapter(getActivity().getBaseContext(), android.R.layout.simple_list_item_1, postArray);
                    list.setAdapter(adapter);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
