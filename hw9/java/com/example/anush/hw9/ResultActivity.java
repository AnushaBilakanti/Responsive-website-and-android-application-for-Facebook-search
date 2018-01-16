package com.example.anush.hw9;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anush.hw9.HttpRequest.getResultData;
import com.example.anush.hw9.model.ResultsData;
import com.example.anush.hw9.utils.ParseResults;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.example.anush.hw9.utils.Constants.KEY_VALUE;

public class ResultActivity extends AppCompatActivity {


    Button btndetails;
    Button btnfavorite;
    View postView;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.s
     */
    private ViewPager mViewPager;
    ArrayList<HashMap<String, String>> contactList;
    String keyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        //used for backbutton in toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent resultintent = getIntent();
        keyword = resultintent.getStringExtra("search_string");
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        //adding icons to sweepable tabs
//        int[] tabIcons = {
//                R.mipmap.userimg,
//                R.mipmap.pagesimg,
//                R.mipmap.eventsimg,
//                R.mipmap.placesimg,
//                R.mipmap.groupsimg,
//        };
//        tabLayout.getTabAt(0).setIcon(R.mipmap.userimg);
//        tabLayout.getTabAt(1).setIcon(R.mipmap.pagesimg);
//        tabLayout.getTabAt(2).setIcon(R.mipmap.eventsimg);
//        tabLayout.getTabAt(3).setIcon(R.mipmap.placesimg);
//        tabLayout.getTabAt(4).setIcon(R.mipmap.groupsimg);


        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("Users");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.userimg, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText("Pages");
//        tabTwo.setTextSize(20);
//        tabTwo.setTextAlignment(1);
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.pagesimg, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText("Events");
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.eventsimg, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);

        TextView tabFour = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabFour.setText("Places");
        tabFour.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.placesimg, 0, 0);
        tabLayout.getTabAt(3).setCustomView(tabFour);

        TextView tabFive = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabFive.setText("Groups");
        tabFive.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.groupsimg, 0, 0);
        tabLayout.getTabAt(4).setCustomView(tabFive);

        /*tabLayout.getTabAt(0).setIcon(tabIcons[0]);
         tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        tabLayout.getTabAt(4).setIcon(tabIcons[4]);*/


        int tab_position = tabLayout.getSelectedTabPosition();


        String myUrl = "http://sample-env.wyksxmx3zv.us-west-2.elasticbeanstalk.com/index.php?name=usc&type=user&section=disp";



    }



    //used for backbutton in toolbar
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    //used for backbutton in toolbar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setupViewPager(ViewPager viewPager)
    {

        ViewPagerAdapter adapter1 = new ViewPagerAdapter(getSupportFragmentManager());
        adapter1.addFrag(new userTab(), "Users");
        adapter1.addFrag(new pageTab(), "Pages");
        adapter1.addFrag(new placeTab(), "Places");
        adapter1.addFrag(new eventTab(), "Events");
        adapter1.addFrag(new groupTab(), "Groups");

        viewPager.setAdapter(adapter1);
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());

        Bundle bundle = new Bundle();
        bundle.putString(KEY_VALUE,keyword);

        userTab mUserTab = new userTab();
        mUserTab.setArguments(bundle);
        adapter.addFragment(mUserTab, "Users");

        pageTab mPageTab = new pageTab();
        mPageTab.setArguments(bundle);
        adapter.addFragment(mPageTab,"Pages");

//        //do for the same for below tabs
        placeTab mplaceTab = new placeTab();
        mplaceTab.setArguments(bundle);
        adapter.addFragment(mplaceTab,"Places");

        eventTab meventTab = new eventTab();
        meventTab.setArguments(bundle);
        adapter.addFragment(meventTab, "Events");

        groupTab mgroupTab = new groupTab();
        mgroupTab.setArguments(bundle);
        adapter.addFragment(mgroupTab, "Groups");

        //u removed this from here..that was the reason for error
        viewPager.setAdapter(adapter);
    }

    //ok... why no images for groups??
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_result, menu);
//        return true;
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Log.d("TAG", "tab_position" + id);
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */

    //u wait for a sec

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter
    {

        private final List<Fragment> mFragmentList = new ArrayList<>(); //to keep track of fragments
        private final List<String> mFragmentTitleList = new ArrayList<>(); //to keep track of fragments

        public SectionsPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }


        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return mFragmentList.get(position);
        }

        @Override
        public int getCount()
        {
            // Show 3 total pages.
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);

        }
    }
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}

//wait
