package com.droidcontalk.aliceyuan.droidcontalk.mocks;

import android.support.annotation.NonNull;

import com.droidcontalk.aliceyuan.droidcontalk.framework.MVPContract.RepositoryListener;
import com.droidcontalk.aliceyuan.droidcontalk.model.UserDataSource;
import com.pinterest.android.pdk.PDKException;
import com.pinterest.android.pdk.PDKUser;

import java.util.List;

public class MockUserDataSourceFailure implements UserDataSource {

    @Override
    public void loadMyUser(@NonNull RepositoryListener listener) {
        listener.onError(new PDKException());
    }

    @Override
    public void loadUserGivenUsername(@NonNull String userName,
            @NonNull RepositoryListener<PDKUser> listener) {
        listener.onError(new PDKException());
    }

    @Override
    public void followUser(boolean following, PDKUser user, @NonNull RepositoryListener<String> listener) {
        listener.onError(new PDKException());
    }

    @Override
    public void loadMyUserNumFollowing(@NonNull RepositoryListener<Integer> listener) {
        listener.onError(new PDKException());
    }

    @Override
    public void loadMyUserIsFollowing(@NonNull PDKUser user,
            @NonNull RepositoryListener<Boolean> listener) {
        listener.onError(new PDKException());
    }
}
