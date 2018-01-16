package com.example.anush.hw9.View.Adapter;

/**
 * Created by anush on 4/22/2017.
 */

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anush.hw9.R;
import com.squareup.picasso.Picasso;

public class MyExListAdapter extends BaseExpandableListAdapter
{

    Context context;
    List<String> albumnames;
    Map<String, List<String>>images;

    public MyExListAdapter(Context context, List<String> albumnames, Map<String, List<String>> images)
    {
        this.context = context;
        this.albumnames = albumnames;
        this.images = images;
    }

    @Override
    public int getGroupCount()
    {
        return albumnames.size();
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        if(albumnames!=null)
        {
            return images.get(albumnames.get(groupPosition)).size();
        }
        return -1;
    }

    @Override
    public Object getGroup(int groupPosition) //returns group name
    {
        return albumnames.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        return images.get(albumnames.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition)
    {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return childPosition;
    }

    @Override
    public boolean hasStableIds()
    {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {//what you see is bcuz of getGroupView & getChildView
        String albumtitle=(String)getGroup(groupPosition);

        if(convertView==null)
        {
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.list_parent,null);

        }
        TextView txtparent=(TextView)convertView.findViewById(R.id.txtParent);
        txtparent.setText(albumtitle);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {
        String albumimage=(String)getChild(groupPosition,childPosition);
        if(convertView==null)
        {
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            //CREATE A SEPARATE XML WITH IMAGEVIEW
            //USE THAT XML INSTAEAD OF ALBUM TAB
            convertView=inflater.inflate(R.layout.album_list,null);

        }

        //WHY ARE SETTING TEXT TO IMAGE VIEW
        //I HAD USED PICASSO IN THE LIST ADAPTER
        //DO IT LIE THAT...k
        ImageView imageView = (ImageView)convertView.findViewById(R.id.albumimg);
        //how to get url? i hv not passed it here..it happens in tat grouplistcrceate
        Picasso.with(context).load(albumimage).into(imageView);
        //what does ur map contain???key is albumname- value is two images
        //then get that na


        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return true;
    }
}
