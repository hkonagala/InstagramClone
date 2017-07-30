package com.example.harikakonagala.instagramclone;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(HomeActivity.this, FeedActivity.class);
                i.putExtra("username", users.get(position));
                startActivity(i);
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
            case R.id.share_picture:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                }else {
                    getPhoto();
                }
                }else {
                    getPhoto();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data !=null){
            Uri selectedImage = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                Log.i("photo", "received");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                ParseFile file = new ParseFile("image.png", byteArray);
                ParseObject object = new ParseObject("Image");
                object.put("image", file);
                object.put("username", ParseUser.getCurrentUser().getUsername());

                object.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null){
                            Toast.makeText(getApplicationContext(), "image shared", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "image cannot be shared, please try again", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void getPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getPhoto();
            }
        }
    }


}
