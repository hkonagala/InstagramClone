package com.example.harikakonagala.instagramclone;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class FeedActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        linearLayout = (LinearLayout) findViewById(R.id.linear_layout);

        Intent intent = getIntent();
        String activeUsername = intent.getStringExtra("username");
        setTitle(activeUsername + "'s Feed");

        ParseQuery<ParseUser> query = new ParseQuery<ParseUser>("Image");
        query.whereEqualTo("username", activeUsername);
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e == null){
                    if(objects.size()>0){
                        for(ParseObject object: objects){
                            ParseFile file = (ParseFile) object.get("image");
                            file.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {

                                    if(e == null && data !=null){
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                        imageView = new ImageView(getApplicationContext());
                                        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.WRAP_CONTENT
                                        ));

                                        imageView.setImageBitmap(bitmap);
                                        linearLayout.addView(imageView);

                                    }
                                }
                            });
                        }
                    }else {
                        Toast.makeText(getApplicationContext(), "no photos available to display", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "error accessing the photos, please try again", Toast.LENGTH_LONG).show();

                }
            }
        });

    }
}
