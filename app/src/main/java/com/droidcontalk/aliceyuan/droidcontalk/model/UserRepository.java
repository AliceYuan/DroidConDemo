package com.droidcontalk.aliceyuan.droidcontalk.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.droidcontalk.aliceyuan.droidcontalk.framework.MVPContract.RepositoryListener;
import com.pinterest.android.pdk.PDKCallback;
import com.pinterest.android.pdk.PDKClient;
import com.pinterest.android.pdk.PDKException;
import com.pinterest.android.pdk.PDKResponse;
import com.pinterest.android.pdk.PDKUser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.droidcontalk.aliceyuan.droidcontalk.activity.LoginActivity.DEBUG;
import static com.pinterest.android.pdk.Utils.log;

public class UserRepository implements UserDataSource {

    // LazyHolder such that we don't have class class methods
    private static class LazyHolder {
        static final UserRepository INSTANCE = new UserRepository();
    }

    public static UserRepository get() {
        return UserRepository.LazyHolder.INSTANCE;
    }

    private final String USER_FIELDS = "id,username,image,counts,first_name,last_name,bio";
    @Nullable private List<PDKUser> _myFollowedUserList;
    private PDKUser _myUser;
    private Map<String, PDKUser> _usernameToUser = new HashMap<>();

    public void loadMyUser(@NonNull final RepositoryListener<PDKUser> listener) {
        /**
         * NOTE: the caching strategy is extremely simple, you may want to do things such as:
         * check local persistent storage
         * have a time limit on cache
         * have a strategy around when to cache user or not
         */
        // check cache first
        if (_myUser != null) {
            listener.onSuccess(_myUser);
            return;
        }
        // then do remote call if no cache
        PDKClient.getInstance().getMe(USER_FIELDS, new PDKCallback() {
            @Override
            public void onSuccess(PDKResponse response) {
                if (DEBUG)
                    log(String.format("status: %d", response.getStatusCode()));
                if (DEBUG)
                    log(String.format("response", response.toString()));
                _myUser = response.getUser();
                updateImageUrlToLarge(_myUser);
                listener.onSuccess(_myUser);
            }

            @Override
            public void onFailure(PDKException exception) {
                if (DEBUG)
                    log(exception.getDetailMessage());
                listener.onError(exception);
            }
        });
    }

    public void loadUserGivenUsername(@NonNull final String userName, @NonNull final RepositoryListener<PDKUser>
            listener) {
        // check cache first
        if (_usernameToUser.get(userName) != null) {
            listener.onSuccess(_usernameToUser.get(userName));
            return;
        }
        // then do remote call if no cache
        PDKClient.getInstance().getUser(userName, USER_FIELDS, new PDKCallback() {
                @Override
                public void onSuccess(PDKResponse response) {
                    Log.d(getClass().getName(), "Response: " + response.getStatusCode());
                    PDKUser user = response.getUser();
                    if (user != null) {
                        updateImageUrlToLarge(user);
                        _usernameToUser.put(userName, user);
                        listener.onSuccess(user);
                    } else {
                        listener.onError(new PDKException("could not create user"));
                    }
                }

                @Override
                public void onFailure(PDKException exception) {
                    Log.e(getClass().getName(), "error: " + exception.getDetailMessage());
                    listener.onError(exception);
                }
            }
        );
    }

    /**
     * follow user api from my user
     * @param following
     * @param user
     * @param listener expects a string of whether or not followUser was successful
     */
    public void followUser(boolean following, PDKUser user, final RepositoryListener<String> listener) {
        mockedFollowUser(following, user, new PDKCallback() {
            @Override
            public void onSuccess(PDKResponse response) {
                super.onSuccess(response);
                listener.onSuccess("successful");
            }

            @Override
            public void onFailure(PDKException exception) {
                super.onFailure(exception);
                listener.onError(exception);
            }
        });
    }

    public void loadMyUserNumFollowing(@NonNull final RepositoryListener<Integer> listener) {
        // check cache first
        if (_myFollowedUserList != null) {
            listener.onSuccess(_myFollowedUserList.size());
            return;
        }
        // then do remote call if no cache
        updateMyFollowedUserList(new RepositoryListener() {
            @Override
            public void onSuccess(Object model) {
                listener.onSuccess(_myFollowedUserList.size());
            }

            @Override
            public void onError(Exception e) {
                listener.onError(e);
            }
        });
    }

    public void loadMyUserIsFollowing(@NonNull final PDKUser user, @NonNull final RepositoryListener<Boolean> listener) {
        if (_myFollowedUserList == null) {
            updateMyFollowedUserList(new RepositoryListener() {
                @Override
                public void onSuccess(Object model) {
                    if (_myFollowedUserList.contains(user)) {
                        listener.onSuccess(true);
                    } else {
                        listener.onSuccess(false);
                    }
                }

                @Override
                public void onError(Exception e) {
                    listener.onError(e);
                }
            });
        } else {
            if (_myFollowedUserList.contains(user)) {
                listener.onSuccess(true);
            } else {
                listener.onSuccess(false);
            }
        }
    }

    private void updateMyFollowedUserList(final RepositoryListener listener) {
        PDKClient.getInstance().getMyFollowedUsers(USER_FIELDS, new PDKCallback() {
            @Override
            public void onSuccess(PDKResponse response) {
                _myFollowedUserList = response.getUserList();
                listener.onSuccess(null);
            }

            @Override
            public void onFailure(PDKException exception) {
                Log.e(getClass().getName(), "onFailure: " + exception);
                listener.onError(exception);
            }
        });
    }



    /**
     * Note: This should be an api call however pinterest pdk does not expose this api externally
     * so we're going to mock it for the purpose of the demo
     */
    private void mockedFollowUser(boolean follow, @NonNull PDKUser user, @NonNull PDKCallback listener) {
        if (_myFollowedUserList != null) {
            if (follow) {
                _myFollowedUserList.add(user);
                listener.onSuccess(new PDKResponse(null));
            } else {
                boolean remove = _myFollowedUserList.remove(user);
                if (remove) {
                    listener.onSuccess(new PDKResponse(null));
                } else {
                    listener.onFailure(new PDKException("couldn't find user to remove"));
                }
            }
        } else {
            listener.onFailure(new PDKException("list is null"));
        }
    }


    /**
     * NOTE: this is a hack, pinterest api does not seem to return images of different sizes
     * we will manually replace image url to be size 444px instead;
     */
    private void updateImageUrlToLarge(@Nullable PDKUser user) {
        if (user.getImageUrl() != null) {
            user.setImageUrl(user.getImageUrl().replace("60.jpg", "444.jpg"));
        }
    }
}
