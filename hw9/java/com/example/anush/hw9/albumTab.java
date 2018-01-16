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
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anush.hw9.HttpRequest.HttpGetRequest;
import com.example.anush.hw9.HttpRequest.getResultData;
import com.example.anush.hw9.View.Adapter.ListAdapter;
import com.example.anush.hw9.View.Adapter.MyExListAdapter;
import com.example.anush.hw9.model.ResultsData;
import com.example.anush.hw9.utils.Constants;
import com.example.anush.hw9.utils.ParseResults;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.anush.hw9.utils.Constants.ID_VALUE;
import static com.example.anush.hw9.utils.Constants.KEY_VALUE;
import static com.example.anush.hw9.utils.Constants.TYPE_VALUE;


/**
 * Created by anush on 4/19/2017.
 */
//but al
public class albumTab extends Fragment
{


    private JSONObject jsonobj;
    private JSONObject jsonobj1;
    private JSONArray data;
    String result="";

    String queryString = "";
    List<String> albumnames = new ArrayList<String>();//will contain all the album names-lyk Timeline photos,etc
    //    List<String> childList;
    Map<String, List<String>> images = null;//string is album name, where are u filling this???
    ExpandableListView expandableListView;
    ExpandableListAdapter listAdapter;

    List<ResultsData> usersData = new ArrayList<ResultsData>();

    ListView listView;

    String type="";
    String url = "http://sample-env.wyksxmx3zv.us-west-2.elasticbeanstalk.com/index.php?section=albumpost&id=";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            queryString = getArguments().getString(ID_VALUE); //this is query string we have hardcode now
            type=getArguments().getString(TYPE_VALUE);
        }

//        Log.i("Adarsh from fragment" , queryString);
        url = url + queryString.trim();



    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View rootView=null;
        rootView = inflater.inflate(R.layout.albumtab, container, false);
        if(type.equalsIgnoreCase("page")||type.equalsIgnoreCase("lplace"))
        {
            HttpGetRequest getRequest = new HttpGetRequest();
            result = getResultData.getHttpResultString(getRequest, url);

            try {
                if (result != null && result.length() != 0) {
                    jsonobj = new JSONObject(result);
                    Log.d("result", result);
                    if (jsonobj.optJSONObject("albums") != null) {
                        jsonobj1 = jsonobj.optJSONObject("albums");
                        if (jsonobj1 != null && jsonobj1.length() != 0) {
                            data = jsonobj1.getJSONArray("data");
                            if (data.length() != 0 && jsonobj1 != null && data != null) {
                                Log.d("data.length", String.valueOf(data.length()));
                                rootView = inflater.inflate(R.layout.albumtab, container, false);
                                findIds(rootView);
                            } else {

                                Log.d("data.length", String.valueOf(data.length()));
                                RelativeLayout parent = (RelativeLayout) rootView.findViewById(R.id.albumtabparent);
                                TextView noPageText = new TextView(getContext());
                                noPageText.setText("No Albums available to display!");
                                noPageText.setTypeface(null, Typeface.BOLD);
                                noPageText.setTextSize(24.0f);
                                noPageText.setTextColor(Color.BLACK);
                                RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                textParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                                parent.addView(noPageText, textParams);
                                return parent;
                            }
                        }


                    } else {
                        RelativeLayout parent = (RelativeLayout) rootView.findViewById(R.id.albumtabparent);
                        TextView noPageText = new TextView(getContext());
                        noPageText.setText("No Albums available to display!");
                        noPageText.setTypeface(null, Typeface.BOLD);
                        noPageText.setTextSize(24.0f);
                        noPageText.setTextColor(Color.BLACK);
                        RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                        textParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                        parent.addView(noPageText, textParams);
                        return parent;
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            RelativeLayout parent = (RelativeLayout) rootView.findViewById(R.id.albumtabparent);
            TextView noPageText = new TextView(getContext());
            noPageText.setText("No Albums available to display!");
            noPageText.setTypeface(null, Typeface.BOLD);
            noPageText.setTextSize(24.0f);
            noPageText.setTextColor(Color.BLACK);
            RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            textParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            parent.addView(noPageText, textParams);
            return parent;
        }
        return rootView;
    }

    private void findIds(View rootView) {
        listView = (ListView) rootView.findViewById(R.id.list_view);
        expandableListView = (ExpandableListView)rootView.findViewById(R.id.expandableListView);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        //if (queryString.length() != 0)
       // {
//            HttpGetRequest getRequest = new HttpGetRequest();
//            String result = getResultData.getHttpResultString(getRequest, url);
//            usersData = ParseResults.getResultData(result); //but here the parsing is done differently right? cant use ParseResults.getResultData
            if(data!=null)
            {
                createGroupList();//run

                UpdateUI();
            }

      //  }
    }

    private void UpdateUI()
    { //i created an expandalble list adater
        //use the adapter instead of list adapterok
        //we have to make an adapter here and attach it to listview here
        MyExListAdapter listAdapter;
        listAdapter=new MyExListAdapter(getActivity(),albumnames,images);//passing the context

        //expandableListView.setAdapter(listAdapter);
       // ListAdapter adapter = new ListAdapter(usersData,getContext());
        expandableListView.setAdapter(listAdapter);
        //put a vreakpoint and check.ok
    }

    //where is this called--this is used to get json array and get the reuiqred valesu
    private void createGroupList()
    {
        albumnames = new ArrayList<String>();
        images = new HashMap<>();

        String albumName="";
        try
        {
            if(data!=null)
            {
                if (data.length()!=0)
                {
//                JSONObject jsonobj = new JSONObject(result);
//                JSONObject jsonobj1 = jsonobj.getJSONObject("albums");
//                JSONArray data = jsonobj1.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject dataObject = (JSONObject) data.get(i);
                        albumName = dataObject.getString("name");

                        albumnames.add(albumName);

                        List<String> imagenames = new ArrayList<String>();

                        if(dataObject!=null)
                        {
                            if (dataObject.getJSONObject("photos") != null) {
                                JSONArray photosArray = dataObject.getJSONObject("photos").getJSONArray("data");
                                for (int j = 0; j < photosArray.length(); j++) {
                                    JSONArray images = photosArray.getJSONObject(j).getJSONArray("images");
                                    String imgsrc = images.getJSONObject(0).getString("source");
                                    imagenames.add(imgsrc);
                                }
                            }
                            images.put(albumName, imagenames);
                        }

                    }
                }
                else
                {
                    //images.put(albumName, new ArrayList<String>());
                    Toast.makeText(getContext(),"No Albums available to display",Toast.LENGTH_LONG).show();
                }
            }

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
