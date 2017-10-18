package com.example.yuzetong.myfb;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

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

/**
 * Created by yuzetong on 4/23/17.
 */

public class Group extends Fragment {

    private View rootView;
    private String nextPage;
    private String prevPage;
    private CustomListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        String str = this.getArguments().getString("Group_json");
        System.out.println(str);
        JSONObject groupObject = null;
        JSONArray groupJArray = null;
        final ArrayList<JSONObject> groupArray = new ArrayList<JSONObject>();
        try
        {
            groupObject = new JSONObject(str);
            groupJArray = groupObject.getJSONArray("data");
            for(int i=0; i<groupJArray.length(); i++)
            {
                JSONObject temp = groupJArray.getJSONObject(i);
                groupArray.add(temp);
            }
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }

        rootView = inflater.inflate(R.layout.group, container, false);
        ListView list = (ListView)rootView.findViewById(R.id.listView1);
        Button next = (Button)rootView.findViewById(R.id.next);
        Button prev = (Button)rootView.findViewById(R.id.previous);
        adapter = new CustomListAdapter(getActivity().getBaseContext(), android.R.layout.simple_list_item_1, groupArray);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                try {
                    intent.putExtra("id", groupArray.get(position).getString("id"));
                    intent.putExtra("name", groupArray.get(position).getString("name"));
                    intent.putExtra("icon", groupArray.get(position).getJSONObject("picture").getJSONObject("data").getString("url"));
                    intent.putExtra("type", "group");
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            if(groupObject.has("paging")) {
                if (!groupObject.getJSONObject("paging").isNull("next")) {
                    nextPage = groupObject.getJSONObject("paging").getString("next");
                }
                if (!groupObject.getJSONObject("paging").isNull("previous")) {
                    prevPage = groupObject.getJSONObject("paging").getString("previous");
                }
            }

            if(nextPage == null)
            {
                next.setTextColor(getResources().getColor(R.color.none));
                next.setClickable(false);
            }
            else
            {
                next.setTextColor(getResources().getColor(R.color.black));
                next.setClickable(true);
                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PagingTask pt = new PagingTask();
                        pt.execute(nextPage);
                    }
                });
            }

            if(prevPage == null)
            {
                prev.setTextColor(getResources().getColor(R.color.none));
                prev.setClickable(false);
            }
            else
            {
                prev.setTextColor(getResources().getColor(R.color.black));
                prev.setClickable(true);
                prev.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PagingTask pt = new PagingTask();
                        pt.execute(prevPage);
                    }
                });
            }
        }
        catch(JSONException e) {
            e.printStackTrace();
        }
        return rootView;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    public class PagingTask extends AsyncTask<String, Void, String> {
        private String type;
        ArrayList<JSONObject> newArray = new ArrayList<JSONObject>();

        @Override
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
            JSONObject newObject = null;
            JSONArray newJArray = null;
            try
            {
                newObject = new JSONObject(result);
                newJArray = newObject.getJSONArray("data");
                for(int i=0; i<newJArray.length(); i++)
                {
                    JSONObject temp = newJArray.getJSONObject(i);
                    newArray.add(temp);
                }
            }
            catch(JSONException e)
            {
                e.printStackTrace();
            }
            ListView list = (ListView)rootView.findViewById(R.id.listView1);
            Button next = (Button)rootView.findViewById(R.id.next);
            Button prev = (Button)rootView.findViewById(R.id.previous);
            CustomListAdapter adapter = new CustomListAdapter(getActivity().getBaseContext(), android.R.layout.simple_list_item_1, newArray);
            list.setAdapter(adapter);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity(), DetailsActivity.class);
                    try {
                        intent.putExtra("id", newArray.get(position).getString("id"));
                        intent.putExtra("name", newArray.get(position).getString("name"));
                        intent.putExtra("icon", newArray.get(position).getJSONObject("picture").getJSONObject("data").getString("url"));
                        intent.putExtra("type", "group");
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            try {
                if(!newObject.getJSONObject("paging").isNull("next")) {
                    nextPage = newObject.getJSONObject("paging").getString("next");
                }
                else
                {
                    nextPage = null;
                }
                if(!newObject.getJSONObject("paging").isNull("previous")) {
                    prevPage = newObject.getJSONObject("paging").getString("previous");
                }
                else
                {
                    prevPage = null;
                }

                if(nextPage == null)
                {
                    next.setTextColor(getResources().getColor(R.color.none));
                    next.setClickable(false);
                }
                else
                {
                    next.setTextColor(getResources().getColor(R.color.black));
                    next.setClickable(true);
                    next.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PagingTask pt = new PagingTask();
                            pt.execute(nextPage);
                        }
                    });
                }

                if(prevPage == null)
                {
                    prev.setTextColor(getResources().getColor(R.color.none));
                    prev.setClickable(false);
                }
                else
                {
                    prev.setTextColor(getResources().getColor(R.color.black));
                    prev.setClickable(true);
                    prev.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PagingTask pt = new PagingTask();
                            pt.execute(prevPage);
                        }
                    });
                }
            }
            catch(JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
