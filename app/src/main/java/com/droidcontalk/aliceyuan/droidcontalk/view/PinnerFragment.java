package com.droidcontalk.aliceyuan.droidcontalk.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.droidcontalk.aliceyuan.droidcontalk.PinnerProfileContract.PinnerProfileView;
import com.droidcontalk.aliceyuan.droidcontalk.R;
import com.droidcontalk.aliceyuan.droidcontalk.framework.MVPContract.Presenter;
import com.droidcontalk.aliceyuan.droidcontalk.framework.MVPFragment;
import com.droidcontalk.aliceyuan.droidcontalk.presenter.PinnerPresenter;

public class PinnerFragment extends MVPFragment implements PinnerProfileView {

    private static final String ARG_SEARCH = "search_query";

    private Button _followBtn;
    private AvatarView _avatarView;
    private PinnerPresenter _listener;

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
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pinner_profile, container, false);
        _avatarView = (AvatarView) view.findViewById(R.id.avatar_view);
        _followBtn = (Button) view.findViewById(R.id.follow_button);
        _followBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                _listener.onFollowButtonClick();
            }
        });
        getActivity().setTitle(getResources().getString(R.string.profile));
        return view;
    }

    @Override
    public void updateFollowingText(String followingText) {
        _avatarView.updateFollowingText(followingText);
    }

    @Override
    public void updateAvatarView(String name, String imageUrl, String bioText) {
        _avatarView.updateView(name, imageUrl, bioText);
    }

    @Override
    public Presenter createPresenter() {
        String searchQuery = getArguments().getString(ARG_SEARCH);
        PinnerPresenter pinnerPresenter = new PinnerPresenter(searchQuery, getViewResources());
        _listener = pinnerPresenter;
        return pinnerPresenter;
    }

    @Override
    public void showToast(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setActionBarTitle(String string) {
        getActivity().setTitle(string);
    }

    @Override
    public void updateButtonFollow(boolean follow) {
        if (!follow) {
            _followBtn.setText(getResources().getText(R.string.follow));
            _followBtn.setBackground(getResources().getDrawable(R.drawable.button_primary));
        } else {
            _followBtn.setText(getResources().getText(R.string.unfollow));
            _followBtn.setBackground(getResources().getDrawable(R.drawable.button_secondary));
        }
    }
}

