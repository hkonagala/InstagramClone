package com.example.harikakonagala.instagramclone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


     /*   ParseUser user = new ParseUser();
        user.setUsername("harika");
        user.setPassword("test123");

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    Log.i("sign up", "successful");
                }else {
                    Log.i("sign up", "failed");
                }
            }
        });*/

   /*  ParseUser.logInInBackground("harika", "test", new LogInCallback() {
         @Override
         public void done(ParseUser user, ParseException e) {
             if( user !=null){
                 Log.i("login", "successful");
             }else{
                 Log.i("login", "failed" + e.toString());
             }
         }
     });*/

   ParseUser.logOut();

   if(ParseUser.getCurrentUser() != null){
       Log.i("current user", "logged in " + ParseUser.getCurrentUser().getUsername());
   }else {
       Log.i("current user", "logged in failed");
   }

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }
}
