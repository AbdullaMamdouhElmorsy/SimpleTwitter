package com.morsy.simpletwitter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.morsy.simpletwitter.models.TwitterApplication;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UserInfoFragment extends Fragment {

    TwitterClient mTwitterClient;
    @Bind(R.id.ivUserProfileImage)
    ImageView mImageViewUserProfileImage;
    @Bind(R.id.tvUserFullName)
    TextView mTextViewUserFullName;
    @Bind(R.id.tvUserTagLine)
    TextView mTextViewUserTageLine;
    @Bind(R.id.ivUserProfileBackgroundImage)
    ImageView mImageViewUserBackgroundProfileImage;

    public static UserInfoFragment newInstance(String profileUrl,
                                               String profileBackgroundUrl,
                                               String name,
                                               String tagLine) {
        UserInfoFragment userInfoFragment = new UserInfoFragment();
        Bundle args = new Bundle();
        args.putString("profile_url", profileUrl);
        args.putString("full_name", name);
        args.putString("tag_line", tagLine);
        args.putString("profile_background_image_url", profileBackgroundUrl);
        userInfoFragment.setArguments(args);
        return userInfoFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTwitterClient = TwitterApplication.getRestClient();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        super.onCreateView(inflater, parent, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_userinfo_header, parent, false);
        ButterKnife.bind(this, v);

        mTextViewUserFullName.setText(getArguments().getString("full_name"));
        mTextViewUserTageLine.setText(getArguments().getString("tag_line"));

        Glide.with(getContext()).load(getArguments().getString("profile_url"))
                .placeholder(R.mipmap.ic_wifi)
                .fitCenter()
                .into(mImageViewUserProfileImage);

        Glide.with(getContext()).load(getArguments().getString("profile_background_image_url"))
                .placeholder(R.drawable.striped)
                .fitCenter()
                .into(mImageViewUserBackgroundProfileImage);

        return v;
    }

}
