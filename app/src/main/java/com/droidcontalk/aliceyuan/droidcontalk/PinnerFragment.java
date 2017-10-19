package com.droidcontalk.aliceyuan.droidcontalk;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.droidcontalk.aliceyuan.droidcontalk.framework.MVPContract.RepositoryListener;
import com.droidcontalk.aliceyuan.droidcontalk.model.UserRepository;
import com.pinterest.android.pdk.PDKUser;

public class PinnerFragment extends Fragment {

    private static final String ARG_SEARCH = "search_query";
    @Nullable private String _searchQuery;

    private Button _followBtn;
    private boolean _following;
    private AvatarView _avatarView;

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

    private void updateFollowingCount(int count, String firstName) {
        _avatarView.updateFollowingText(getResources().getString( R.string.user_following, firstName, count));
    }

    private void init(final PDKUser user) {
        updateFollowingCount(user.getFollowingCount(), user.getFirstName());
        _avatarView.updateView(user.getFirstName() + " " + user.getLastName(),
                user.getImageUrl(), user.getBio());
        getActivity().setTitle(getResources().getString(R.string.pinner_profile,
                user.getFirstName()));
        _followBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                final boolean newFollowing = !_following;
                setButtonFollow(newFollowing);
                UserRepository.get().followUser(newFollowing, user, new RepositoryListener<String>() {
                    @Override
                    public void onSuccess(String model) {
                        _following = newFollowing;
                        postToastFollow(newFollowing, user.getFirstName());
                    }

                    @Override
                    public void onError(Exception e) {
                        // revert on error
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
        UserRepository.get().loadUserGivenUsername(searchQuery, new RepositoryListener<PDKUser>() {
            @Override
            public void onSuccess(PDKUser user) {
                // the callback nesting can be avoided using rxJava, we will not be going over this in this code example
                UserRepository.get().loadMyUserIsFollowing(user, new RepositoryListener<Boolean>() {
                    @Override
                    public void onSuccess(Boolean following) {
                        setButtonFollow(following);
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
                init(user);
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    private void postToastFollow(boolean follow, String firstName) {
        if (follow) {
            Toast.makeText(getContext(), getResources().getString(R.string.toast_follow,
                    firstName), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), getResources().getString(R.string.toast_unfollow,
                    firstName), Toast .LENGTH_SHORT).show();
        }
    }
}
