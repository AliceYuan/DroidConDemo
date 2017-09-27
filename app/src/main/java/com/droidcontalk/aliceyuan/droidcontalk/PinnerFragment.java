package com.droidcontalk.aliceyuan.droidcontalk;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.droidcontalk.aliceyuan.droidcontalk.MyUserUtils.FollowEvent;
import com.droidcontalk.aliceyuan.droidcontalk.MyUserUtils.UserCountApiCallback;
import com.pinterest.android.pdk.PDKCallback;
import com.pinterest.android.pdk.PDKClient;
import com.pinterest.android.pdk.PDKException;
import com.pinterest.android.pdk.PDKResponse;
import com.pinterest.android.pdk.PDKUser;

import org.greenrobot.eventbus.EventBus;

public class PinnerFragment extends BaseProfileFragment {

    private static final String ARG_SEARCH = "search_query";
    @Nullable private String _searchQuery;
    PDKUser _curUser;

    private final String USER_FIELDS = "id,username,image,counts,first_name,last_name,bio";
    private Button _followBtn;
    private boolean _following;

    public PinnerFragment() {
        // Required empty public constructor
    }

    public static PinnerFragment newInstance(String searchQuery) {
        Bundle args = new Bundle();
        args.putString(ARG_SEARCH, searchQuery);
        PinnerFragment fragment = new PinnerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            _searchQuery = getArguments().getString(ARG_SEARCH);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getResources().getString(R.string.profile));
    }


    @Override
    protected void loadUser() {
        if (_searchQuery != null) {
            searchUser(_searchQuery);
        }
    }

    @Override
    public void updateFollowingCount(int count) {
        _followersTv.setText(getResources().getString( R.string.user_following, _curUser.getFirstName(),
                _curUser.getFollowingCount()));
    }

    @Override
    protected void updateView(@NonNull PDKUser user) {
        super.updateView(user);
        init(getContext(), user);
    }

    private void init(final Context context, final PDKUser user) {
        _followBtn = new Button(context);
        LayoutParams layoutParams = new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        _followBtn.setLayoutParams(layoutParams);
        _followBtn.setTextColor(getResources().getColor(R.color.colorAccent));
        _layout.addView(_followBtn);
        updateFollowingCount(user.getFollowingCount());
        _followBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                final boolean newFollowing = !_following;
                setButtonFollow(newFollowing);
                MyUserUtils.get().followUser(newFollowing, user, new UserCountApiCallback() {
                    @Override
                    public void onSuccess(int count) {
                        EventBus.getDefault().post(new FollowEvent(count));
                        _following = newFollowing;
                        postToastFollow(newFollowing);
                    }

                    @Override
                    public void onFailure(PDKException pdkException) {
                        setButtonFollow(_following);
                    }
                });
            }
        });
    }

    private void setButtonFollow(boolean follow) {
        _following = follow;
        if (!follow) {
            _followBtn.setText(getResources().getText(R.string.follow));
            _followBtn.setBackground(getResources().getDrawable(R.drawable.button_primary));
        } else {
            _followBtn.setText(getResources().getText(R.string.unfollow));
            _followBtn.setBackground(getResources().getDrawable(R.drawable.button_secondary));
        }
    }

    private void searchUser(final String searchQuery) {
        PDKClient.getInstance().getUser(searchQuery, USER_FIELDS, new PDKCallback() {
                    @Override
                    public void onSuccess(PDKResponse response) {
                        Log.d(getClass().getName(), "Response: " + response.getStatusCode());
                        _curUser = response.getUser();
                        if (_curUser != null) {
                            boolean following = MyUserUtils.get().isFollowing(_curUser);
                            updateView(_curUser);
                            setButtonFollow(following);
                            getActivity().setTitle(getResources().getString(R.string.pinner_profile,
                                    _curUser.getFirstName()));
                        }
                    }

                    @Override
                    public void onFailure(PDKException exception) {
                        Log.e(getClass().getName(), "error: " + exception.getDetailMessage());
                        Toast.makeText(getContext(), "Error: cannot find user of username \"" + searchQuery + "\"",
                                Toast.LENGTH_SHORT).show(); }
                }
        );
    }

    private void postToastFollow(boolean follow) {
        if (follow) {
            Toast.makeText(getContext(), getResources().getString(R.string.toast_follow,
                    _curUser.getFirstName()), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), getResources().getString(R.string.toast_unfollow,
                    _curUser.getFirstName()), Toast .LENGTH_SHORT).show();
        }
    }
}
