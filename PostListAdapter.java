package com.example.yuzetong.myfb;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


import java.util.ArrayList;



/**
 * Created by yuzetong on 4/26/17.
 */

public class PostListAdapter extends ArrayAdapter<PostObj> {
    Context context;
    ArrayList<PostObj> posts;
    int resource;

    public PostListAdapter(Context context, int resource, ArrayList<PostObj> posts) {
        super(context, resource, posts);

        this.context = context;
        this.resource = resource;
        this.posts = posts;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.postrow, null,true);

        ImageView imageView = (ImageView)rowView.findViewById(R.id.icon);
        TextView textView1 = (TextView)rowView.findViewById(R.id.author);
        TextView textView2 = (TextView)rowView.findViewById(R.id.time);
        TextView textView3 = (TextView)rowView.findViewById(R.id.des);

        PostObj post = posts.get(position);

        String icon = post.getIcon();
        String author = post.getAuthor();
        String time = post.getTime();
        String des = post.getDes();

        Picasso.with(context).load(icon).into(imageView);
        textView1.setText(author);
        textView2.setText(time);
        textView3.setText(des);

        return rowView;
    };
}
