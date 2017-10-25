package com.droidcontalk.aliceyuan.droidcontalk.presenter;

import com.droidcontalk.aliceyuan.droidcontalk.mocks.MockUserDataSourceFailure;
import com.droidcontalk.aliceyuan.droidcontalk.mocks.MockUserDataSourceSuccess;
import com.droidcontalk.aliceyuan.droidcontalk.MyProfileContract.MyProfileView;
import com.droidcontalk.aliceyuan.droidcontalk.presenter.MyProfilePresenterTests.TestUpdateMyProfileFailure;
import com.droidcontalk.aliceyuan.droidcontalk.presenter.MyProfilePresenterTests.TestUpdateMyProfileSuccess;
import com.droidcontalk.aliceyuan.droidcontalk.framework.MVPContract.ViewResources;
import com.droidcontalk.aliceyuan.droidcontalk.model.UserDataSource;
import com.pinterest.android.pdk.PDKUser;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(Suite.class)
@SuiteClasses({TestUpdateMyProfileSuccess.class, TestUpdateMyProfileFailure.class})
public class MyProfilePresenterTests {

     @RunWith(MockitoJUnitRunner.class)
     public static abstract class BaseMyProfileTest {
         @Mock MyProfileView _myProfileView;
         UserDataSource _mockDataSource;
         @Mock ViewResources _viewResources;

         @Before
         public void setUp() throws Exception {
             _mockDataSource = getUserDataSource();
             MyProfilePresenter myProfilePresenter = new MyProfilePresenter(_viewResources, _mockDataSource);
             myProfilePresenter.attachView(_myProfileView);
         }

        abstract UserDataSource getUserDataSource();
    }

    public static class TestUpdateMyProfileSuccess extends BaseMyProfileTest {
        String FIRST_NAME = "First";
        String LAST_NAME = "Last";
        String IMAGE_URL = "imageUrl";
        String BIO = "bio";
        final PDKUser _pdkUser;
        final List<PDKUser> _followingUsersList = new ArrayList<>();

        public TestUpdateMyProfileSuccess() {
            _pdkUser = new PDKUser();
            _pdkUser.setFirstName(FIRST_NAME);
            _pdkUser.setLastName(LAST_NAME);
            _pdkUser.setImageUrl(IMAGE_URL);
            _pdkUser.setBio(BIO);
            _followingUsersList.add(new PDKUser());
        }

        @Override
        UserDataSource getUserDataSource() {
            return new MockUserDataSourceSuccess(_pdkUser, _followingUsersList);
        }

        @Test
        public void testLoadMyUserSuccess() throws Exception {
            verify(_myProfileView).updateAvatarView(FIRST_NAME + " " + LAST_NAME,
                    IMAGE_URL, BIO);
            verify(_viewResources).getString(anyInt(), eq(_followingUsersList.size()));
            verify(_myProfileView).updateFollowingText(null); // null because mock returns null
        }
    }

    public static class TestUpdateMyProfileFailure extends BaseMyProfileTest {

        @Override
        UserDataSource getUserDataSource() {
            return new MockUserDataSourceFailure();
        }

        @Test
        public void testLoadMyUserFailed() throws Exception {
            verify(_myProfileView).showToast(anyString());
        }
    }
}