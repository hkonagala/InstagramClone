package com.example.harikakonagala.instagramclone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.parse.GetCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //to save data into parse server
    /*    ParseObject score = new ParseObject("Score");
        score.put("username", "harika");
        score.put("score", 29);
        score.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null ){
                    Log.i("saveInBackground", "successfull");
                }else{
                    Log.i("saveInBackground", "Failed, Error: "+ e.toString());
                }
            }
        });*/

        //to get values back from parse server

   /*     ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Score");
        query.getInBackground("d8RoEIfhLT", new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if(e == null && object !=null){

                    //update the value
                    object.put("score", 200);
                    object.saveInBackground();



                    Log.i("object value", object.getString("username"));
                    Log.i("object value", String.valueOf(object.getInt("score")));
                }
            }
        });*/

        ParseObject parseObject = new ParseObject("Tweet");
        parseObject.put("username", "harika");
        parseObject.put("tweet", "hey there!");

        parseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    Log.i("parse object saved", "successfully saved");
                }else {
                    Log.i("parse object saved", "Failed to save, error: " + e.toString());
                }
            }
        });

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Tweet");
        query.getInBackground("wwe8fvliT9", new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if(e == null && object != null){

                    object.put("tweet", "hey! what up!");
                    object.saveInBackground();

                    Log.i("object value", object.getString("username"));
                    Log.i("object value", object.getString("tweet"));
                }
            }
        });

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }
}
