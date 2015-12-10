package com.togather.me.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;

import com.google.gson.Gson;


public class PrefUtils {
    private static final String TAG = LogUtils.makeLogTag(PrefUtils.class);
    /**
     * Keys of the shared preferences stored
     */
    // Location
    public static String PREF_KEY_USER_LEARNED_DRAWER = "pref_key_user_learned_drawer";
    public static final String PREF_KEY_LOCATION = "PREF_KEY_LOCATION";
    public static final String PREF_KEY_LOCATION_STATUS = "PREF_KEY_LOCATION_STATUS";
    public static final String PREF_KEY_ADDRESS = "PREF_KEY_ADDRESS";
    public static final String PREF_KEY_ADDRESS_STATUS = "PREF_KEY_ADDRESS_STATUS";
    public static final String PREF_KEY_IS_LOGGED_IN = "pref_key_is_logged_in";
    public static final String PREF_KEY_TOKEN = "pref_key_token";
    public static final String PREF_KEY_USER_ACCOUNT = "pref_key_user_account";
    public static final String PREF_KEY_CREDIT_BALANCE = "pref_key_credit_balance";
    public static final String PREF_KEY_OTP = "pref_key_OTP";
    public static final String PREF_KEY_BOOKED_RIDE = "pref_key_booked_ride";
    public static final String PREF_KEY_START_TIME_RIDE = "pref_key_start_time_ride";
    public static final String PREF_KEY_BOOKED_RIDE_START_STOP_PK = "pref_key_booked_ride_start_stop_pk";
    public static final String PREF_KEY_BOOKED_RIDE_END_STOP_PK = "pref_key_booked_ride_end_stop_pk";
    public static final String PREF_KEY_HAS_BOOKED_RIDE = "pref_key_is_booked_ride";
    public static final String PREF_KEY_CAN_CANCEL_RIDE = "pref_key_can_cancel_ride";
    public static final String PREF_KEY_SHOULD_REQUEST_BUS_LOCATION = "pref_key_should_request_bus_location";
    public static final String PREF_KEY_IS_GCM_REGISTERED = "pref_key_is_gcm_registered";
    public static final String PREF_KEY_VEHICLE_BOOKED = "pref_key_vehicle_booked";
    public static final String PREF_KEY_STARTSTOP_INFO = "pref_key_startstop_info";
    public static final String PREF_KEY_ENDSTOP_INFO = "pref_key_endstop_info";
    public static final String PREF_KEY_LOADED_ROUTES_MORNING = "pref_key_loaded_routes_morning";
    public static final String PREF_KEY_LOADED_ROUTES_EVENING = "pref_key_loaded_routes_evening";
    public static final String PREF_KEY_REFFERAL_CODE = "pref_key_refferal_code";
    public static final String PREF_KEY_TIME_OF_ROUTE_LOAD = "pref_key_time_of_route_load";


    /**
     * Constant string for file name to store the SharedPreferences of the
     * application. This is required to get the same SharedPreferences object
     * regardless of the package name of the class
     */
    private static final String FILE_NAME = "in.togather.android.default.prefs";

    /**
     * static preference manager that is returned when any class calls
     * for a preference manager
     */
    private static SharedPreferences mSharedPreferences;

    /**
     * returns same SharedPrefs
     * through out the application
     *
     * @param context context
     * @return SharedPreference object
     */
    public static SharedPreferences getSharedPreferences(Context context) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(FILE_NAME,
                    Context.MODE_PRIVATE);
        }
        return mSharedPreferences;
    }

    /**
     * convenience method to add preference listener
     * @param listener preference listener
     * @param context context
     */
    public static void addOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener, Context context) {
        getSharedPreferences(context).registerOnSharedPreferenceChangeListener(listener);
    }

    /**
     * convenience method to remove preference listener
     * @param listener preference listener
     * @param context context
     */
    public static void removeOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener, Context context) {
        getSharedPreferences(context).unregisterOnSharedPreferenceChangeListener(listener);
    }

    /**
     * converts any given object to json and
     * saves to shared prefs with given key
     *
     * @param prefKey key to be saved to
     * @param object  to be saved
     * @param context context
     * @return true if success
     */
    public static boolean saveObjectToPrefs(String prefKey, Object object, Context context) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        try {
            Gson gson = new Gson();
            String json = gson.toJson(object);
            LogUtils.LOGD(TAG, "Json: " + json);
            editor.putString(prefKey, json);
            editor.apply();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * returns an object stored in preferences
     * if the key is not present or the type of
     * object is wrong returns null
     *
     * @param prefKey key of the required object
     * @param context context
     * @param <T>     type of object to be returned
     * @return object if present else null
     */
    public static <T> T getObjectFromPrefs(String prefKey, Class<T> type, Context context) {
        String json = getSharedPreferences(context).getString(prefKey, null);
        if(json != null) {
            try {
                Gson gson = new Gson();
                T result = gson.fromJson(json, type);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * convenience method to save location
     *
     * @param location location to be saved
     * @param context  context
     * @return true if success
     */
    public static boolean saveLocation(Location location, Context context) {
        return saveObjectToPrefs(PREF_KEY_LOCATION, location, context);
    }

    /**
     * convenience method to get location
     *
     * @param context context
     * @return location if exists else null
     */
    public static Location getLocation(Context context) {
        return getObjectFromPrefs(PREF_KEY_LOCATION, Location.class, context);
    }

    /**
     * convenience method to save address
     *
     * @param address address to be saved
     * @param context context
     */
    public static void saveAddress(String address, Context context) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_KEY_ADDRESS, address);
        editor.apply();
    }

    /**
     * convenience method to get the address
     *
     * @param context context
     * @return address if exists else null
     */
    public static String getAddress(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getString(PREF_KEY_ADDRESS, null);
    }

    /**
     * convenience method set the location status
     *
     * @param locationStatus location status to be set
     * @param context        context
     */
    public static void saveLocationStatus(int locationStatus, Context context) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt(PREF_KEY_LOCATION_STATUS, locationStatus);
        editor.apply();
    }



    /**
     * convenience method set the address status
     *
     * @param addressStatus address status to be set
     * @param context       context
     */
    public static void saveAddressStatus(int addressStatus, Context context) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt(PREF_KEY_ADDRESS_STATUS, addressStatus);
        editor.apply();
    }


    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getBoolean(Context context, String key, boolean defValue) {
        return getSharedPreferences(context).getBoolean(key, defValue);
    }

    public static boolean getBoolean(Context context, String key) {
        return getBoolean(context, key, false);
    }

    public static void putInt(Context context, String key, int value) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int getInt(Context context, String key, int defValue) {
        return getSharedPreferences(context).getInt(key, defValue);
    }

    public static int getInt(Context context, String key) {
        return getInt(context, key, -1);
    }

    public static void putString(Context context, String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getString(Context context, String key, String defValue) {
        return getSharedPreferences(context).getString(key, defValue);
    }

    public static String getString(Context context, String key) {
        return getString(context, key, "");
    }
}
