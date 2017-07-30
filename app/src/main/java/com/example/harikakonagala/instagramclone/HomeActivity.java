package com.example.harikakonagala.instagramclone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> users = new ArrayList<>();
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        listView = (ListView) findViewById(R.id.user_list);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, users);

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        //check for current user
        query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.addAscendingOrder("username");

        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e == null){
                    if(objects.size() >0 ){
                        for( ParseUser object : objects){
                            users.add(object.getUsername());
                        }
                        listView.setAdapter(arrayAdapter);
                    }
                }else {

                    e.printStackTrace();
                }
            }
        });





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_logout, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.logout:
                ParseUser.logOut();
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "logged out", Toast.LENGTH_LONG).show();
                return true;
        }
        return super.onOptionsItemSelected(item);

    }
}
