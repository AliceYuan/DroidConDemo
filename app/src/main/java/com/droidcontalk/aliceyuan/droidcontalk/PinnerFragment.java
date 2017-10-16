package com.droidcontalk.aliceyuan.droidcontalk;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.droidcontalk.aliceyuan.droidcontalk.MyUserUtils.UserCountApiCallback;
import com.pinterest.android.pdk.PDKCallback;
import com.pinterest.android.pdk.PDKClient;
import com.pinterest.android.pdk.PDKException;
import com.pinterest.android.pdk.PDKResponse;
import com.pinterest.android.pdk.PDKUser;

public class PinnerFragment extends Fragment {

    private static final String ARG_SEARCH = "search_query";
    @Nullable private String _searchQuery;
    PDKUser _curUser;

    private final String USER_FIELDS = "id,username,image,counts,first_name,last_name,bio";
    private Button _followBtn;
    private boolean _following;
    private AvatarView _avatarView;
    private FollowListener _followListener;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pinner_profile, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _avatarView = (AvatarView) view.findViewById(R.id.avatar_view);
        _followBtn = (Button) view.findViewById(R.id.follow_button);
        getActivity().setTitle(getResources().getString(R.string.profile));
        loadUser();
    }

    private void loadUser() {
        if (_searchQuery != null) {
            searchUser(_searchQuery);
        }
    }

    private void updateFollowingCount(int count) {
        _avatarView.updateFollowingText(getResources().getString( R.string.user_following, _curUser.getFirstName(),
                count));
    }

    private void init(final PDKUser user) {
        final Context context = getContext();
        updateFollowingCount(user.getFollowingCount());
        _avatarView.updateView(user.getFirstName() + " " + user.getLastName(),
                MyUserUtils.get().getLargeImageUrl(user), user.getBio());
        _followBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                final boolean newFollowing = !_following;
                setButtonFollow(newFollowing);
                MyUserUtils.get().followUser(newFollowing, user, new UserCountApiCallback() {
                    @Override
                    public void onSuccess(int count) {
                        _following = newFollowing;
                        postToastFollow(newFollowing);
                        if (_followListener != null) {
                            _followListener.onFollowCountChanged(count);
                        }
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
                            init(_curUser);
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

    public void registerListener(FollowListener followListener) {
        _followListener = followListener;
    }
}
