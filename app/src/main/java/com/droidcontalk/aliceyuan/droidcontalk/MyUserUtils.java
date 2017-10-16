package com.droidcontalk.aliceyuan.droidcontalk;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.pinterest.android.pdk.PDKCallback;
import com.pinterest.android.pdk.PDKClient;
import com.pinterest.android.pdk.PDKException;
import com.pinterest.android.pdk.PDKResponse;
import com.pinterest.android.pdk.PDKUser;

import java.util.List;

public class MyUserUtils {

    // LazyHolder such that we don't have class class methods
    private static class LazyHolder {
        static final MyUserUtils INSTANCE = new MyUserUtils();
    }

    public static MyUserUtils get() {
        return LazyHolder.INSTANCE;
    }

    private final String USER_FIELDS = "id,username,image,first_name,last_name";
    // We're using this list to mock out the `followUser` api call
    @Nullable private List<PDKUser> _myFollowedUserList;

    public void loadMyFollowedUsersCount(final UserCountApiCallback callback) {
        PDKClient.getInstance().getMyFollowedUsers(USER_FIELDS, new PDKCallback() {
            @Override
            public void onSuccess(PDKResponse response) {
                _myFollowedUserList = response.getUserList();
                super.onSuccess(response);
                callback.onSuccess(_myFollowedUserList.size());
            }

            @Override
            public void onFailure(PDKException exception) {
                Log.e(getClass().getName(), "onFailure: " + exception);
                super.onFailure(exception);
                callback.onFailure(exception);
            }
        });
    }

    /**
     * Note: This should be an api call however pinterest pdk does not expose this api externally
     * so we're going to mock it for the purpose of the demo
     */
    public void followUser(boolean following, @NonNull PDKUser userToFollow, @NonNull UserCountApiCallback followUserCallback) {
        if (_myFollowedUserList != null) {
            if (following) {
                _myFollowedUserList.add(userToFollow);
                followUserCallback.onSuccess(_myFollowedUserList.size());
            } else {
                boolean remove = _myFollowedUserList.remove(userToFollow);
                if (remove) {
                    followUserCallback.onSuccess(_myFollowedUserList.size());
                } else {
                    PDKException pdkException = new PDKException("couldn't find user to remove");
                    followUserCallback.onFailure(pdkException);
                }
            }
        } else {
            PDKException pdkException = new PDKException("list is null");
            followUserCallback.onFailure(pdkException);
        }
    }

    /**
     * NOTE: this is a hack, pinterest api does not seem to return images of different sizes
     * we will manually replace image url to be size 444px instead;
     */
    @Nullable
    public String getLargeImageUrl(@Nullable PDKUser user) {
        if (user.getImageUrl() != null) {
            return user.getImageUrl().replace("60.jpg", "444.jpg");
        }
        return null;
    }

    public boolean isFollowing(PDKUser user) {
        return _myFollowedUserList.contains(user);
    }

    public static class FollowEvent {

        private final int _followCount;

        public FollowEvent(int followCount) {
            _followCount = followCount;
        }

        public int getFollowCount() {
            return _followCount;
        }
    }

    /**
     * Since PDK does not expose the follow user API, we are mocking the callback for the purpose of the demo.
     */
    interface UserCountApiCallback {
        void onSuccess(int count);

        void onFailure(PDKException pdkException);
    }
}
