package com.morsy.simpletwitter;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.loopj.android.http.TextHttpResponseHandler;
import com.morsy.simpletwitter.models.TwitterApplication;
import com.morsy.simpletwitter.models.User;

import cz.msebera.android.httpclient.Header;


public class ProfileActivity extends AppCompatActivity {
    public static final String USER = "user";
    TwitterClient mTwitterClient;
    String screenName;
    User newUser;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mTwitterClient = TwitterApplication.getRestClient();

        screenName = getIntent().getStringExtra("screen_name");
        if (getIntent().hasExtra("screen_name")) {
            getUserInfo(savedInstanceState);
        }

    }


    void getUserInfo(final Bundle savedInstanceState) {
        mTwitterClient.getUserInfo(screenName, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    Gson gson = new GsonBuilder().create();
                    JsonObject jsonUserObject = gson.fromJson(responseString, JsonObject.class);

                    if (jsonUserObject != null) {
                        newUser = User.fromJsonObjectToUser(jsonUserObject);
                        if (newUser != null && !TextUtils.isEmpty(newUser.getTwitterHandle())) {
                            getSupportActionBar().setTitle("@" + newUser.getTwitterHandle());
                        }

                        loadPage(savedInstanceState, screenName);
                    }
                } catch (JsonParseException e) {
                    Log.d("Async onSuccess", "Json parsing error:" + e.getMessage(), e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.w("AsyncHttpClient", "HTTP Request failure: " + statusCode + " " +
                        throwable.getMessage());
            }

        });
    }

    void loadPage(Bundle savedInstanceState, String screenName) {
        if (savedInstanceState == null) {
            UserTimelineFragment userTimelineFragment = UserTimelineFragment.newInstance(screenName);
            UserInfoFragment userInfoFragment =
                    UserInfoFragment.newInstance(
                            newUser.getProfileImageUrl(),
                            newUser.getProfileBackgroundImageUrl(),
                            newUser.getUserName(), newUser.getTagLine());

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flUserTimelineContainer, userTimelineFragment);
            ft.replace(R.id.rlUserHeader, userInfoFragment);
            ft.commit();
        }
    }


}
