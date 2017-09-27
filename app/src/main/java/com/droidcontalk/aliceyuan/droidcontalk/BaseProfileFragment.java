package com.droidcontalk.aliceyuan.droidcontalk;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.pinterest.android.pdk.PDKUser;

abstract class BaseProfileFragment extends Fragment {
    @Nullable protected RoundedImageView _profileIv;
    @Nullable protected TextView _nameTv;
    @Nullable protected TextView _bioTv;
    @Nullable protected TextView _followersTv;
    @Nullable protected LinearLayout _layout;

    public BaseProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        init(view);
        loadUser();
        return view;
    }

    private void init(final View view) {
        _layout = (LinearLayout) view.findViewById(R.id.profile_layout);
        _profileIv = (RoundedImageView) view.findViewById(R.id.profile_imageview);
        _nameTv = (TextView) view.findViewById(R.id.name_textview);
        _bioTv = (TextView) view.findViewById(R.id.bio_textview);
        _followersTv = (TextView) view.findViewById(R.id.followers_textview);
    }

    protected void updateView(@NonNull final PDKUser user) {
        _nameTv.setText(user.getFirstName() + " " + user.getLastName());
        _bioTv.setText(user.getBio());
        Glide.with(getContext())
                .load(getLargeImageUrl(user))
                .crossFade()
                .placeholder(R.drawable.avatar_circle)
                .dontAnimate()
                .into(_profileIv);
    }

    abstract void loadUser();

    abstract void updateFollowingCount(int count);

    /**
     * NOTE: this is a hack, pinterest api does not seem to return images of different sizes
     * we will manually replace image url to be size 444px instead;
     */
    @Nullable
    private String getLargeImageUrl(@Nullable PDKUser user) {
        if (user.getImageUrl() != null) {
            return user.getImageUrl().replace("60.jpg", "444.jpg");
        }
        return null;
    }

}
