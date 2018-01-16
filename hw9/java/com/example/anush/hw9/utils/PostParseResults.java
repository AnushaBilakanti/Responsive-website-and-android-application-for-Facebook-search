package com.example.anush.hw9.utils;

import com.example.anush.hw9.model.PostResultsData;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import android.support.v4.app.Fragment;

import static com.example.anush.hw9.utils.Constants.KEY_VALUE;

/**
 * Created by anush on 4/20/2017.
 */

public class PostParseResults
{

    public static List<PostResultsData> getPostResultData(String result) {
        List<PostResultsData> postData = new ArrayList<PostResultsData>();
        if(result == null){
            return postData;
        }
        try {

            JSONObject jsonobj = new JSONObject(result);
            JSONObject jsonobj1 = jsonobj.getJSONObject("posts");
            JSONArray data = jsonobj1.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                PostResultsData mData = new PostResultsData();
                JSONObject dataObject = (JSONObject) data.get(i);
                String time = dataObject.getString("created_time");
                mData.setDate(time);
                String msg = dataObject.getString("message");
                mData.setMsg(msg);
                postData.add(mData);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return postData;
    }


}
