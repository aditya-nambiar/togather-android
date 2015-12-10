package com.togather.me.ui.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.JsonElement;
import com.togather.me.api.CustomErrorHandler;
import com.togather.me.api.QueryResolver;
import com.togather.me.model.GsonModels;
import com.togather.me.service.BaseHttpService;
import com.togather.me.service.CustomRequest;
import com.togather.me.service.HttpResult;
import com.togather.me.service.HttpResultListener;
import com.togather.me.togather.R;
import com.togather.me.ui.view.CustomAnimationProgressDialog;
import com.togather.me.util.Constants;
import com.togather.me.util.LogUtils;
import com.togather.me.util.PrefUtils;

import java.util.ArrayList;



/**
 * Created by mouli on 25/07/15.
 */
@SuppressWarnings("unused")
public class BaseActivity extends AppCompatActivity implements ServiceConnection,
        LoaderManager.LoaderCallbacks<Cursor>, HttpResultListener,
        com.togather.me.ui.utils.InterfaceUtils.DrawerOpenListener {
    private static String TAG = LogUtils.makeLogTag(BaseActivity.class);

    protected DrawerLayout mDrawerLayout;
    protected Toolbar mToolbar;
    protected boolean mShouldShowBackButton;
    protected Tracker mTracker;
    protected CustomAnimationProgressDialog mProgressDialog;
    protected boolean mUserLearnedDrawer;
    protected ActionBarDrawerToggle mDrawerToggle;

    protected ArrayList<CustomRequest> unboundRequestQueue;

    /**
     * http service bound to activity to perform get and put requests
     */
    private BaseHttpService mService;
    /**
     * variable that tells whether the bound service is present
     */
    private boolean mBound = false;

    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private ServiceConnection mConnection = this;

    public static Snackbar getSnackbar(Context context,
                                       View view,
                                       String infoText,
                                       View.OnClickListener listener,
                                       String actionText) {
        Snackbar snackbar = Snackbar.make(view, infoText, Snackbar.LENGTH_LONG);
        if (listener != null) {
            snackbar.setAction(actionText, listener);
            snackbar.setActionTextColor(com.togather.me.util.UIUtils.getColor(context, R.color.theme_primary));
        }
        snackbar.getView().setBackgroundColor(com.togather.me.util.UIUtils.getColor(context, R.color.crimson));
        return snackbar;
    }

    @Override
    protected void onStart() {
        unboundRequestQueue = new ArrayList<>();
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        BaseHttpService.registerHttpResultListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        /**
         * stopping the bound service when activity is stopped
         */
        if (!isChangingConfigurations() && (mService != null)) {
            mService.checkForTermination();
        }
        if (mBound) {
            //LOGD(TAG, "unbinding");
            unbindService(mConnection);
            mBound = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        BaseHttpService.unregisterHttpResultListener();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
      //  CityfloCustomerApplication application = (CityfloCustomerApplication) getApplication();
      //  mTracker = application.getDefaultTracker();
        mTracker.setScreenName(this.getLocalClassName());
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        GoogleAnalytics.getInstance(getBaseContext()).dispatchLocalHits();
        mProgressDialog = CustomAnimationProgressDialog.newInstance(this);

        mShouldShowBackButton =
                getIntent().getBooleanExtra(Constants.INTENT_KEY_SHOULD_SHOW_BACK_BUTTON,
                                            false);
        LogUtils.LOGD(TAG, "Start Service");

        Intent serviceIntent = new Intent(this, BaseHttpService.class);
        startService(serviceIntent);
        bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        mUserLearnedDrawer = sp.getBoolean(PrefUtils.PREF_KEY_USER_LEARNED_DRAWER, false);

        /*The order is IMPORTANT. first setUpToolbar() and then setDrawerFragment()*/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            mToolbar = toolbar;
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            }
        }

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawerLayout != null) {
            boolean fromSavedInstanceState = false;
            if (savedInstanceState != null) {
                fromSavedInstanceState = true;
            }
            setUpDrawer(drawerLayout, fromSavedInstanceState);
        }
    }

    private void setUpDrawer(DrawerLayout drawerLayout, boolean mFromSavedInstanceState) {

        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                mToolbar,
                R.string.drawer_open,
                R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                onDrawerToggle(false);
                if (!mUserLearnedDrawer) {
                    // The user manually opened the drawer; store this flag to prevent auto-showing
                    // the navigation drawer automatically in the future.
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(BaseActivity.this);
                    sp.edit().putBoolean(PrefUtils.PREF_KEY_USER_LEARNED_DRAWER, true).apply();
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                onDrawerToggle(true);
                if (!mUserLearnedDrawer) {
                    // The user manually opened the drawer; store this flag to prevent auto-showing
                    // the navigation drawer automatically in the future.
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(BaseActivity.this);
                    sp.edit().putBoolean(PrefUtils.PREF_KEY_USER_LEARNED_DRAWER, true).apply();
                }
            }
        };
        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(findViewById(R.id.left_drawer));
        }

        if (!mShouldShowBackButton) {
            // Defer code dependent on restoration of previous instance state.
            mDrawerLayout.post(new Runnable() {
                @Override
                public void run() {
                    mDrawerToggle.syncState();
                }
            });
            mDrawerLayout.setDrawerListener(mDrawerToggle);
        } else {
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.activity_slide_in, R.anim.activity_alpha_out);
    }

    @Override
    public void startActivity(Intent intent, Bundle options) {
        super.startActivity(intent, options);
        overridePendingTransition(R.anim.activity_slide_in, R.anim.activity_alpha_out);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        overridePendingTransition(R.anim.activity_slide_in, R.anim.activity_alpha_out);
    }

    @Override
    public void finish() {
        super.finish();
//        if (!(this instanceof RegisterActivity) &&
//                !(this instanceof SignInActivity) &&
//                !(this instanceof com.togather.me.ui.activity.BaseDrawerActivity))
//        overridePendingTransition(R.anim.activity_alpha_in, R.anim.activity_slide_out);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        overridePendingTransition(R.anim.activity_alpha_in, R.anim.activity_slide_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_alpha_in, R.anim.activity_slide_out);
    }

    protected void closeDrawer() {
        mDrawerLayout.closeDrawer(findViewById(R.id.left_drawer));
    }

    public void startCustomRequestWithLoader(CustomRequest customRequest, int loaderId) {
        startCustomRequestWithLoader(customRequest, loaderId, false);
    }

    public void startCustomRequest(CustomRequest customRequest, boolean showProgressBar) {
        if (showProgressBar && mProgressDialog != null) {
            mProgressDialog.show();
        }
        if (mService != null) {
            mService.newRequest(customRequest);
        } else {
            if (unboundRequestQueue == null) {
                unboundRequestQueue = new ArrayList<>();
            }
            if (!unboundRequestQueue.contains(customRequest)) {
                LogUtils.LOGD(TAG, "Adding to unbound queue: " + customRequest);

                unboundRequestQueue.add(customRequest);
            }
        }
    }

    public void startCustomRequest(CustomRequest customRequest) {
        startCustomRequest(customRequest, false);
    }

    public void startCustomRequestWithLoader(CustomRequest customRequest, int loaderId,
                                             boolean showProgressBar) {
        startCustomRequest(customRequest, showProgressBar);
        Bundle args = new Bundle();
        args.putParcelable(Constants.ARG_KEY_CUSTOM_REQUEST, customRequest);
        getSupportLoaderManager().initLoader(loaderId, args, this);
    }

    protected void startCursorLoader(CustomRequest customRequest, int loaderId) {
        Bundle args = new Bundle();
        args.putParcelable(Constants.ARG_KEY_CUSTOM_REQUEST, customRequest);
        getSupportLoaderManager().initLoader(loaderId, args, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CustomRequest request = args.getParcelable(Constants.ARG_KEY_CUSTOM_REQUEST);
        return QueryResolver.getLoaderFromRequest(this, request);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        final int loaderId = loader.getId();
        if (data == null) {
            return;
        }
        if (data.isClosed()) {
            return;
        }
//        List<JsonCache> jsonCaches = JsonCache.listFromCursor(data);
//        if (jsonCaches.isEmpty()) {
//            return;
//        }
//        final JsonCache jsonCache = jsonCaches.get(0);
//        String inputJson = jsonCache.getJson();
//        JsonUtils.parseJsonElement(inputJson, new JsonUtils.ParserCallback() {
//            @Override
//            public void onParseComplete(JsonElement jsonElement) {
//                LOGD(TAG, "jsonElement from url are " + jsonElement);
//                onParseJsonComplete(jsonElement, loaderId);
//            }
//        });
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    protected void onParseJsonComplete(JsonElement jsonElement, int loaderId) {

    }

    @Override
    public void onHttpResult(HttpResult httpResult) {
        if (httpResult.isError) {
            GsonModels.ErrorInfo errorInfo =
                    CustomErrorHandler.getErrorInfo(this, httpResult.retrofitError);
            View view = findViewById(R.id.main_container);
            if (view != null) {
                showSnackbar(view, errorInfo.getErrorMessage(), errorInfo.getDuration());
            }
        }
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onServiceConnected(ComponentName className,
                                   IBinder service) {
        // We've bound to LocalService, cast the IBinder and get LocalService instance
        LogUtils.LOGD(TAG, "Service Bound");

        BaseHttpService.HttpGetBinder binder = (BaseHttpService.HttpGetBinder) service;
        mService = binder.getService();
        mBound = true;
        for (CustomRequest request : unboundRequestQueue)
            mService.newRequest(request);
    }

    @Override
    public void onServiceDisconnected(ComponentName arg0) {
        mBound = false;
    }

    public void showSnackbar(View view, String infoText, View.OnClickListener listener,
                             String actionText) {
        Snackbar snackbar = getSnackbar(this, view, infoText, listener, actionText);
        snackbar.show();
    }

    public Snackbar getSnackbar(View view, String infoText) {
        return getSnackbar(this, view, infoText, null, null);
    }

    public void showSnackbar(View view, String infoText) {
        showSnackbar(view, infoText, null, null);
    }

    public void showSnackbar(View view, String infoText, int time) {
        Snackbar snackbar = getSnackbar(view, infoText);
        snackbar.setDuration(time);
        snackbar.show();
    }

    @Override
    public void onDrawerToggle(boolean isOpened) {

    }

    public ActionBarDrawerToggle getDrawerToggle() {
        return mDrawerToggle;
    }

    public DrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }
}
