package com.droidcontalk.aliceyuan.droidcontalk.framework;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

public interface MVPContract {

    interface Presenter<V extends MVPView> {

        void attachView(@NonNull final V view);

        void detachView();
    }

    interface MVPView {
        Presenter createPresenter();

        Presenter getPresenter();

        ViewResources getViewResources();
    }

    interface RepositoryListener<M extends Object> {

        void onSuccess(M model);

        void onError(Exception e);
    }

    interface ViewResources {
        String getString(@StringRes int id, Object... formatArgs);
    }
}
