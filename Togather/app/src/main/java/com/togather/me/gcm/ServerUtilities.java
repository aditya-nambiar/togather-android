package com.togather.me.gcm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.togather.me.service.BaseHttpService;
import com.togather.me.ui.activity.BaseActivity;
import com.togather.me.util.LogUtils;
import com.togather.me.util.PackageManagerUtils;
import com.togather.me.api.ApiEndPoints;


/**
 * Created by Aditya
 */
public class ServerUtilities {

    private static final String LOG_TAG = LogUtils.makeLogTag("GCMs");

    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";

    /**
     * Gets the current registration ID for application on GCM service.
     * <p>
     * If result is empty, the app needs to register.
     *    public static final String GCM_DEREGISTER_URL = "/api/push-notifs/gcm/deregister-android-device/";

     * @return registration ID, or empty string if there is no existing
     *         registration ID.
     */
    public static String getRegistrationId(final Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            LogUtils.LOGI(LOG_TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing registration ID is not guaranteed to work with
        // the new app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = PackageManagerUtils.getAppVersion(context);
        if (registeredVersion != currentVersion) {
            LogUtils.LOGI(LOG_TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    /**
     * Saving the RegID on our server for pushing notifications
     * @param context
     * @param regID
     */
    public static void registerOnServer(final Context context,final String regID){
        String[] params = {regID};
        Intent intent =  new Intent(context,BaseHttpService.class);
        intent.putExtra("request_id", ApiEndPoints.GCM_REGISTER_REQUEST_ID);
        intent.putExtra("url", ApiEndPoints.BASE_URL + ApiEndPoints.GCM_REGISTER_URL);
        intent.putExtra("params",params);
        context.startService(intent);
    }

    /**
     * Unregister this account/device pair within the server.
     *
     * @param context Current context
     * @param regID   The GCM registration ID for this device
     */
    static void unRegisterOnServer(final Context context, final String regID) {

        LogUtils.LOGI(LOG_TAG, "unregistering device (regID = " + regID + ")");
        String[] params = {regID};
        Intent intent =  new Intent(context,BaseHttpService.class);
//        intent.putExtra("request_id", com.togather.me.gcm.GCMConstants.GCM_SERVER_DISCONNECT_ID);
//        intent.putExtra("url", com.togather.me.gcm.GCMConstants.SERVER_BASE_URL);
        intent.putExtra("params",params);
        context.startService(intent);
    }

    /**
     * Unregister this account/device pair within the server.
     *
     * @param context Current context
     * @param regID   The GCM registration ID for this device
     */
    static boolean isRegisteredOnServer(final Context context, final String regID) {

        // TODO create corresponding retrofit calls, this requires callback
        String[] params = {regID};
        Intent intent =  new Intent(context,BaseHttpService.class);
//        intent.putExtra("request_id", com.cityflo.customer.gcm.GCMConstants.GCM_SERVER_DISCONNECT_ID);
//        intent.putExtra("url", com.cityflo.customer.gcm.GCMConstants.SERVER_BASE_URL);
        intent.putExtra("params",params);
        context.startService(intent);

        return false;
    }

    /**
     * Save RegId in sharedPref
     * @param context
     * @param regId
     */
    public static void saveRegId(final Context context, final String regId){
        // We do not want to do the register process everytime
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = PackageManagerUtils.getAppVersion(context);
        LogUtils.LOGI(LOG_TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.apply();
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private static SharedPreferences getGCMPreferences(Context context) {
        return context.getSharedPreferences(BaseActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

}
