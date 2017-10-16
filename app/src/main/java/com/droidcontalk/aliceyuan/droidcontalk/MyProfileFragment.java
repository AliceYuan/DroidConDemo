package com.droidcontalk.aliceyuan.droidcontalk;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.droidcontalk.aliceyuan.droidcontalk.MyUserUtils.UserCountApiCallback;
import com.droidcontalk.aliceyuan.droidcontalk.MyUserUtils.FollowEvent;
import com.pinterest.android.pdk.PDKCallback;
import com.pinterest.android.pdk.PDKClient;
import com.pinterest.android.pdk.PDKException;
import com.pinterest.android.pdk.PDKResponse;
import com.pinterest.android.pdk.PDKUser;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.droidcontalk.aliceyuan.droidcontalk.activity.LoginActivity.DEBUG;
import static com.pinterest.android.pdk.Utils.log;


public class MyProfileFragment extends Fragment {

    private final String USER_FIELDS = "id,username,image,counts,first_name,last_name,bio";
    //cache values to avoid having to make network calls in the future
    private PDKUser _myUser;
    private int _followingCount;
    private AvatarView _avatarView;

    public MyProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
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
        loadUser();
    }

    private void loadUser() {
        if (_myUser == null) {
            loadMyUser();
        } else {
            _avatarView.updateView(_myUser.getFirstName() + " " + _myUser.getLastName(),
                    MyUserUtils.get().getLargeImageUrl(_myUser), _myUser.getBio());
            updateFollowingCount(_followingCount);
        }
    }

    private void updateFollowingCount(int count) {
        _avatarView.updateFollowingText(getResources().getString(R.string.my_user_following, count));
    }

    private void loadMyUser() {
        MyUserUtils.get().loadMyFollowedUsersCount(new UserCountApiCallback() {
            @Override
            public void onSuccess(int count) {
                _followingCount = count;
                updateFollowingCount(count);
            }

            @Override
            public void onFailure(PDKException pdkException) {

            }
        });
        PDKClient.getInstance().getMe(USER_FIELDS, new PDKCallback() {
            @Override
            public void onSuccess(PDKResponse response) {
                if (DEBUG)
                    log(String.format("status: %d", response.getStatusCode()));
                if (DEBUG)
                    log(String.format("response", response.toString()));
                _myUser = response.getUser();
                _avatarView.updateView(_myUser.getFirstName() + " " + _myUser.getLastName(),
                        MyUserUtils.get().getLargeImageUrl(_myUser), _myUser.getBio());
            }

            @Override
            public void onFailure(PDKException exception) {
                if (DEBUG)
                    log(exception.getDetailMessage());
                Toast.makeText(getContext(), "/me Request failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(FollowEvent event) {
        // update cached value
        _followingCount = event.getFollowCount();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
