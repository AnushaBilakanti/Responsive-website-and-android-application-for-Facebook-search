package com.example.anush.hw9;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by anush on 4/18/2017.
 */

public class HomeFragment extends Fragment {
    View myView;
    AutoCompleteTextView mTextView;
    Button btn;
    Button btnclear;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.home, container, false);
        findIds();
        createOnClickListeners(myView);
        return myView;

    }

    private void findIds() {
        btn = (Button)myView.findViewById(R.id.searchbtn);
        mTextView = (AutoCompleteTextView)myView.findViewById(R.id.keywordtxt);
        btnclear=(Button)myView.findViewById(R.id.clearbtn);
    }

    private void createOnClickListeners(View myView) {

        btnclear.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v) {
                mTextView.setText("");
            }
        });

        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mTextView.getText().toString().trim().length()!=0){// hv nnot creatde yet
                    Intent intent = new Intent(getActivity(),ResultActivity.class);//AboutMeActivity change to Resultactivity
                    intent.putExtra("search_string",mTextView.getText().toString().trim());//here content in keyword is passed to nxt activity
                    startActivity(intent);

                }else{
                    Toast.makeText(getActivity(),"Please enter a keyword",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //in the result activity
    //write these lines in oncreate to get the text
    //int searchString = getIntent().getExtraString("search_string");
    //then make a network call using this search string
}
