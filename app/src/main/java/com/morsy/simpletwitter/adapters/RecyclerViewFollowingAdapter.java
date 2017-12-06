package com.morsy.simpletwitter.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.morsy.simpletwitter.ProfileActivity;
import com.morsy.simpletwitter.R;
import com.morsy.simpletwitter.models.User;

import java.util.List;

public class RecyclerViewFollowingAdapter
        extends RecyclerView.Adapter<RecyclerViewFollowingAdapter.ViewHolder> {

    List<User> mUsers;
    Context mContext;
    public RecyclerViewFollowingAdapter(Context context, List<User> users ) {
        mUsers = users;
        mContext = context;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {


        ImageView mImageViewFollowingUserPhoto;
        TextView mTextViewFollowingUserName;
        TextView mTextViewFollowingScreenName;
        TextView mTextViewFollowingTagline;


        public ViewHolder(View itemView) {
            super(itemView);
            mImageViewFollowingUserPhoto = (ImageView)itemView.findViewById(R.id.ivFollowUserPhoto);
            mTextViewFollowingUserName = (TextView)itemView.findViewById(R.id.tvFollowUserName);
            mTextViewFollowingScreenName = (TextView)itemView.findViewById(R.id.tvFollowUserScreenName);
            mTextViewFollowingTagline = (TextView)itemView.findViewById(R.id.tvFollowUserTagline);
        }
    }
    @Override
    public RecyclerViewFollowingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                      int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View followingView = inflater.inflate(R.layout.follower_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(followingView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewFollowingAdapter.ViewHolder viewHolder,
                                 int position) {
        final User user = mUsers.get(position);

        viewHolder.mTextViewFollowingUserName.setText(user.getUserName());
        viewHolder.mTextViewFollowingScreenName.setText("@" + user.getTwitterHandle());
        viewHolder.mTextViewFollowingTagline.setText(user.getTagLine());
        if (!TextUtils.isEmpty(user.getProfileImageUrl())) {
            Glide.with(mContext).load(user.getProfileImageUrl()).fitCenter()
                    .into(viewHolder.mImageViewFollowingUserPhoto);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent( mContext , ProfileActivity.class);
                i.putExtra("screen_name",user.getTwitterHandle());
                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }
}
