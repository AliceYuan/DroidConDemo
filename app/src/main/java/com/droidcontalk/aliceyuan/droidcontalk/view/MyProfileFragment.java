package com.droidcontalk.aliceyuan.droidcontalk.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.droidcontalk.aliceyuan.droidcontalk.MyProfileContract.MyProfileView;
import com.droidcontalk.aliceyuan.droidcontalk.R;
import com.droidcontalk.aliceyuan.droidcontalk.framework.MVPContract.Presenter;
import com.droidcontalk.aliceyuan.droidcontalk.framework.MVPContract.RepositoryListener;
import com.droidcontalk.aliceyuan.droidcontalk.framework.MVPFragment;
import com.droidcontalk.aliceyuan.droidcontalk.model.UserRepository;
import com.droidcontalk.aliceyuan.droidcontalk.presenter.MyProfilePresenter;
import com.droidcontalk.aliceyuan.droidcontalk.view.AvatarView;
import com.pinterest.android.pdk.PDKUser;

public class MyProfileFragment extends MVPFragment implements MyProfileView{

    private AvatarView _avatarView;

    public MyProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        getActivity().setTitle(getResources().getString(R.string.my_profile));
        _avatarView = (AvatarView) view.findViewById(R.id.avatar_view);
        return view;
    }

    @Override
    public Presenter createPresenter() {
        return new MyProfilePresenter(getViewResources(), UserRepository.get());
    }

    @Override
    public void updateFollowingText(String followingText) {
        _avatarView.updateFollowingText(followingText);
    }

    @Override
    public void updateAvatarView(String name, String imageUrl, String bioText) {
        _avatarView.updateView(name, imageUrl, bioText);
    }

    @Override
    public void showToast(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }
}
