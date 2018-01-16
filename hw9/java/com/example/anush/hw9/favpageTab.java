package com.example.anush.hw9;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.http.SslCertificate;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.anush.hw9.View.Adapter.FavListAdapter;
import com.example.anush.hw9.View.Adapter.ListAdapter;
import com.example.anush.hw9.model.ResultsData;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.R.attr.data;
import static android.R.attr.name;
import static com.example.anush.hw9.R.id.container;


/**
 * Created by anush on 4/24/2017.
 */

public class favpageTab extends Fragment
{
    ListView listView;
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.pagetab, container, false);
        SharedPreferences sharedPrefPage=getActivity().getSharedPreferences("pagedetails", Context.MODE_PRIVATE);

        Button prev = (Button)rootView.findViewById(R.id.previous);
        Button next = (Button) rootView.findViewById(R.id.next);
        prev.setVisibility(View.INVISIBLE);
        next.setVisibility(View.INVISIBLE);

        listView = (ListView) rootView.findViewById(R.id.list_view);
        List<ResultsData> pageData = new ArrayList<ResultsData>();
        ResultsData mData = new ResultsData();
        String name="";
        String url="";
        String id="";
        Map<String,?> keys = sharedPrefPage.getAll();
        for(Map.Entry<String,?> entry : keys.entrySet())
        {
            String []values=entry.getValue().toString().split(",");
            name=values[0];
            url=values[1];
            id=entry.getKey();
            mData.setName(name);
            mData.setUrl(url);
            mData.setId(id);
            pageData.add(mData);
        }
        FavListAdapter adapter = new FavListAdapter(pageData,getContext(),"page");
        listView.setAdapter(adapter);
        return rootView;
    }
}
