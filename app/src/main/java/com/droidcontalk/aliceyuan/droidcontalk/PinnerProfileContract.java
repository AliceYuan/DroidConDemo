package com.droidcontalk.aliceyuan.droidcontalk;

import com.droidcontalk.aliceyuan.droidcontalk.framework.MVPContract.MVPView;

public class PinnerProfileContract {

    public interface PinnerProfileView extends MVPView {

        void updateFollowingText(String followingText);

        void updateAvatarView(String name, String imageUrl, String bioText);

        void showToast(String s);

        void setActionBarTitle(String string);

        void updateButtonFollow(boolean follow);

        interface ProfileListener {
            void onFollowButtonClick();
        }
    }
}
