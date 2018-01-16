package com.example.anush.hw9;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
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
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.LikeView;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static com.example.anush.hw9.utils.Constants.ID_VALUE;
import static com.example.anush.hw9.utils.Constants.NAME_VALUE;
import static com.example.anush.hw9.utils.Constants.TYPE_VALUE;
import static com.example.anush.hw9.utils.Constants.URL_VALUE;

public class DetailActivity extends AppCompatActivity
{


    private CallbackManager mCallbackManager;
    private ShareDialog mShareDialog;
    private LoginManager manager;
    String shared_id;//this id is saved in ID_VALUE and will be used in albums and posts
    String shared_name;
    String shared_url;
    String shared_type;

    char ch_type;
    TabLayout tabLayout;

    //Shared Preferences

    public SharedPreferences sharedPrefUser;
    public SharedPreferences sharedPrefPage;
    public SharedPreferences sharedPrefEvent;
    public SharedPreferences sharedPrefPlace;
    public SharedPreferences sharedPrefGroup;
//    getSharedPreferences("userdetails", Context.MODE_PRIVATE);//only this appln can access shared preferences
//    SharedPreferences sharedPrefPage=getSharedPreferences("pagedetails", Context.MODE_PRIVATE);//only this appln can access shared preferences
//    SharedPreferences sharedPrefEvent=getSharedPreferences("eventdetails", Context.MODE_PRIVATE);//only this appln can access shared preferences
//    SharedPreferences sharedPrefPlace=getSharedPreferences("placedetails", Context.MODE_PRIVATE);//only this appln can access shared preferences
//    SharedPreferences sharedPrefGroup=getSharedPreferences("groupdetails", Context.MODE_PRIVATE);//only this appln can access shared preferences


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

        ResultsData eachData = new ResultsData();


        if(getIntent()!=null){
            shared_name = getIntent().getStringExtra("name");
            shared_url=  getIntent().getStringExtra("url");
            shared_id=  getIntent().getStringExtra("id");
            shared_type= getIntent().getStringExtra("type");


            Log.i("shared_type",shared_type);
            ch_type = shared_type.charAt(0);

//            Log.d("ch_type",ch_type);
            System.out.print("\"ch_type\",ch_type"+ch_type);


            eachData.setName(shared_name);
            eachData.setUrl(shared_url);
            eachData.setId(shared_id);
            //we have got all the data here.. like you got the search string in the resulty activity. Now create a tabbed layout
            //and make expandable lsit view
            Log.i("DetailActivity name",shared_name);
//            Log.i("DetailActivity id",shared_id);
            Log.i("DetailActivity url",shared_url);

            String myUrl = "http://cs-server.usc.edu:20679/callinghw9.php?section=albumpost&id=";


            myUrl=myUrl+shared_id.trim();
            Log.i("URL in Detail",myUrl);
        }


        setContentView(R.layout.activity_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent resultintent = getIntent();

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        //adding icons to sweepable tabs
//        int[] tabIcons = {
//                R.mipmap.userimg,
//                R.mipmap.pagesimg,
//                R.mipmap.eventsimg,
//                R.mipmap.placesimg,
//                R.mipmap.groupsimg,
//        };
//        tabLayout.getTabAt(0).setIcon(R.mipmap.albumsimg);
//        tabLayout.getTabAt(1).setIcon(R.mipmap.postsimg);



        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("Albums");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.albumsimg, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText("Posts");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.postsimg, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

       /* tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        tabLayout.getTabAt(4).setIcon(tabIcons[4]);*/



        int tab_position = tabLayout.getSelectedTabPosition();
        Log.d("TAG", "tab_positionanu" + tab_position);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setupViewPager(ViewPager viewPager)
    {

        ViewPagerAdapter adapter1 = new ViewPagerAdapter(getSupportFragmentManager());
        adapter1.addFrag(new albumTab(), "Albums");
        adapter1.addFrag(new postTab(), "Posts");
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());

        Bundle bundle = new Bundle();
        bundle.putString(ID_VALUE,shared_id);
        bundle.putString(NAME_VALUE,shared_name);//contains the name of the selected row, will be used in post
        bundle.putString(URL_VALUE,shared_url);//contains the url of the selected row, img will be used in post
        bundle.putString(TYPE_VALUE,shared_type);
        //good pass this to yoy albumsfragment as a bundle argumentS
        // ian not understanding ur idea
        //how did u parse posts
        //in the same can u parrse name and url??? but name and url is wt v get from results it will not be tat call used ofr albums and posts
        //i have shown u what i am using.but its not tat right
        //one sec.its the name and img of the row tat u clicked in the results table
        albumTab malbumTab = new albumTab();
        malbumTab.setArguments(bundle);
        adapter.addFragment(malbumTab,"Albums");

        postTab mpostTab = new postTab();
        mpostTab.setArguments(bundle);
        adapter.addFragment(mpostTab, "Posts");

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

        sharedPrefUser=getSharedPreferences("userdetails", Context.MODE_PRIVATE);
        sharedPrefPage=getSharedPreferences("pagedetails", Context.MODE_PRIVATE);
        sharedPrefEvent=getSharedPreferences("eventdetails", Context.MODE_PRIVATE);
        sharedPrefPlace=getSharedPreferences("placedetails", Context.MODE_PRIVATE);
        sharedPrefGroup=getSharedPreferences("groupdetails", Context.MODE_PRIVATE);

//        if(sharedPrefUser.getString(shared_id, "")!=""||sharedPrefPage.getString(shared_id, "")!=""||sharedPrefEvent.getString(shared_id, "")!=""||sharedPrefPlace.getString(shared_id, "")!=""||sharedPrefGroup.getString(shared_id, "")!="")
//        {

        Log.i("sh",sharedPrefUser.getString(shared_id, ""));
        String fav="";
        int tab_position = tabLayout.getSelectedTabPosition();

        switch(ch_type)
        {
            case 'u':fav=sharedPrefUser.getString(shared_id, "");
                    break;
            case 'p':
                fav=sharedPrefPage.getString(shared_id, "");
                break;
            case 'e':
                fav=sharedPrefEvent.getString(shared_id, "");
                break;
            case 'l':
                fav=sharedPrefPlace.getString(shared_id, "");
                break;
            case 'g':
                fav=sharedPrefGroup.getString(shared_id, "");
                break;
        }
        if(fav.equalsIgnoreCase(""))
        {
            getMenuInflater().inflate(R.menu.menu_details, menu);
            return true;

        }
        else
        {
            getMenuInflater().inflate(R.menu.menu_details_remove, menu);
            return true;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();

        List<String> valuesSharedPref = new ArrayList<String>();
        valuesSharedPref.add(shared_name);
        valuesSharedPref.add(shared_url);

        StringBuilder csvList = new StringBuilder();
        for(String s : valuesSharedPref)
        {
            csvList.append(s);
            csvList.append(",");
        }
//        Log.d("TAG", "tab_position" + id);
        //noinspection SimplifiableIfStatement

//        String tab_position = tabLayout.getSelectedTabPosition();
        Log.d("TAG", "tab_position" + shared_type);

        if (item.getItemId()==R.id.detail_fav)
        {
            //write logic---create shared preference
            Toast.makeText(DetailActivity.this,"Added to Favorites!",Toast.LENGTH_SHORT).show();
            switch (ch_type)
            {
                case 'u':
                    SharedPreferences.Editor editor=sharedPrefUser.edit();
                    editor.putString(shared_id,csvList.toString());
                    editor.commit();
                    break;
                case 'p':
                    SharedPreferences.Editor editor1=sharedPrefPage.edit();
                    editor1.putString(shared_id,csvList.toString());
                    editor1.apply();
                    break;
                case 'e':
                    SharedPreferences.Editor editor2=sharedPrefEvent.edit();
                    editor2.putString(shared_id,csvList.toString());
                    editor2.apply();
                    break;
                case 'l':
                    SharedPreferences.Editor editor3=sharedPrefPlace.edit();
                    editor3.putString(shared_id,csvList.toString());
                    editor3.apply();
                    break;
                case 'g':
                    SharedPreferences.Editor editor4=sharedPrefGroup.edit();
                    editor4.putString(shared_id,csvList.toString());
                    editor4.apply();
                    break;
            }
            return true;
        }

        if (item.getItemId()== R.id.detail_share)
        {
            //write logic
            Log.i("Sahre","Share");
            mShareDialog=new ShareDialog(this);
            mCallbackManager=CallbackManager.Factory.create();
            mShareDialog.registerCallback(mCallbackManager,new FacebookCallback<Sharer.Result>()
            {

                @Override
                public void onSuccess(Sharer.Result result)
                {
                    Toast.makeText(DetailActivity.this,"You shared this post",Toast.LENGTH_LONG).show();
                    Log.d("Hello","success"+result.getPostId());

                }

                @Override
                public void onCancel()
                {
                    Toast.makeText(DetailActivity.this,"Sharing Cancelled",Toast.LENGTH_LONG).show();

                }

                @Override
                public void onError(FacebookException error)
                {
                    Toast.makeText(DetailActivity.this,"Error while sharing",Toast.LENGTH_LONG).show();

                }
            });
            Log.i("PIC URL",shared_url);
            Log.i("SHARED NAME",shared_name);
           /* if (ShareDialog.canShow(ShareLinkContent.class)) {*/
                ShareLinkContent linkContent = new ShareLinkContent.Builder().
                        setContentUrl(Uri.parse(shared_url))
                       .setImageUrl(Uri.parse(shared_url))
                        .setContentTitle(shared_name)
                        .setContentDescription("FB SEARCH FROM USC CSCI571...").build();
                mShareDialog.show(linkContent);

           /* }*/



            /*FacebookSdk.sdkInitialize(getApplicationContext());
            callbackManager=CallbackManager.Factory.create();
            List<String>permissionNeeds= Arrays.asList("publish_actions");
            manager=LoginManager.getInstance();
            manager.logInWithPublishPermissions(this,permissionNeeds);
            manager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult)
                {
                    publishImage();
                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onError(FacebookException error) {

                }
            });

            return true;*/

        }
        if (item.getItemId()== R.id.detail_fav_remove)
        {
            //write logic---create shared preference
            Toast.makeText(DetailActivity.this,"Removed from Favorites!",Toast.LENGTH_SHORT).show();

            Map<String,?> keys = sharedPrefPage.getAll();
            for(Map.Entry<String,?> entry : keys.entrySet()){
                Log.d("map values",entry.getKey() + ": " + entry.getValue().toString());
            }

            switch (ch_type)
            {

                case 'u':
                    SharedPreferences.Editor editor=sharedPrefUser.edit();
                    editor.remove(shared_id);
                    editor.apply();
                    break;
                case 'p':
                    SharedPreferences.Editor editor1=sharedPrefPage.edit();
                    editor1.remove(shared_id);
                    editor1.apply();
                    break;
                case 'e':
                    SharedPreferences.Editor editor2=sharedPrefEvent.edit();
                    editor2.remove(shared_id);
                    editor2.apply();
                    break;
                case 'l':
                    SharedPreferences.Editor editor3=sharedPrefPlace.edit();
                    editor3.remove(shared_id);
                    editor3.apply();
                    break;
                case 'g':
                    SharedPreferences.Editor editor4=sharedPrefGroup.edit();
                    editor4.putString(shared_id,csvList.toString());
                    editor4.apply();
                    break;
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

   /* private void publishImage()
    {
        Bitmap image= BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);

        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(image)
                .build();

        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();

        ShareApi.share(content,null);
    }*/


   @Override
    protected void onActivityResult(int requestCode,int responseCode,Intent data)
    {
        super.onActivityResult(requestCode,responseCode,data);
        mCallbackManager.onActivityResult(requestCode,responseCode,data);
    }
    /**
     * A placeholder fragment containing a simple view.
     */

    //u wait for a sec

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>(); //to keep track of fragments
        private final List<String> mFragmentTitleList = new ArrayList<>(); //to keep track of fragments

        public SectionsPagerAdapter(FragmentManager fm) {
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
        public int getCount() {
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

