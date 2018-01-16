package com.example.anush.hw9.View.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anush.hw9.DetailActivity;
import com.example.anush.hw9.R;
import com.example.anush.hw9.model.ResultsData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anush on 4/20/2017.
 */

public class ListAdapter extends BaseAdapter
{

    List<ResultsData> mData = new ArrayList<ResultsData>();

    Context context;
    String type;

    public ListAdapter(List<ResultsData> mData, Context context,String type)
    {
        this.mData = mData;
        this.context = context;
        this.type=type;
    }

    @Override
    public int getCount()
    {
        return mData.size();
    }

    @Override
    public ResultsData getItem(int position)
    {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        //see for every item in the list this method gets called in the adapter
        //then we inflate a view
        //then we fill the view and return to the list
        View row = convertView;
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.list_item,parent,false);
        }
        ImageView imageView = (ImageView)row.findViewById(R.id.image_in_list);
        TextView mTextView = (TextView)row.findViewById(R.id.text_list_view);
        ImageButton imageButton = (ImageButton)row.findViewById(R.id.next_image);//;the name is??

        final ResultsData eachData = getItem(position);

        if(eachData!=null)
        {
            String imageuRL = eachData.getUrl();
            String name = eachData.getName();
            mTextView.setText(name);
            Picasso.with(context).load(imageuRL).resize(60,60).into(imageView);

        }

        imageButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("name",eachData.getName());
                intent.putExtra("id",eachData.getId());
                intent.putExtra("url",eachData.getUrl());
                intent.putExtra("type",type);
                context.startActivity(intent);
                Log.i("Lname",eachData.getName());
                Log.i("Lid",eachData.getId());
                Log.i("Lurl",eachData.getUrl());
            }
        });
        return row;
    }
}
