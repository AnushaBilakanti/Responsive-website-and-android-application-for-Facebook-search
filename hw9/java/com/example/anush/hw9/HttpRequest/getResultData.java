package com.example.anush.hw9.HttpRequest;

import java.util.concurrent.ExecutionException;

/**
 * Created by anush on 4/20/2017.
 */

public class getResultData {

    public static String getHttpResultString(HttpGetRequest getRequest, String url){

        String result = null;
        try {
            result = getRequest.execute(url).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return result;

    }

}
