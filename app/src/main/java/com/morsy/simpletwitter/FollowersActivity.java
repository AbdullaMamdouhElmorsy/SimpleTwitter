package com.morsy.simpletwitter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.loopj.android.http.TextHttpResponseHandler;
import com.morsy.simpletwitter.adapters.EndlessRecyclerViewScrollListener;
import com.morsy.simpletwitter.adapters.RecyclerViewFollowingAdapter;
import com.morsy.simpletwitter.models.TwitterApplication;
import com.morsy.simpletwitter.models.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class FollowersActivity extends AppCompatActivity {

    final static String NEXT_CURSOR = "next_cursor";
    final static String USERS = "users";

    private TwitterClient mTwitterClient;
    LinearLayoutManager layoutManager;

    long cursor = -1;
    LinkedList<User> mUsers;
    RecyclerViewFollowingAdapter mRecyclerViewFollowersAdapter;

    @Bind(R.id.rvFollowers)
    RecyclerView mRecyclerViewFollowers;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.swipeContainerFollowers)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Followers");

        mUsers = new LinkedList<>();

        mRecyclerViewFollowersAdapter = new RecyclerViewFollowingAdapter(this, mUsers);
        mRecyclerViewFollowers.setAdapter(mRecyclerViewFollowersAdapter);

        layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerViewFollowers.setLayoutManager(layoutManager);
        mRecyclerViewFollowers.setItemAnimator(new DefaultItemAnimator());

        mRecyclerViewFollowers.addOnScrollListener(
                new EndlessRecyclerViewScrollListener(layoutManager) {
                    @Override
                    public void onLoadMore(int page, int totalItemsCount) {
                        // Triggered only when new data needs to be appended to the list
                        // Add whatever code is needed to append new items to the bottom of the list
                        Toast.makeText(getApplicationContext(),
                                "Loading more...", Toast.LENGTH_SHORT).show();
                        populateFollowers(true, false);

                    }
                });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                populateFollowers(false, false);
            }
        });

        mTwitterClient = TwitterApplication.getRestClient();


        //Check for internet
        if (!isNetworkAvailable()) {
            Toast.makeText(getApplicationContext(), "Opps looks like network connectivity problem",
                    Toast.LENGTH_LONG).show();
            //TODO launch activity and show failure droid
        } else if (!isOnline()) {
            Toast.makeText(getApplicationContext(), "Your device is not online, " +
                            "check wifi and try again!",
                    Toast.LENGTH_LONG).show();
        } else {
            mUsers.clear();
            mRecyclerViewFollowersAdapter.notifyDataSetChanged();
            //kick off realtime timelines
            populateFollowers(false, false);
        }

    }

    private void populateFollowers(final boolean isScrolled, final boolean isRefreshed) {
        mTwitterClient.getFollowers(
                isRefreshed? -1: cursor,
                new TextHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        List<User> fetchedUsers = new ArrayList<>();
                        if (responseString != null) {
                            Log.i("FollowingActivity", responseString);
                            try {
                                Gson gson = new GsonBuilder().create();
                                JsonObject jsonObject = gson.fromJson(responseString, JsonObject.class);
                                if (jsonObject.has(NEXT_CURSOR)) {
                                    cursor = Long.parseLong(jsonObject.get(NEXT_CURSOR).getAsString());
                                }
                                JsonArray jsonUsersArray = jsonObject.getAsJsonArray(USERS);

                                if (jsonUsersArray != null) {
                                    for (int i = 0; i < jsonUsersArray.size(); i++) {
                                        JsonObject jsonUserObject = jsonUsersArray.get(i).getAsJsonObject();

                                        if (jsonUserObject != null) {
                                            fetchedUsers.add(User.fromJsonObjectToUser(jsonUserObject));
                                        }
                                    }
                                    Log.i("FollowingActivity", fetchedUsers.size() + " users found");

                                    //add to list
                                    mUsers.addAll(fetchedUsers);
                                    Log.i("TimelineActivity", mUsers.getFirst().getIdStr() + " max id");
                                    Log.i("TimelineActivity", mUsers.getLast().getIdStr() + " since id");
                                    Log.i("TimelineActivity", mUsers.size() + " users found");
                                }
                            } catch (JsonParseException e) {
                                Log.d("Async onSuccess", "Json parsing error:" + e.getMessage(), e);
                            }

                            //notify adapter
                            if (isScrolled) {
                                mRecyclerViewFollowersAdapter.notifyItemRangeInserted(
                                        mRecyclerViewFollowersAdapter.getItemCount(),
                                        fetchedUsers.size());
                            } else if (isRefreshed) {
                                mRecyclerViewFollowersAdapter.notifyDataSetChanged();
                                //layoutManager.scrollToPosition(0);
                                // Now we call setRefreshing(false) to signal refresh has finished
                                mSwipeRefreshLayout.setRefreshing(false);

                            } else {
                                mRecyclerViewFollowersAdapter.notifyDataSetChanged();
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString,
                                          Throwable throwable) {
                        Log.w("AsyncHttpClient", "HTTP Request failure: " + statusCode + " " +
                                throwable.getMessage());
                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    protected boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (InterruptedException | IOException e) { e.printStackTrace(); }
        return false;
    }
}
