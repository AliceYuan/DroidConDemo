package com.droidcontalk.aliceyuan.droidcontalk.presenter;

import android.support.annotation.NonNull;

import com.droidcontalk.aliceyuan.droidcontalk.MyProfileContract.MyProfileView;
import com.droidcontalk.aliceyuan.droidcontalk.R;
import com.droidcontalk.aliceyuan.droidcontalk.framework.MVPContract.Presenter;
import com.droidcontalk.aliceyuan.droidcontalk.framework.MVPContract.RepositoryListener;
import com.droidcontalk.aliceyuan.droidcontalk.framework.MVPContract.ViewResources;
import com.droidcontalk.aliceyuan.droidcontalk.model.UserDataSource;
import com.droidcontalk.aliceyuan.droidcontalk.model.UserRepository;
import com.pinterest.android.pdk.PDKUser;

public class MyProfilePresenter implements Presenter<MyProfileView> {
    private final ViewResources _viewResources;
    @NonNull private final UserDataSource _dataSource;

    public MyProfilePresenter(@NonNull ViewResources viewResources, @NonNull UserDataSource dataSource) {
        _viewResources = viewResources;
        _dataSource = dataSource;
    }

    @Override
    public void attachView(@NonNull final MyProfileView view) {
        loadUser(view);
    }

    @Override
    public void detachView() {

    }

    private void loadUser(final MyProfileView view) {
        _dataSource.loadMyUserNumFollowing(new RepositoryListener<Integer>() {
            @Override
            public void onSuccess(Integer followingCount) {
                view.updateFollowingText(_viewResources.getString(R.string.my_user_following, followingCount));
            }

            @Override
            public void onError(Exception e) {

            }
        });
        _dataSource.loadMyUser(new RepositoryListener<PDKUser>() {
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
