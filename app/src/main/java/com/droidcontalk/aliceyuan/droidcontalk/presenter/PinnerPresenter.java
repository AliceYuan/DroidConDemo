package com.droidcontalk.aliceyuan.droidcontalk.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.droidcontalk.aliceyuan.droidcontalk.PinnerProfileContract.PinnerProfileView;
import com.droidcontalk.aliceyuan.droidcontalk.PinnerProfileContract.PinnerProfileView.ProfileListener;
import com.droidcontalk.aliceyuan.droidcontalk.R;
import com.droidcontalk.aliceyuan.droidcontalk.framework.MVPContract.Presenter;
import com.droidcontalk.aliceyuan.droidcontalk.framework.MVPContract.RepositoryListener;
import com.droidcontalk.aliceyuan.droidcontalk.framework.MVPContract.ViewResources;
import com.droidcontalk.aliceyuan.droidcontalk.model.UserRepository;
import com.pinterest.android.pdk.PDKUser;

public class PinnerPresenter implements Presenter<PinnerProfileView>, ProfileListener {

    private final String _searchQuery;
    private final ViewResources _viewResources;
    private PDKUser _curUser;
    private boolean _following;
    private PinnerProfileView _view;

    public PinnerPresenter(@Nullable String searchQuery, @NonNull ViewResources viewResources) {
        _searchQuery = searchQuery;
        _viewResources = viewResources;
    }

    @Override
    public void attachView(final PinnerProfileView view) {
        _view = view;
        if (_searchQuery != null) {
            searchUser(_searchQuery, view);
        }
    }

    private void searchUser(final String searchQuery, final PinnerProfileView view) {
        UserRepository.get().loadUserGivenUsername(searchQuery, new RepositoryListener<PDKUser>() {
            @Override
            public void onSuccess(PDKUser user) {
                _curUser = user;
                view.updateAvatarView(user.getFirstName() + " " + user.getLastName(),
                        user.getImageUrl(), user.getBio());
                view.setActionBarTitle(_viewResources.getString(R.string.pinner_profile,
                        user.getFirstName()));
                view.updateFollowingText(_viewResources.getString(R.string.user_following,
                        user.getFirstName(), user.getFollowingCount()));
                // the callback nesting can be avoided using rxJava, we will not be going over this in this code example
                UserRepository.get().loadMyUserIsFollowing(user, new RepositoryListener<Boolean>() {
                    @Override
                    public void onSuccess(Boolean following) {
                        view.updateButtonFollow(following);
                    }

                    @Override
                    public void onError(Exception e) {
                        view.showToast("Something went wrong loadMyUserIsFollowing");
                    }
                });
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    @Override
    public void onFollowButtonClick() {
        final boolean newFollowing = !_following;
        _view.updateButtonFollow(newFollowing);
        UserRepository.get().followUser(newFollowing, _curUser, new RepositoryListener<String>() {
            @Override
            public void onSuccess(String model) {
                _following = newFollowing;
                _view.showToast(getToastMessage(newFollowing, _curUser.getFirstName()));
            }

            @Override
            public void onError(Exception e) {
                // revert on error
                _view.updateButtonFollow(_following);
            }
        });
    }

    private String getToastMessage(boolean follow, String firstName) {
        if (follow) {
            return _viewResources.getString(R.string.toast_follow, firstName);
        } else {
            return _viewResources.getString(R.string.toast_unfollow, firstName);
        }
    }

    @Override
    public void detachView() {
        _view = null;
    }
}
