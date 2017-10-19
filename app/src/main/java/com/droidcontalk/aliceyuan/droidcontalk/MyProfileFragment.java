package com.droidcontalk.aliceyuan.droidcontalk;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.droidcontalk.aliceyuan.droidcontalk.framework.MVPContract.RepositoryListener;
import com.droidcontalk.aliceyuan.droidcontalk.model.UserRepository;
import com.pinterest.android.pdk.PDKUser;

public class MyProfileFragment extends Fragment {

    //cache values to avoid having to make network calls in the future
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
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _avatarView = (AvatarView) view.findViewById(R.id.avatar_view);
        getActivity().setTitle(getResources().getString(R.string.my_profile));
        loadMyUser();
    }

    private void updateFollowingCount(int count) {
        _avatarView.updateFollowingText(getResources().getString(R.string.my_user_following, count));
    }

    private void loadMyUser() {
        UserRepository.get().loadMyUserNumFollowing(new RepositoryListener<Integer>() {
            @Override
            public void onSuccess(Integer followingCount) {
                updateFollowingCount(followingCount);
            }

            @Override
            public void onError(Exception e) {

            }
        });
        UserRepository.get().loadMyUser(new RepositoryListener<PDKUser>() {
            @Override
            public void onSuccess(PDKUser user) {
                _avatarView.updateView(user.getFirstName() + " " + user.getLastName(),
                        user.getImageUrl(), user.getBio());
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(getContext(), "UserRepository.loadMyUser failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
