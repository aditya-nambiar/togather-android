package com.togather.me.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.togather.me.service.CustomRequest;
import com.togather.me.service.HttpResult;
import com.togather.me.ui.activity.BaseActivity;

/**
 * Created by mouli on 8/9/15.
 */
public abstract class BaseFragment extends Fragment {

    protected Tracker mTracker;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mTracker.setScreenName(this.getClass().getName());
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        return inflater.inflate(getLayoutId(), container, false);
    }

    public void startCustomRequestWithLoader(CustomRequest customRequest, int loaderId) {
        startCustomRequestWithLoader(customRequest, loaderId, false);
    }

    public void startCustomRequestWithLoader(CustomRequest customRequest, int loaderId,
                                             boolean showProgressBar) {
        ((BaseActivity) getActivity()).startCustomRequestWithLoader(customRequest,
                                                                    loaderId,
                                                                    showProgressBar);
    }

    public void startCustomRequest(CustomRequest customRequest) {
        startCustomRequest(customRequest, false);
    }

    public void startCustomRequest(CustomRequest customRequest, boolean showProgressBar) {
        ((BaseActivity) getActivity()).startCustomRequest(customRequest, showProgressBar);
    }

    protected abstract int getLayoutId();

    public abstract void onHttpResult(HttpResult result);

}
