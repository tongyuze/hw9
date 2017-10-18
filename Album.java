package com.example.yuzetong.myfb;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yuzetong on 4/25/17.
 */

public class Album extends Fragment
{
    private View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String id = this.getArguments().getString("id");
        String type = this.getArguments().getString("type");
        String link = "http://lowcost-env.s4vmfzggp4.us-west-2.elasticbeanstalk.com/index.php?id="+id;
        rootView = inflater.inflate(R.layout.album, container, false);
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

    private class JsonTask extends AsyncTask<String, Void, String>
    {

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try
            {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuilder sb = new StringBuilder();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    sb.append(line+"\n");
                }
                String res = sb.toString();
                return res;
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            finally
            {
                if (connection != null)
                {
                    connection.disconnect();
                }
                try
                {
                    if (reader != null)
                    {
                        reader.close();
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            JSONObject jb = null;
            try
            {
                jb = new JSONObject(result);
                if(!jb.has("albums") || !(jb.getJSONArray("albums").getJSONObject(0)).has("photos")) {
                    TextView none = (TextView)rootView.findViewById(R.id.nodata);
                    none.setVisibility(View.VISIBLE);
                }
                else {
                    JSONArray albumJArray = jb.getJSONArray("albums");
                    ExpandableListView ev= (ExpandableListView)rootView.findViewById(R.id.albumView);

                    ArrayList<String> titleArray = new ArrayList<String>();
                    HashMap<String,ArrayList<String>> albumArray = new HashMap<String,ArrayList<String>>();

                    for(int i=0; i<albumJArray.length(); i++)
                    {
                        JSONObject temp = albumJArray.getJSONObject(i);
                        ArrayList<String> temp2 = new ArrayList<String>();
                        String albumname = temp.getString("name");
                        titleArray.add(albumname);
                        if(temp.has("photos"))
                        {
                            for(int j=0; j<temp.getJSONArray("photos").length(); j++)
                            {
                                temp2.add(temp.getJSONArray("photos").getJSONObject(j).getString("picture"));
                            }
                        }
                        albumArray.put(titleArray.get(i), temp2);
                    }
                    ExpandableAdapter adapter = new ExpandableAdapter(getActivity().getBaseContext(), titleArray, albumArray);
                    ev.setAdapter(adapter);
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
                TextView none = (TextView)rootView.findViewById(R.id.nodata);
                none.setVisibility(View.VISIBLE);
            }
        }
    }
}
