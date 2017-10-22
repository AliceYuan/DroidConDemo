package com.droidcontalk.aliceyuan.droidcontalk.presenter;

import android.support.annotation.NonNull;

import com.droidcontalk.aliceyuan.droidcontalk.MyProfileContract.MyProfileView;
import com.droidcontalk.aliceyuan.droidcontalk.R;
import com.droidcontalk.aliceyuan.droidcontalk.framework.MVPContract.Presenter;
import com.droidcontalk.aliceyuan.droidcontalk.framework.MVPContract.RepositoryListener;
import com.droidcontalk.aliceyuan.droidcontalk.framework.MVPContract.ViewResources;
import com.droidcontalk.aliceyuan.droidcontalk.model.UserRepository;
import com.pinterest.android.pdk.PDKUser;

public class MyProfilePresenter implements Presenter<MyProfileView> {
    private final ViewResources _viewResources;

    public MyProfilePresenter(ViewResources viewResources) {
        _viewResources = viewResources;
    }

    @Override
    public void attachView(@NonNull final MyProfileView view) {
        loadUser(view);
    }

    @Override
    public void detachView() {

    }

    private void loadUser(final MyProfileView view) {
        UserRepository.get().loadMyUserNumFollowing(new RepositoryListener<Integer>() {
            @Override
            public void onSuccess(Integer followingCount) {
                view.updateFollowingText(_viewResources.getString(R.string.my_user_following, followingCount));
            }

            @Override
            public void onError(Exception e) {

            }
        });
        UserRepository.get().loadMyUser(new RepositoryListener<PDKUser>() {
            @Override
            public void onSuccess(PDKUser user) {
                view.updateAvatarView(user.getFirstName() + " " + user.getLastName(),
                        user.getImageUrl(), user.getBio());
            }

            @Override
            public void onError(Exception e) {
                view.showToast("failed to load my profile. try again");
            }
        });

    }
}
