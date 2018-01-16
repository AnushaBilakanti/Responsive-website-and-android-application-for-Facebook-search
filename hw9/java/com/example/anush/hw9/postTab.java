package com.example.anush.hw9;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.anush.hw9.HttpRequest.HttpGetRequest;
import com.example.anush.hw9.HttpRequest.getResultData;
import com.example.anush.hw9.View.Adapter.ListAdapter;
import com.example.anush.hw9.View.Adapter.PostListAdapter;
import com.example.anush.hw9.model.PostResultsData;
import com.example.anush.hw9.model.ResultsData;
import com.example.anush.hw9.utils.Constants;
import com.example.anush.hw9.utils.ParseResults;
import com.example.anush.hw9.utils.PostParseResults;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.anush.hw9.utils.Constants.ID_VALUE;
import static com.example.anush.hw9.utils.Constants.KEY_VALUE;
import static com.example.anush.hw9.utils.Constants.NAME_VALUE;
import static com.example.anush.hw9.utils.Constants.TYPE_VALUE;
import static com.example.anush.hw9.utils.Constants.URL_VALUE;


/**
 * Created by anush on 4/19/2017.
 */

public class postTab extends Fragment
{
    private JSONObject jsonobj;
    private JSONObject jsonobj1;
    private JSONArray data;
    String result="";

    String queryString = "";

    List<PostResultsData> postData = new ArrayList<PostResultsData>();

    ListView listView;

    String url = "http://cs-server.usc.edu:20679/callinghw9.php?section=albumpost&id=";

    String name = "";

    String imageUrl  = "";

    View rootView=null;
    String type="";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        //get those variables here like query_serach
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            queryString = getArguments().getString(ID_VALUE); //this is query string we have hardcode now
            name =  getArguments().getString(NAME_VALUE);
            imageUrl =  getArguments().getString(URL_VALUE);
            type=getArguments().getString(TYPE_VALUE);
        }

//        Log.i("Adarsh from fragment" , queryString);
        url = url + queryString.trim();

    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.posttab, container, false);
        findIds(rootView);
        return rootView;
    }

    private void findIds(View rootView)
    {
        listView = (ListView) rootView.findViewById(R.id.post_list_view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        jsonobj=null;
        jsonobj1=null;
        data=null;
        super.onActivityCreated(savedInstanceState);
        if (queryString.length() != 0)
        {
            Log.i("type",type);
            if(type.equalsIgnoreCase("page")||type.equalsIgnoreCase("lplace"))
            {
                HttpGetRequest getRequest = new HttpGetRequest();
                result = getResultData.getHttpResultString(getRequest, url);
                try {
                    if (result != null && result.length() != 0) {
                        Log.i("ResultPost", result);
                        jsonobj = new JSONObject(result);
                        if (jsonobj.optJSONObject("posts") != null) {
                            jsonobj1 = jsonobj.getJSONObject("posts");
                            if (jsonobj1 != null && jsonobj1.length() != 0) {
                                data = jsonobj1.getJSONArray("data");
                                if (data.length() != 0 && jsonobj1 != null && data != null) {
                                    postData = PostParseResults.getPostResultData(result);
                                    UpdateUI();
                                }
                            }
                        } else {
                            RelativeLayout parent = (RelativeLayout) rootView.findViewById(R.id.posttabparent);
                            TextView noPageText = new TextView(getContext());
                            noPageText.setText("No Posts available to display!");
                            noPageText.setTypeface(null, Typeface.BOLD);
                            noPageText.setTextSize(24.0f);
                            noPageText.setTextColor(Color.BLACK);
                            RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                            textParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                            parent.addView(noPageText, textParams);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                RelativeLayout parent = (RelativeLayout) rootView.findViewById(R.id.posttabparent);
                TextView noPageText = new TextView(getContext());
                noPageText.setText("No Posts available to display!");
                noPageText.setTypeface(null, Typeface.BOLD);
                noPageText.setTextSize(24.0f);
                noPageText.setTextColor(Color.BLACK);
                RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                textParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                parent.addView(noPageText, textParams);

            }


        }
    }

    private void UpdateUI() {
        //we have to make an adapter here and attach it to listview here
        //PostListAdapter adapter = new PostListAdapter(postData,getContext());

        //parse and get name and url and and populate the name and url
        //name=
        PostListAdapter adapter = new PostListAdapter(postData,getContext(),name,imageUrl);
        listView.setAdapter(adapter);
    }

}
