package com.example.yuzetong.myfb;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FavoritesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private FavoritesActivity.SectionsPagerAdapter mSectionsPagerAdapter;

    private Userfav tab1 = new Userfav();
    private Pagefav tab2 = new Pagefav();
    private Eventfav tab3 = new Eventfav();
    private Placefav tab4 = new Placefav();
    private Groupfav tab5 = new Groupfav();

    private int[] menuicon = {
            R.drawable.users,
            R.drawable.pages,
            R.drawable.events,
            R.drawable.places,
            R.drawable.groups
    };
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new FavoritesActivity.SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /*getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });*/
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent i = new Intent(FavoritesActivity.this, MainActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_favorite) {
            Intent i = new Intent(FavoritesActivity.this, FavoritesActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_about) {
            Intent i = new Intent(FavoritesActivity.this, AboutActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_results, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        /*int id = item.getItemId();

        if(id == android.R.id.home)
        {
            finish();
        }*/

        return super.onOptionsItemSelected(item);
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
                case 2:
                    return tab3;
                case 3:
                    return tab4;
                case 4:
                    return tab5;
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 5 total pages.
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    Drawable image = getResources().getDrawable(menuicon[0]);
                    image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
                    SpannableString sb = new SpannableString(" \n"+"Users");
                    ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
                    sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    return sb;
                case 1:
                    Drawable image2 = getResources().getDrawable(menuicon[1]);
                    image2.setBounds(0, 0, image2.getIntrinsicWidth(), image2.getIntrinsicHeight());
                    SpannableString sb2 = new SpannableString(" \n"+"Pages");
                    ImageSpan imageSpan2 = new ImageSpan(image2, ImageSpan.ALIGN_BOTTOM);
                    sb2.setSpan(imageSpan2, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    return sb2;
                case 2:
                    Drawable image3 = getResources().getDrawable(menuicon[2]);
                    image3.setBounds(0, 0, image3.getIntrinsicWidth(), image3.getIntrinsicHeight());
                    SpannableString sb3 = new SpannableString(" \n"+"Events");
                    ImageSpan imageSpan3 = new ImageSpan(image3, ImageSpan.ALIGN_BOTTOM);
                    sb3.setSpan(imageSpan3, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    return sb3;
                case 3:
                    Drawable image4 = getResources().getDrawable(menuicon[3]);
                    image4.setBounds(0, 0, image4.getIntrinsicWidth(), image4.getIntrinsicHeight());
                    SpannableString sb4 = new SpannableString(" \n"+"Places");
                    ImageSpan imageSpan4 = new ImageSpan(image4, ImageSpan.ALIGN_BOTTOM);
                    sb4.setSpan(imageSpan4, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    return sb4;
                case 4:
                    Drawable image5 = getResources().getDrawable(menuicon[4]);
                    image5.setBounds(0, 0, image5.getIntrinsicWidth(), image5.getIntrinsicHeight());
                    SpannableString sb5 = new SpannableString(" \n"+"Groups");
                    ImageSpan imageSpan5 = new ImageSpan(image5, ImageSpan.ALIGN_BOTTOM);
                    sb5.setSpan(imageSpan5, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    return sb5;
            }
            return null;
        }
    }
}
