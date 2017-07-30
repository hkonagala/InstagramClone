package com.example.harikakonagala.instagramclone;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;

import static android.R.attr.button;
import static android.R.attr.start;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener{

    EditText username , pwd;
    Button signup, login;
    Boolean isUserAnonymous = true;
    String usernametxt, pwdtext;
    RelativeLayout relativeLayout;
    ImageView background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        username = (EditText) findViewById(R.id.username);
        pwd = (EditText) findViewById(R.id.password);
        signup = (Button) findViewById(R.id.signup);
        signup.setPaintFlags(signup.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        login = (Button) findViewById(R.id.login);
        login.setPaintFlags(login.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        relativeLayout = (RelativeLayout) findViewById(R.id.relative_layout);
        background = (ImageView) findViewById(R.id.icon_imageView);

        pwd.setOnKeyListener(this);
        relativeLayout.setOnClickListener(this);
        background.setOnClickListener(this);
        //user is already logged in
        if(ParseUser.getCurrentUser() !=null){
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
        }

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }

    private void userLogin() {
        usernametxt = username.getText().toString();
        pwdtext = pwd.getText().toString();

        isUserAnonymous = false;
        ParseUser.logInInBackground(usernametxt, pwdtext, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {

                if(user !=null){
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    Log.i("login", "sucessfully logged in");
                    Toast.makeText(getApplicationContext(),
                            "Successfully Logged in",
                            Toast.LENGTH_LONG).show();
                }else {
                    Log.i("login", "failed to log in");
                    Toast.makeText(
                            getApplicationContext(),
                            e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void userSignup() {

        usernametxt = username.getText().toString();
        pwdtext = pwd.getText().toString();

        if(usernametxt.equals("") && pwdtext.equals("")){
            Toast.makeText(
                    getApplicationContext(),
                    "Username or Password cannot be empty",
                    Toast.LENGTH_LONG).show();
        } else {
            isUserAnonymous = true;
            ParseUser user = new ParseUser();
            user.setUsername(usernametxt);
            user.setPassword(pwdtext);
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if(e == null){
                        Log.i("signup", "sign up successfully done");
                        Toast.makeText(getApplicationContext(),
                                "Successfully Signed up, please log in.",
                                Toast.LENGTH_LONG).show();
                    }else {
                        Log.i("signup", "signup failed");
                        Toast.makeText(getApplicationContext(),
                                e.getMessage(), Toast.LENGTH_LONG)
                                .show();
                    }
                }
            });
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        signup.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.login:
                userLogin();
                if(isUserAnonymous){
                    isUserAnonymous = false;
                    signup.setText("Login");
                    login.setText("Or, signup");
                }

                break;
            case R.id.signup:
                userSignup();
                if(!isUserAnonymous){
                    isUserAnonymous = true;
                    signup.setText("signup");
                    login.setText("Or, Login");
                }
                break;
            case R.id.relative_layout:
                removeKeyboard();
                break;
            case R.id.icon_imageView:
                removeKeyboard();
                break;
        }
    }

    private void removeKeyboard() {
        InputMethodManager in = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if(keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_DOWN){
            userSignup();
        }

        return false;
    }
}
