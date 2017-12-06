package com.morsy.simpletwitter.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.morsy.simpletwitter.R;
import com.morsy.simpletwitter.models.Tweet;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ImageTextTweetViewHolder extends RecyclerView.ViewHolder
        {
    @Bind(R.id.ivTweetImage)
    ImageView mImageViewTweetImage;
    @Bind(R.id.tvTweetTextWithImage)
    TextView mTextViewTweetWithImage;
    @Bind(R.id.tvUserName)
    TextView mTextViewUserName;
    @Bind(R.id.tvTwitterHandle)
    TextView mTextViewTwitterHandle;
    @Bind(R.id.tvTimeSend)
    TextView mTextViewTimeSend;
    @Bind(R.id.ivProfileImage)
    ImageView mImageViewProfileImage;

    List<Tweet> mTweets;
    Context mContext;

    public ImageTextTweetViewHolder(Context context, View view, List<Tweet> mTweets) {
        super(view);

        this.mTweets = mTweets;
        this.mContext = context;
        ButterKnife.bind(this, view);

    }
}
