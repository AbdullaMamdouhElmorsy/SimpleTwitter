package com.morsy.simpletwitter.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.morsy.simpletwitter.R;
import com.morsy.simpletwitter.models.Tweet;
import com.morsy.simpletwitter.Util.Util;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;

public class ComplexRecyclerViewUserTimelineAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static List<Tweet> sTweets;
    private static Context mContext;
    private final int TEXTONLY = 0, TEXT_PLUS_IMAGE = 1;
    public ComplexRecyclerViewUserTimelineAdapter(Context context, LinkedList<Tweet> tweets) {
        this.sTweets = tweets;
        mContext = context;
    }

    @Override
    public int getItemCount() {
        return this.sTweets.size();
    }

    @Override
    public int getItemViewType(int position) {
        Tweet tweet = sTweets.get(position);

        if (tweet.getMedia_type().equals("photo")) {
            return TEXT_PLUS_IMAGE;
        }

        return TEXTONLY;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case TEXTONLY:
                View v1 = inflater.inflate(R.layout.view_template_textonly_tweet,
                        viewGroup, false);
                viewHolder = new TextOnlyTweetViewHolder(mContext, v1, sTweets);
                break;
            case TEXT_PLUS_IMAGE:
                View v2 = inflater.inflate(R.layout.view_template_image_tweet,
                        viewGroup, false);
                viewHolder = new ImageTextTweetViewHolder(mContext, v2, sTweets);
                break;
            default:
                v2 = inflater.inflate(R.layout.view_template_textonly_tweet,
                        viewGroup, false);
                viewHolder = new ImageTextTweetViewHolder(mContext, v2, sTweets);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case TEXTONLY:
                TextOnlyTweetViewHolder vh1 = (TextOnlyTweetViewHolder) viewHolder;
                configureTextOnlyTweetViewHolder(vh1, position);
                break;
            case TEXT_PLUS_IMAGE:
                ImageTextTweetViewHolder vh2 = (ImageTextTweetViewHolder) viewHolder;
                configureImageTextTweetViewHolder(vh2, position);
                break;
            default:
                ImageTextTweetViewHolder vh = (ImageTextTweetViewHolder) viewHolder;
                configureImageTextTweetViewHolder(vh, position);
                break;
        }
    }

    private void configureTextOnlyTweetViewHolder(TextOnlyTweetViewHolder viewHolder, int position) {
        Tweet tweet = sTweets.get(position);

        viewHolder.mTextViewTweetTextOnly.setText(tweet.getText());
        viewHolder.mTextViewUserName.setText(tweet.getUser().getUserName());
        viewHolder.mTextViewTwitterHandle.setText("@" + tweet.getUser().getTwitterHandle());

        if (!TextUtils.isEmpty(tweet.getUser().getProfileImageUrl())) {
            Picasso.with(mContext).load(tweet.getUser().getProfileImageUrl())
                    .placeholder(R.mipmap.ic_wifi)
                    .into(viewHolder.mImageViewProfileImage);
        }
        viewHolder.mImageViewProfileImage.setTag(tweet.getUser().getTwitterHandle());

        viewHolder.mTextViewTimeSend.setText(Util.getRelativeTimeAgo(tweet.getCreated_at()));
    }

    private void configureImageTextTweetViewHolder(ImageTextTweetViewHolder viewHolder,
                                                   int position) {

        Tweet tweet = sTweets.get(position);

        viewHolder.mTextViewTweetWithImage.setText(tweet.getText());

        if (tweet.getMedia_type().equals("photo")) {

            if (!TextUtils.isEmpty(tweet.getMedia_url())) {
                Picasso.with(mContext).load(tweet.getMedia_url())
                        .placeholder(R.mipmap.ic_wifi)
                        .into(viewHolder.mImageViewTweetImage);
            }
        }
        viewHolder.mTextViewUserName.setText(tweet.getUser().getUserName());
        viewHolder.mTextViewTwitterHandle.setText("@" + tweet.getUser().getTwitterHandle());

        if (!TextUtils.isEmpty(tweet.getUser().getProfileImageUrl())) {
            Picasso.with(mContext).load(tweet.getUser().getProfileImageUrl())
                    .placeholder(R.mipmap.ic_wifi)
                    .into(viewHolder.mImageViewProfileImage);
        }
        viewHolder.mImageViewProfileImage.setTag(tweet.getUser().getTwitterHandle());

        viewHolder.mTextViewTimeSend.setText(Util.getRelativeTimeAgo(tweet.getCreated_at()));
    }
}