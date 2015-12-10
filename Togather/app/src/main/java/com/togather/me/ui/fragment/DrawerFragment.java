package com.togather.me.ui.fragment;

import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.togather.me.model.GsonModels;
import com.togather.me.togather.R;
import com.togather.me.ui.view.DrawerMainSectionView;
import com.togather.me.util.LogUtils;
import com.togather.me.util.PrefUtils;
import com.togather.me.util.UIUtils;

public class DrawerFragment extends Fragment implements View.OnClickListener, SharedPreferences.OnSharedPreferenceChangeListener{
    private static String TAG = LogUtils.makeLogTag(DrawerFragment.class);

    private int mCurrentItem = -1;
    private Tracker mTracker;
    private PorterDuffColorFilter selectedColorFilter;

    public interface DrawerItemClickListener {
        void onDrawerItemClick(int id);
    }

    private static int mViewIds[] = new int[]{
//            R.id.iv_profile_pic,
//            R.id.tv_user_name,
//            R.id.tv_contact,
//            R.id.dmsv_current_booking,
//            R.id.dmsv_balance,
//            R.id.dmsv_history,
//            R.id.dmsv_share,
//            R.id.dmsv_logout,
//            R.id.dmsv_suggest,
//            R.id.dmsv_feedback,
//            R.id.dmsv_call_us,
//            R.id.dmsv_faq
    };
    private View mRootView;
    private DrawerItemClickListener mListener;

    public static DrawerFragment newInstance(){
        DrawerFragment fragment = new DrawerFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int resId = getLayoutResId();
        return inflater.inflate(resId, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        selectedColorFilter = new PorterDuffColorFilter(
                UIUtils.getColor(getContext(), R.color.theme_blue), PorterDuff.Mode.SRC_IN);
       // mTracker = ((CityfloCustomerApplication) getActivity().getApplication()).getDefaultTracker();
        mTracker.setScreenName(TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mRootView = view;
        if(mCurrentItem != -1){
            setCurrentItem(mCurrentItem);
        }
        setUpClickListeners(view);
    }

    @Override
    public void onStart() {
        super.onStart();
        PrefUtils.getSharedPreferences(getActivity()).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        PrefUtils.getSharedPreferences(getActivity()).unregisterOnSharedPreferenceChangeListener(this);
    }

    public void setListener(DrawerItemClickListener listener) {
        mListener = listener;
    }

    private void setUpClickListeners(View view) {
        setUpDrawerAccount(view, PrefUtils.getObjectFromPrefs(PrefUtils.PREF_KEY_USER_ACCOUNT,
                GsonModels.UserAccount.class, getActivity()));
//        view.findViewById(R.id.dmsv_balance).setOnClickListener(this);
//        view.findViewById(R.id.dmsv_history).setOnClickListener(this);
//        view.findViewById(R.id.dmsv_logout).setOnClickListener(this);
//        view.findViewById(R.id.dmsv_share).setOnClickListener(this);
//        view.findViewById(R.id.dmsv_current_booking).setOnClickListener(this);
//        view.findViewById(R.id.dmsv_home).setOnClickListener(this);
//        view.findViewById(R.id.dmsv_suggest).setOnClickListener(this);
//        view.findViewById(R.id.dmsv_feedback).setOnClickListener(this);
//        view.findViewById(R.id.dmsv_call_us).setOnClickListener(this);
//        view.findViewById(R.id.dmsv_faq).setOnClickListener(this);
    }

    private void setUpDrawerAccount(View view, GsonModels.UserAccount userAccount) {
        DrawerAccountHolder holder = new DrawerAccountHolder(view);
        if(userAccount!=null) {
            holder.tvUserName.setText(userAccount.getFirstName() + " " + userAccount.getLastName());
            holder.tvContact.setText(userAccount.getPhoneNumber());
        }
    }

    private int getLayoutResId() {
        return R.layout.fragment_drawer;
    }

    /**
     * Retrieves the id of the clicked view and calls the listener with id
     *
     * @param v View that is clicked
     */
    @Override
    public void onClick(View v) {
        if (mListener != null) {
            mListener.onDrawerItemClick(v.getId());
        }
    }

    public void setCurrentItem(int viewId) {
        if(mRootView != null) {
            for (int mViewId : mViewIds) {
                View view = mRootView.findViewById(mViewId);
                if (view != null && view instanceof DrawerMainSectionView) {
                    ((ImageView) view.findViewById(R.id.iv_main_section_icon)).setColorFilter(null);
                    ((TextView) view.findViewById(R.id.tv_main_section_name)).setTextColor(
                            UIUtils.getColor(getContext(), R.color.black));
                }
            }
            View view = mRootView.findViewById(viewId);
            ((ImageView) view.findViewById(R.id.iv_main_section_icon)).setColorFilter(
                    selectedColorFilter);
            ((TextView) view.findViewById(R.id.tv_main_section_name)).setTextColor(
                    UIUtils.getColor(getContext(), R.color.theme_blue));
        } else{
            mCurrentItem = viewId;
        }
    }

    private class DrawerAccountHolder {
        ImageView ivProfilePic;
        TextView tvUserName;
        TextView tvContact;

        public DrawerAccountHolder(View view) {
//            ivProfilePic = (ImageView) view.findViewById(R.id.iv_profile_pic);
//            tvContact = (TextView) view.findViewById(R.id.tv_contact);
//            tvUserName = (TextView) view.findViewById(R.id.tv_user_name);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(PrefUtils.PREF_KEY_CREDIT_BALANCE)){
            int balance = sharedPreferences.getInt(key, 0);
//            DrawerMainSectionView view = (DrawerMainSectionView) mRootView.findViewById(R.id.dmsv_balance);
//            if(view!=null) {
//                ((TextView) view.findViewById(R.id.tv_balance_value)).setText(
//                        getString(R.string.rupee) + " " + balance+"");
//            }
        }

    }
}
