package com.togather.me.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.togather.me.service.HttpResult;
import com.togather.me.R;
import com.togather.me.ui.fragment.DrawerFragment;
import com.togather.me.util.LogUtils;

/**
 * Created by Aditya
 */
public abstract class BaseDrawerActivity extends BaseActivity
        implements DrawerFragment.DrawerItemClickListener {
    private static String TAG = LogUtils.makeLogTag(BaseDrawerActivity.class);

    protected DrawerFragment mDrawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_drawer);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        LinearLayout llLayoutContainer = (LinearLayout) findViewById(R.id.ll_layout_container);
        View layoutView = LayoutInflater.from(this).inflate(getLayoutId(), null);
        llLayoutContainer.addView(layoutView);
        setUpDrawerClickListener();
        initViews();
        initData();
        setCurrentItem();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mDrawerFragment != null) {
            mDrawerFragment.setListener(this);
            setCurrentItem();
        }
    }

    private void setUpDrawerClickListener() {
        mDrawerFragment = DrawerFragment.newInstance();
        mDrawerFragment.setListener(this);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.left_drawer, mDrawerFragment).commit();
    }

    @Override
    public void onDrawerItemClick(int id) {
        switch (id) {

        }
        mDrawerFragment.setCurrentItem(id);
        closeDrawer();
    }

    @Override
    public void onDrawerToggle(boolean isOpened) {
        super.onDrawerToggle(isOpened);
    }

    private void setCurrentItem() {
//        if (this instanceof RunningRoutesActivity) {
//            mDrawerFragment.setCurrentItem(R.id.dmsv_home);
//        } else if (this instanceof BalanceActivity) {
//            mDrawerFragment.setCurrentItem(R.id.dmsv_balance);
//        } else if (this instanceof BookingHistoryActivity) {
//            mDrawerFragment.setCurrentItem(R.id.dmsv_current_booking);
//        } else if (this instanceof SuggestRouteActivity) {
//            mDrawerFragment.setCurrentItem(R.id.dmsv_suggest);
//        } else if (this instanceof FeedbackActivity) {
//            mDrawerFragment.setCurrentItem(R.id.dmsv_feedback);
//        } else if (this instanceof ReferralActivity) {
//            mDrawerFragment.setCurrentItem(R.id.dmsv_share);
//        } else if (this instanceof FaqActivity) {
//            mDrawerFragment.setCurrentItem(R.id.dmsv_faq);
//        }
    }

    @Override
    public void onHttpResult(HttpResult httpResult) {
        super.onHttpResult(httpResult);
    }

    protected abstract int getLayoutId();

    protected abstract void initViews();

    protected abstract void initData();

//    private boolean shouldKillCurrent() {
//        return (this instanceof BookingHistoryActivity) ||
//               (this instanceof BalanceActivity) ||
//               (this instanceof SuggestRouteActivity) ||
//               (this instanceof FeedbackActivity) ||
//               (this instanceof ReferralActivity) ||
//               (this instanceof FaqActivity);
//    }
}
