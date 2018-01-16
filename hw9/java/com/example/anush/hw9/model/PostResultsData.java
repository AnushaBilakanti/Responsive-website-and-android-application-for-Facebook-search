package com.example.anush.hw9.model;

/**
 * Created by anush on 4/20/2017.
 */

public class PostResultsData
{

    String name;

    String id;

    String url;

    String created_time;

    String msg;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDate(){return created_time;}

    public void setDate(String created_time){this.created_time=created_time;}

    public String getMsg(){return  msg;}

    public void setMsg(String msg){this.msg=msg;}

}
//wait... what is this model??
//this is an object where i am going to store data\cat we create it in ResultActivity itself?
//forget it , just see..ask if you don't understand