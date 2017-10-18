package com.example.yuzetong.myfb;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by yuzetong on 4/27/17.
 */

public class Userfav extends Fragment{
    private View rootView;
    private CustomFavListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("favor", 0);
        Map<String, ?> map = sharedPreferences.getAll();
        ArrayList<JSONObject> favArray = new ArrayList<JSONObject>();
        Iterator entries = map.entrySet().iterator();
        while (entries.hasNext())
        {
            Map.Entry entry = (Map.Entry) entries.next();
            String value = (String)entry.getValue();
            try {
                JSONObject jb = new JSONObject(value);
                favArray.add(jb);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(!favArray.isEmpty()) {
            final ArrayList<JSONObject> userArray = new ArrayList<JSONObject>();
            for (int i = 0; i < favArray.size(); i++) {
                try {
                    JSONObject temp = favArray.get(i);
                    if (temp.getString("type").equals("user")) {
                        userArray.add(temp);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            rootView = inflater.inflate(R.layout.userfav, container, false);
            if (!userArray.isEmpty()) {
                ListView list = (ListView) rootView.findViewById(R.id.listView1);
                adapter = new CustomFavListAdapter(getActivity().getBaseContext(), android.R.layout.simple_list_item_1, userArray);
                list.setAdapter(adapter);


                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getActivity(), DetailsActivity.class);
                        try {
                            intent.putExtra("id", userArray.get(position).getString("id"));
                            intent.putExtra("name", userArray.get(position).getString("name"));
                            intent.putExtra("icon", userArray.get(position).getString("icon"));
                            intent.putExtra("type", "user");
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
        return rootView;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if(adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}
