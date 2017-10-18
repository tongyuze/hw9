package com.example.yuzetong.myfb;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.support.v4.view.PagerAdapter.POSITION_NONE;

public class CustomFavListAdapter extends ArrayAdapter<JSONObject>
{
    Context context;
    ArrayList<JSONObject> items;
    int resource;

    public CustomFavListAdapter(Context context, int resource, ArrayList<JSONObject> items) {
        super(context, resource, items);

        this.context = context;
        this.resource = resource;
        this.items = items;
    }

    @Override
    public int getPosition(JSONObject item)
    {
        return -1;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row, null,true);

        ImageView imageView = (ImageView)rowView.findViewById(R.id.icon);
        TextView textView = (TextView)rowView.findViewById(R.id.itemname);
        ImageView star = (ImageView)rowView.findViewById(R.id.star);
        ImageView details = (ImageView)rowView.findViewById(R.id.right);

        JSONObject item = items.get(position);
        String id = null;

        try {
            String src = item.getString("icon");
            Picasso.with(context).load(src).into(imageView);
            String name = item.getString("name");
            id = item.getString("id");
            textView.setText(name);
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences("favor", 0);
        if(sharedPreferences.contains(id)) {
            star.setImageResource(R.mipmap.favon);
        }
        else
        {
            star.setImageResource(R.mipmap.ic_launcher);
        }
        details.setImageResource(R.mipmap.det);
        return rowView;
    };
}
