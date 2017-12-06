package com.morsy.simpletwitter;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.codepath.oauth.OAuthLoginActionBarActivity;

public class LogInActivity extends  OAuthLoginActionBarActivity<TwitterClient> {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }



    @Override
    public void onLoginSuccess() {
        Toast.makeText(LogInActivity.this , "loggedin" , Toast.LENGTH_LONG).show();
        Intent i = new Intent(LogInActivity.this , FollowersActivity.class);
        startActivity(i);
    }

    @Override
    public void onLoginFailure(Exception e) {
        e.printStackTrace();
    }

    public void loginToRest(View view) {
        getClient().connect();
    }
}
