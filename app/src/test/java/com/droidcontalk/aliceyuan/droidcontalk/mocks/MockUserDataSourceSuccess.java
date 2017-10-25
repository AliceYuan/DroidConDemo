package com.droidcontalk.aliceyuan.droidcontalk.mocks;

import android.support.annotation.NonNull;

import com.droidcontalk.aliceyuan.droidcontalk.framework.MVPContract.RepositoryListener;
import com.droidcontalk.aliceyuan.droidcontalk.model.UserDataSource;
import com.pinterest.android.pdk.PDKUser;

import java.util.List;

public class MockUserDataSourceSuccess implements UserDataSource {

    @NonNull private final PDKUser _user;
    @NonNull private final List<PDKUser> _followingUsers;


    public MockUserDataSourceSuccess(@NonNull PDKUser user, @NonNull List<PDKUser> followingUsers) {
        _user = user;
        _followingUsers = followingUsers;
    }

    @Override
    public void loadMyUser(@NonNull RepositoryListener listener) {
        listener.onSuccess(_user);
    }

    @Override
    public void loadUserGivenUsername(@NonNull String userName,
            @NonNull RepositoryListener<PDKUser> listener) {
        listener.onSuccess(_user);
    }

    @Override
    public void followUser(boolean following, PDKUser user, @NonNull RepositoryListener<String> listener) {
        listener.onSuccess("success");
    }

    @Override
    public void loadMyUserNumFollowing(@NonNull RepositoryListener<Integer> listener) {
        listener.onSuccess(_followingUsers.size());
    }

    @Override
    public void loadMyUserIsFollowing(@NonNull PDKUser user,
            @NonNull RepositoryListener<Boolean> listener) {
        listener.onSuccess(_followingUsers.contains(user));
    }
}
