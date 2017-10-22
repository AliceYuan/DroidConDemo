package com.droidcontalk.aliceyuan.droidcontalk;

import com.droidcontalk.aliceyuan.droidcontalk.framework.MVPContract.MVPView;

public interface MyProfileContract {

    interface MyProfileView extends MVPView {

        void updateFollowingText(String followingText);

        void updateAvatarView(String name, String imageUrl, String bioText);

        void showToast(String s);
    }
}
