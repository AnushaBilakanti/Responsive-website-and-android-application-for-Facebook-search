package com.example.anush.hw9;

import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.anush.hw9.HttpRequest.HttpGetRequest;
import com.example.anush.hw9.HttpRequest.getResultData;
import com.example.anush.hw9.View.Adapter.ListAdapter;
import com.example.anush.hw9.model.ResultsData;
import com.example.anush.hw9.utils.Constants;
import com.example.anush.hw9.utils.ParseResults;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.anush.hw9.utils.Constants.KEY_VALUE;


/**
 * Created by anush on 4/19/2017.
 */

public class pageTab extends Fragment {

    String queryString = "";

    List<ResultsData> pageData = new ArrayList<ResultsData>();

    ListView listView;
    int begin=0;
    int finish=10;

    Button prev;
    Button next;

    String url = "http://sample-env.wyksxmx3zv.us-west-2.elasticbeanstalk.com/index.php?type=page&section=disp&name=";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            queryString = getArguments().getString(KEY_VALUE); //this is query string we have hardcode now
        }

//        Log.i("Adarsh from fragment" , queryString);
        url = url + queryString.trim();

    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.pagetab, container, false);
        findIds(rootView);
        prev.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                begin-=10;
                finish-=10;
                if(begin==0)
                    prev.setVisibility(View.INVISIBLE);
                next.setVisibility(View.VISIBLE);
                fun();

            }
        });
        next.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                begin+=10;
                finish+=10;
                if(begin>10)
                    next.setVisibility(View.INVISIBLE);
                else
                    next.setVisibility(View.VISIBLE);
                prev.setVisibility(View.VISIBLE);
                fun();

            }
        });
        return rootView;
    }

    private void findIds(View rootView) {
        listView = (ListView) rootView.findViewById(R.id.list_view);
        prev= (Button)rootView.findViewById(R.id.previous);
        prev.setVisibility(View.INVISIBLE);
        next= (Button)rootView.findViewById(R.id.next);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        if (queryString.length() != 0)
        {
            fun();
        }
    }

    public void fun()
    {
        HttpGetRequest getRequest = new HttpGetRequest();
        String result = getResultData.getHttpResultString(getRequest, url);
        pageData = ParseResults.getResultData(result, begin, finish);
        UpdateUI();
    }


    private void UpdateUI() {
        //we have to make an adapter here and attach it to listview here
        ListAdapter adapter = new ListAdapter(pageData,getContext(),"page");
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
    }

}
//did you add buttons in xmls?