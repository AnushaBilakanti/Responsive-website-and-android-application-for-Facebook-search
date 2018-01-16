package com.example.anush.hw9.View.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anush.hw9.DetailActivity;
import com.example.anush.hw9.R;
import com.example.anush.hw9.model.PostResultsData;
import com.example.anush.hw9.model.ResultsData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import static com.example.anush.hw9.utils.Constants.NAME_VALUE;
import static com.example.anush.hw9.utils.Constants.URL_VALUE;

/**
 * Created by anush on 4/20/2017.
 */

public class PostListAdapter extends BaseAdapter {

    List<PostResultsData> mData = new ArrayList<PostResultsData>();

    Context context;

    String name = "";

    String url = "";


    public PostListAdapter(List<PostResultsData> mData, Context context) {
        this.mData = mData;
        this.context = context;
    }


    //somehow call this constructor by adding name and url

    public PostListAdapter(List<PostResultsData> mData, Context context, String name, String url) {
        this.mData = mData;
        this.context = context;
        this.name = name;
        this.url = url;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public PostResultsData getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //see for every item in the list this method gets called in the adapter
        //then we inflate a view
        //then we fill the view and return to the list
        View row = convertView;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.post_list_item,parent,false);
        }
        ImageView imageView = (ImageView)row.findViewById(R.id.post_image);
        TextView m1TextView = (TextView)row.findViewById(R.id.post_title);
        TextView m2TextView = (TextView)row.findViewById(R.id.post_date);
        TextView m3TextView = (TextView)row.findViewById(R.id.post_msg);

        final PostResultsData eachData = getItem(position);

        if(eachData!=null){
            String postimageuRL = url;
            String posttitle= name;
            //date why did u remove it
            //even that is required
            String postdate=eachData.getDate();
            String postmsg = eachData.getMsg();

            m1TextView.setText(name);
            m2TextView.setText(postdate); //check what is the mistake here
            m3TextView.setText(postmsg);
            Picasso.with(context).load(postimageuRL).resize(60,60).into(imageView);

        }
        return row;
    }
}
