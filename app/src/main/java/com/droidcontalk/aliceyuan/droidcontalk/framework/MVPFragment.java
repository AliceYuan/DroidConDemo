package com.droidcontalk.aliceyuan.droidcontalk.framework;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.droidcontalk.aliceyuan.droidcontalk.framework.MVPContract.Presenter;
import com.droidcontalk.aliceyuan.droidcontalk.framework.MVPContract.MVPView;
import com.droidcontalk.aliceyuan.droidcontalk.framework.MVPContract.ViewResources;

public abstract class MVPFragment extends Fragment implements MVPView {
    Presenter _presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _presenter = createPresenter();
    }

    @Nullable
    @Override
    public android.view.View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            Bundle savedInstanceState) {
        android.view.View createView = super.onCreateView(inflater, container, savedInstanceState);
        return createView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        _presenter.attachView(this);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    abstract public Presenter createPresenter();

    @Override
    public Presenter getPresenter() {
        return _presenter;
    }

    @Override
    public void onDestroyView() {
        _presenter.detachView();
        super.onDestroyView();
    }

    @Override
    public ViewResources getViewResources() {
        return new ViewResources() {

            @Override
            public String getString(@StringRes int id, Object... formatArgs) {
                return getResources().getString(id, formatArgs);
            }
        };
    }
}
