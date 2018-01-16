package com.example.anush.hw9.utils;

import com.example.anush.hw9.model.ResultsData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anush on 4/20/2017.
 */

public class ParseResults {


    public static List<ResultsData> getResultData(String result,int begin, int finish) {
        List<ResultsData> usersData = new ArrayList<ResultsData>();

        if(result == null){
            return usersData;
        }
        try {
            JSONObject jsonobj = new JSONObject(result);
            JSONArray data = jsonobj.getJSONArray("data");
            for (int i = begin; i < finish; i++) {
                ResultsData mData = new ResultsData();
                JSONObject dataObject = (JSONObject) data.get(i);
                String name = dataObject.getString("name");
                mData.setName(name);
                String id = dataObject.getString("id");
                mData.setId(id);
                JSONObject picture = dataObject.getJSONObject("picture");
                JSONObject data_picture = picture.getJSONObject("data");
                String url = data_picture.getString("url");
                mData.setUrl(url);
                usersData.add(mData);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return usersData;
    }


}
