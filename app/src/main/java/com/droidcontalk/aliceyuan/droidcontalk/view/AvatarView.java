package com.droidcontalk.aliceyuan.droidcontalk.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.droidcontalk.aliceyuan.droidcontalk.R;
import com.makeramen.roundedimageview.RoundedImageView;

public class AvatarView extends LinearLayout {
    @Nullable protected RoundedImageView _profileIv;
    @Nullable protected TextView _nameTv;
    @Nullable protected TextView _bioTv;
    @Nullable protected TextView _followersTv;
    @Nullable protected LinearLayout _layout;

    public AvatarView(Context context) {
        super(context);
        View view = inflate(context, R.layout.avatar_view, this);
        init(context);
    }

    public AvatarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AvatarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = inflate(context, R.layout.avatar_view, this);
        _layout = (LinearLayout) view.findViewById(R.id.profile_layout);
        _profileIv = (RoundedImageView) view.findViewById(R.id.profile_imageview);
        _nameTv = (TextView) view.findViewById(R.id.name_textview);
        _bioTv = (TextView) view.findViewById(R.id.bio_textview);
        _followersTv = (TextView) view.findViewById(R.id.followers_textview);
    }

    public void updateView(String name, String imageUrl, String bioText) {
        _nameTv.setText(name);
        _bioTv.setText(bioText);
        Glide.with(getContext())
                .load(imageUrl)
                .crossFade()
                .placeholder(R.drawable.avatar_circle)
                .dontAnimate()
                .into(_profileIv);
    }

    public void updateFollowingText(String followersText) {
        _followersTv.setText(followersText);
    }
}
