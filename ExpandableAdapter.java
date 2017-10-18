package com.example.yuzetong.myfb;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yuzetong on 4/26/17.
 */

public class ExpandableAdapter extends BaseExpandableListAdapter
{
    Context context;
    ArrayList<String> titleArray;
    HashMap<String, ArrayList<String>> albumArray;

    public ExpandableAdapter(Context context, ArrayList<String> titleArray, HashMap<String, ArrayList<String>> albumArray)
    {
        this.context = context;
        this.titleArray = titleArray;
        this.albumArray = albumArray;
    }

    @Override
    public int getGroupCount() {
        return titleArray.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return albumArray.get(titleArray.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return titleArray.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return albumArray.get(titleArray.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String title = (String)this.getGroup(groupPosition);
        if(convertView == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater)this.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.title, null);
        }
        TextView tv = (TextView)convertView.findViewById(R.id.groupt);
        tv.setText(title);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        String picture = (String)this.getChild(groupPosition,childPosition);
        if(convertView == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater)this.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.child, null);
        }
        ImageView iv = (ImageView)convertView.findViewById(R.id.photo1);
        Picasso.with(context).load(picture).into(iv);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
