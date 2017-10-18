package com.example.yuzetong.myfb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.telecom.Call;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

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

public class DetailsActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private SharedPreferences sharedpreferences;
    private ShareDialog shareDialog;
    private CallbackManager callbackManager;

    private String id, name, icon, type;

    private Album tab1 = new Album();
    private Post tab2 = new Post();

    private int[] menuicon = {
            R.mipmap.albumsnew,
            R.mipmap.postsnew
    };
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        sharedpreferences = getSharedPreferences("favor", 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FacebookSdk.sdkInitialize(getApplicationContext());
        this.shareDialog = new ShareDialog(this);
        this.callbackManager = CallbackManager.Factory.create();
        this.shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {

            public void onSuccess(Sharer.Result paramAnonymousResult){
                Toast.makeText(DetailsActivity.this,"You shared this post",Toast.LENGTH_SHORT).show();
            }

            public void onCancel(){
                Toast.makeText(DetailsActivity.this,"You cancelled to share this post",Toast.LENGTH_SHORT).show();
            }

            public void onError(FacebookException paramAnonymousFacebookException){
                Toast.makeText(DetailsActivity.this,"Facebook share error",Toast.LENGTH_SHORT).show();
            }

        });

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        Bundle b = getIntent().getExtras();
        id = b.getString("id");
        name = b.getString("name");
        icon = b.getString("icon");
        type = b.getString("type");

        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("type", type);
        tab1.setArguments(bundle);
        tab2.setArguments(bundle);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mSectionsPagerAdapter.notifyDataSetChanged();
    }

    /*@Override
    protected void onRestart()
    {
        super.onRestart();
        finish();
    }*/

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        MenuItem fav = menu.findItem(R.id.fav);
        if(sharedpreferences.contains(id))
        {
            fav.setTitle("Remove from Favorites");
        }
        else
        {
            fav.setTitle("Add to Favorites");
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int menuid = item.getItemId();
        SharedPreferences.Editor editor = sharedpreferences.edit();
        /*if(menuid == android.R.id.home)
        {
            finish();
        }*/

        if (menuid == R.id.fav)
        {
            if(!sharedpreferences.contains(id))
            {
                HashMap<String, String> map = new HashMap<String,String>();
                map.put("id", id);
                map.put("name", name);
                map.put("icon", icon);
                map.put("type", type);
                JSONObject favJson = new JSONObject(map);
                editor.putString(id, favJson.toString());
                editor.commit();
                Toast.makeText(DetailsActivity.this,"Added to Favorites!", Toast.LENGTH_LONG).show();
            }
            else
            {
                editor.remove(id);
                editor.commit();
                Toast.makeText(DetailsActivity.this,"Removed from Favorites!", Toast.LENGTH_LONG).show();
            }
        }

        if(menuid == R.id.share) {
            Toast.makeText(this, "Sharing " + name, Toast.LENGTH_SHORT).show();
            String url = "http://www.facebook.com/" + id;

            if (ShareDialog.canShow(ShareLinkContent.class)) {
                ShareLinkContent localShareLinkContent = ((ShareLinkContent.Builder) new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse(url)))
                        .setContentTitle(name)
                        .setImageUrl(Uri.parse(icon))
                        .setContentDescription("SEARCH FB FROM USC CSCI571")
                        .build();
                this.shareDialog.show(localShareLinkContent, ShareDialog.Mode.AUTOMATIC);

            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return tab1;
                case 1:
                    return tab2;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    Drawable image = getResources().getDrawable(menuicon[0]);
                    image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
                    SpannableString sb = new SpannableString(" \n"+"Albums");
                    ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
                    sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    return sb;
                case 1:
                    Drawable image2 = getResources().getDrawable(menuicon[1]);
                    image2.setBounds(0, 0, image2.getIntrinsicWidth(), image2.getIntrinsicHeight());
                    SpannableString sb2 = new SpannableString(" \n"+"Posts");
                    ImageSpan imageSpan2 = new ImageSpan(image2, ImageSpan.ALIGN_BOTTOM);
                    sb2.setSpan(imageSpan2, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    return sb2;
            }
            return null;
        }
    }

    /*private class JsonTask extends AsyncTask<String, Void, String>
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
            super.onPostExecute(result);
                JSONObject jb = new JSONObject(result);
                JSONArray albumJson = jb.getJSONObject("data").getJSONArray("albums");
                JSONArray postJson = jb.getJSONObject("data").getJSONArray("posts");
                JSONObject detailJson = jb.getJSONObject("data");
                Album tabe1 = new Album();
                Bundle bundle = new Bundle();
                bundle.putString("Detail_json", result);
                tab1.setArguments(bundle);

                Bundle bundle2 = new Bundle();
                bundle2.putString("Detail_json", result);
                tab2.setArguments(bundle2);

            }
    }*/
}
