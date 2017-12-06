package com.morsy.simpletwitter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.morsy.simpletwitter.models.TwitterApplication;

import java.io.IOException;

public abstract class TweetsFragment extends Fragment {

    TwitterClient mTwitterClient;

    abstract void populateTimeLine(boolean b, boolean b1);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTwitterClient = TwitterApplication.getRestClient(); //singleton client

    }

    protected boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}