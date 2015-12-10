package com.togather.me.api;

public class ApiEndPoints {

    public static String BASE_URL = "https://cityflo.com";

    public static final int REGISTER_REQUEST_ID = 100;
    public static String REGISTER_URL = "/api/users/customers/";

    public static final int LOGIN_REQUEST_ID = 101;
    public static final String LOGIN_URL = "/api/users/login/";

    public static final int GET_UPCOMING_RIDES_LIST_REQUEST_ID = 102;
    public static final String GET_UPCOMING_RIDES_LIST_URL = "/api/rides/get-upcoming-vehicle-rides/";

    public static final int GET_CREDIT_BALANCE_REQUEST_ID = 103;
    public static final String GET_CREDIT_BALANCE_URL = "/api/users/get-customer-credits-balance/";

    public static final int BOOK_RIDE_REQUEST_ID = 104;
    public static final String BOOK_RIDE_URL = "/api/rides/book-customer-ride/";

    public static final int VERIFY_OTP_REQUEST_ID = 105;
    public static final String VERIFY_OTP_URL = "/api/users/verify-otp/";

    public static final int GET_BUS_LOCATION_REQUEST_ID = 106;
    public static final String GET_BUS_LOCATION_URL = "/api/vehicles/get-latest-vehicle-location/";

    public static final int RESET_OTP_REQUEST_ID = 106;
    public static final String RESET_OTP_URL = "/api/users/resend-otp/";

    public static final int GCM_REGISTER_REQUEST_ID = 107;
    public static final String GCM_REGISTER_URL = "/api/push-notifs/register-android-device/";

    public static final int IS_CAMPAIGN_LIVE_REQUEST_ID = 108;
    public static final String IS_CAMPAIGN_LIVE_URL = "/campaigns/is-live-campaign/";

    public static final int SUGGEST_ROUTE_REQUEST_ID = 109;
    public static final String SUGGEST_ROUTE_URL = "/api/routes/route-suggestion/";

    public static final int RUNNING_ROUTES_REQUEST_ID = 110;
    public static final String RUNNING_ROUTES_URL = "/api/routes/running-routes/";

    public static final int SUBMIT_FEEDBACK_REQUEST_ID = 111;
    public static final String SUBMIT_FEEDBACK_URL = "/api/users/submit-customer-feedback/";

    public static final int RUNNING_ROUTES_MORNING_REQUEST_ID = 112;
    public static final String RUNNING_ROUTES_MORNING_URL = "/api/routes/running-routes-morning/";

    public static final int RUNNING_ROUTES_EVENING_REQUEST_ID = 113;
    public static final String RUNNING_ROUTES_EVENING_URL = "/api/routes/running-routes-evening/";

    public static final int UPCOMING_BUSES_FROM_STOPS_ID = 114;
    public static final String UPCOMING_BUSES_FROM_STOPS_URL = "/api/rides/get-upcoming-buses-from-stops/";

    public static final int GET_RECENT_RIDE_MORNING_ID = 115;
    public static final int GET_RECENT_RIDE_EVENING_ID = 116;

    public static final String GET_RECENT_RIDE_URL = "/api/rides/get-most-recent-ride/";

    public static final int GET_REFERRAL_DATA_ID = 118;
    public static final String GET_REFERRAL_DATA_URL = "/api/users/get-referral-data/";

    public static final int FORGOT_PASSWORD_SEND_MOBILE_ID = 117;
    public static final String FORGOT_PASSWORD_SEND_MOBILE_URL = "/api/users/forgot-password/";


    public static final int GET_REQUEST_ID = 2001;
    public static final int POST_REQUEST_ID = 2002;
    public static final int PUT_REQUEST_ID = 2003;

}

