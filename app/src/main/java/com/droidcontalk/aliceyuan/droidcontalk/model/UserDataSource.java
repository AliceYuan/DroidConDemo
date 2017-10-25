package com.droidcontalk.aliceyuan.droidcontalk.model;

import android.support.annotation.NonNull;

import com.droidcontalk.aliceyuan.droidcontalk.framework.MVPContract.RepositoryListener;
import com.pinterest.android.pdk.PDKUser;

public interface UserDataSource {

    void loadMyUser(@NonNull final RepositoryListener<PDKUser> listener);

    void loadUserGivenUsername(@NonNull final String userName, @NonNull final RepositoryListener<PDKUser> listener);

    void followUser(boolean following, PDKUser user, @NonNull final RepositoryListener<String> listener);

    void loadMyUserNumFollowing(@NonNull final RepositoryListener<Integer> listener);

    void loadMyUserIsFollowing(@NonNull final PDKUser user, @NonNull final RepositoryListener<Boolean> listener);
}
