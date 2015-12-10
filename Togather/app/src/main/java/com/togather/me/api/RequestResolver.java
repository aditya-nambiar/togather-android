package com.togather.me.api;

import android.content.Context;

import com.togather.me.service.CustomRequest;
import com.togather.me.service.HttpResult;
import com.togather.me.util.LogUtils;

import retrofit.RetrofitError;

import static com.togather.me.util.LogUtils.LOGD;

public class RequestResolver {
    private static final String TAG = LogUtils.makeLogTag(RequestResolver.class);

    public static HttpResult getByRequestId(CustomRequest customRequest, Context context) {
        HttpResult httpResult = new HttpResult();
        httpResult.request = customRequest;
        try {
            switch (customRequest.getRequestId()) {
//                case com.cityflo.customer.api.ApiEndPoints.GET_UPCOMING_RIDES_LIST_REQUEST_ID:
//                    com.cityflo.customer.api.RetrofitInterface.GetUpcomingVehicleRidesClient getUpcomingVehicleRidesClient =
//                            com.cityflo.customer.api.ServiceGenerator.createService(
//                                    com.cityflo.customer.api.RetrofitInterface.GetUpcomingVehicleRidesClient.class,
//                                    com.cityflo.customer.api.ApiEndPoints.BASE_URL,
//                                    context
//                            );
//                    String[] upcomingRideParams = customRequest.getParams();
//                    GsonModels.UpcomingVehicleRidesList upcomingVehicleRidesList =
//                            getUpcomingVehicleRidesClient.getUpcomingRides(Integer.valueOf(upcomingRideParams[0]));
//                    httpResult.successResponse = upcomingVehicleRidesList;
//                    saveUpcomingRidesList(upcomingVehicleRidesList.getUpcomingVehicleRideList(), context);
//                    break;
//
//                case com.cityflo.customer.api.ApiEndPoints.RUNNING_ROUTES_MORNING_REQUEST_ID:
//                    com.cityflo.customer.api.RetrofitInterface.RunningRoutesMorningClient  runningRoutesMorningClient =
//                            com.cityflo.customer.api.ServiceGenerator.createService(
//                                    com.cityflo.customer.api.RetrofitInterface.RunningRoutesMorningClient.class,
//                                    com.cityflo.customer.api.ApiEndPoints.BASE_URL,
//                                    context
//                            );
//
//
//                    GsonModels.RunningRoutesWithStopsList runningRoutesMorningList =
//                            runningRoutesMorningClient.getRunningRoutes();
//                    httpResult.successResponse = runningRoutesMorningList;
//
//                    saveRunningRoutesList(runningRoutesMorningList.getRunningRoutes(), context, 1);
//                    break;
//                case com.cityflo.customer.api.ApiEndPoints.RUNNING_ROUTES_EVENING_REQUEST_ID:
//                    com.cityflo.customer.api.RetrofitInterface.RunningRoutesEveningClient runningRoutesEveningClient =
//                            com.cityflo.customer.api.ServiceGenerator.createService(
//                                    com.cityflo.customer.api.RetrofitInterface.RunningRoutesEveningClient.class,
//                                    com.cityflo.customer.api.ApiEndPoints.BASE_URL,
//                                    context
//                            );
//
//
//                    GsonModels.RunningRoutesWithStopsList runningRoutesEveningList =
//                            runningRoutesEveningClient.getRunningRoutes();
//                    httpResult.successResponse = runningRoutesEveningList;
//
//                    saveRunningRoutesList(runningRoutesEveningList.getRunningRoutes(), context, 2);
//                    break;
//                case com.cityflo.customer.api.ApiEndPoints.UPCOMING_BUSES_FROM_STOPS_ID:
//                    com.cityflo.customer.api.RetrofitInterface.UpcomingBusesFromStops busRideClient =
//                            com.cityflo.customer.api.ServiceGenerator.createService(
//                                    com.cityflo.customer.api.RetrofitInterface.UpcomingBusesFromStops.class,
//                                    com.cityflo.customer.api.ApiEndPoints.BASE_URL,
//                                    context
//                            );
//
//                    String[] requestParams = customRequest.getParams();
//                    GsonModels.BusRideList busRideList =
//                            busRideClient.getUpcomingBusesFromStops(Integer.valueOf(requestParams[0]), Integer.valueOf(requestParams[1]), Integer.valueOf(requestParams[2]));
//                    httpResult.successResponse = busRideList;
//                    break;
//                case com.cityflo.customer.api.ApiEndPoints.GET_CREDIT_BALANCE_REQUEST_ID:
//                    com.cityflo.customer.api.RetrofitInterface.VerbClient getCreditBalanceClient = com.cityflo.customer.api.ServiceGenerator.createService(
//                            com.cityflo.customer.api.RetrofitInterface.VerbClient.class,
//                            customRequest.getUrl(),
//                            context
//                    );
//
//                    JsonElement creditsJsonElement = getCreditBalanceClient.getData();
//                    saveCreditBalance(creditsJsonElement, context);
//                    break;
//                case com.cityflo.customer.api.ApiEndPoints.GET_BUS_LOCATION_REQUEST_ID:
//                    com.cityflo.customer.api.RetrofitInterface.VerbClient getBusLocationClient = com.cityflo.customer.api.ServiceGenerator.createService(
//                            com.cityflo.customer.api.RetrofitInterface.VerbClient.class,
//                            customRequest.getUrl(),
//                            context);
//
//                    JsonElement busLocationElement = getBusLocationClient.postData(customRequest.getJsonBody());
//                    httpResult.successResponse = busLocationElement;
//                    break;
//                case com.cityflo.customer.api.ApiEndPoints.LOGIN_REQUEST_ID:
//                    com.cityflo.customer.api.RetrofitInterface.LoginClient loginClient = com.cityflo.customer.api.ServiceGenerator.createService(
//                            com.cityflo.customer.api.RetrofitInterface.LoginClient.class,
//                            com.cityflo.customer.api.ApiEndPoints.BASE_URL,
//                            context
//                    );
//                    JsonElement loginResponse = loginClient.login(customRequest.getJsonBody());
//                    httpResult.successResponse = loginResponse;
//                    saveGetData(loginResponse, customRequest, context);
//                    break;
//                case com.cityflo.customer.api.ApiEndPoints.IS_CAMPAIGN_LIVE_REQUEST_ID:
//                case com.cityflo.customer.api.ApiEndPoints.GET_REQUEST_ID:
//                case com.cityflo.customer.api.ApiEndPoints.GET_REFERRAL_DATA_ID:
//                    com.cityflo.customer.api.RetrofitInterface.VerbClient verbClient = com.cityflo.customer.api.ServiceGenerator.createService(
//                            com.cityflo.customer.api.RetrofitInterface.VerbClient.class,
//                            customRequest.getUrl(),
//                            context
//                    );
//
//                    JsonElement jsonElement = verbClient.getData();
//                    httpResult.successResponse = jsonElement;
//                    saveGetData(jsonElement, customRequest, context);
//                    break;
//                case com.cityflo.customer.api.ApiEndPoints.GCM_REGISTER_REQUEST_ID:
//                    com.cityflo.customer.api.RetrofitInterface.VerbClient gcmRegisterClient = com.cityflo.customer.api.ServiceGenerator.createService(
//                            com.cityflo.customer.api.RetrofitInterface.VerbClient.class,
//                            customRequest.getUrl(),
//                            context
//                    );
//
//                    JsonObject gcmParams = new JsonObject();
//                    gcmParams.addProperty("reg_id", customRequest.getParams()[0]);
//                    gcmRegisterClient.postData(gcmParams);
//                    PrefUtils.putBoolean(context, PrefUtils.PREF_KEY_IS_GCM_REGISTERED, true);
//                    break;
//                case com.cityflo.customer.api.ApiEndPoints.GET_RECENT_RIDE_MORNING_ID:
//                    com.cityflo.customer.api.RetrofitInterface.RecentRide recentRideMorningClient =  com.cityflo.customer.api.ServiceGenerator.createService(
//                            com.cityflo.customer.api.RetrofitInterface.RecentRide.class,
//                            com.cityflo.customer.api.ApiEndPoints.BASE_URL,
//                            context
//                    );
//
//                    String[] params1 = customRequest.getParams();
//                    JsonElement jsonElem1 = recentRideMorningClient.getData(params1[0]);
//                    httpResult.successResponse = jsonElem1;
//                    break;
//                case com.cityflo.customer.api.ApiEndPoints.GET_RECENT_RIDE_EVENING_ID:
//                    com.cityflo.customer.api.RetrofitInterface.RecentRide recentRideEveningClient = com.cityflo.customer.api.ServiceGenerator.createService(
//                            com.cityflo.customer.api.RetrofitInterface.RecentRide.class,
//                            com.cityflo.customer.api.ApiEndPoints.BASE_URL,
//                            context
//                    );
//                    String[] params = customRequest.getParams();
//                    JsonElement jsonElem = recentRideEveningClient.getData((params[0]));
//                    httpResult.successResponse = jsonElem;
//                    break;
//                case com.cityflo.customer.api.ApiEndPoints.SUBMIT_FEEDBACK_REQUEST_ID:
//                case com.cityflo.customer.api.ApiEndPoints.SUGGEST_ROUTE_REQUEST_ID:
//                case com.cityflo.customer.api.ApiEndPoints.VERIFY_OTP_REQUEST_ID:
//                case com.cityflo.customer.api.ApiEndPoints.BOOK_RIDE_REQUEST_ID:
//                case com.cityflo.customer.api.ApiEndPoints.REGISTER_REQUEST_ID:
//                case com.cityflo.customer.api.ApiEndPoints.POST_REQUEST_ID:
//                case com.cityflo.customer.api.ApiEndPoints.FORGOT_PASSWORD_SEND_MOBILE_ID:
//                    com.cityflo.customer.api.RetrofitInterface.VerbClient postClient = com.cityflo.customer.api.ServiceGenerator.createService(
//                            com.cityflo.customer.api.RetrofitInterface.VerbClient.class,
//                            customRequest.getUrl(),
//                            context
//                    );
//                    JsonElement postResponse = postClient.postData(customRequest.getJsonBody());
//                    httpResult.successResponse = postResponse;
//                    savePostData(postResponse, customRequest, context);
//                    break;
                default:
                    if (customRequest.getUrl().isEmpty()) {
                        LOGD(TAG, "Incorrect Request");
                    }
                    break;
            }
        } catch (RetrofitError error) {
            httpResult.isError = true;
            httpResult.successResponse = error.getResponse();
            httpResult.retrofitError = error;
        }
        return httpResult;

    }



//    private static void saveGetData(JsonElement jsonElement, CustomRequest customRequest, Context context) {
//        if(jsonElement == null || jsonElement.isJsonNull()){
//            return;
//        }
//        JsonCache jsonCache;
//        String submitUrl = customRequest.getUrl();
//        Cursor cursor = context.getContentResolver()
//                .query(ConsumerProvider.JSON_CACHE_CONTENT_URI, null,
//                        JsonCacheTable.WHERE_URL_EQUALS, new String[]{submitUrl}, null);
//        List<JsonCache> jsonCaches = JsonCache.listFromCursor(cursor);
//        cursor.close();
//        if (jsonCaches.size()>0) {
//            jsonCache = jsonCaches.get(0);
//            jsonCache.setJson(jsonElement.toString());
//            jsonCache.setSubmitStatus(false);
//            context.getContentResolver().update(
//                    ConsumerProvider.JSON_CACHE_CONTENT_URI, jsonCache.getContentValues(),
//                    JsonCacheTable.WHERE_URL_EQUALS, new String[]{submitUrl});
//        } else {
//            jsonCache = new JsonCache();
//            jsonCache.setRequestId(customRequest.getRequestId());
//            jsonCache.setUrl(submitUrl);
//            jsonCache.setJson(jsonElement.toString());
//            jsonCache.setDbId(customRequest.getRequestId());
//            jsonCache.setSubmitStatus(false);
//
//            context.getContentResolver().insert(ConsumerProvider.JSON_CACHE_CONTENT_URI,
//                    jsonCache.getContentValues());
//        }
//    }

//    private static void savePostData(JsonElement jsonElement, CustomRequest customRequest, Context context) {
//        if(jsonElement == null || jsonElement.isJsonNull()){
//            return;
//        }
//        JsonCache jsonCache;
//        String submitUrl = customRequest.getUrl();
//        Cursor cursor = context.getContentResolver()
//                .query(ConsumerProvider.JSON_CACHE_CONTENT_URI, null,
//                        JsonCacheTable.WHERE_URL_EQUALS, new String[]{submitUrl}, null);
//        List<JsonCache> jsonCaches = JsonCache.listFromCursor(cursor);
//        cursor.close();
//        if (jsonCaches.size()>0) {
//            jsonCache = jsonCaches.get(0);
//            jsonCache.setJson(jsonElement.toString());
//            jsonCache.setSubmitStatus(false);
//            context.getContentResolver().update(
//                    ConsumerProvider.JSON_CACHE_CONTENT_URI, jsonCache.getContentValues(),
//                    JsonCacheTable.WHERE_URL_EQUALS, new String[]{submitUrl});
//        } else {
//            jsonCache = new JsonCache();
//            jsonCache.setRequestId(customRequest.getRequestId());
//            jsonCache.setUrl(submitUrl);
//            jsonCache.setJson(jsonElement.toString());
//            jsonCache.setDbId(customRequest.getRequestId());
//            jsonCache.setSubmitStatus(false);
//
//            context.getContentResolver().insert(ConsumerProvider.JSON_CACHE_CONTENT_URI,
//                    jsonCache.getContentValues());
//        }
//    }


}
