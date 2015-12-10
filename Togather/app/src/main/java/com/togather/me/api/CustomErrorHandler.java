package com.togather.me.api;

import android.content.Context;
import android.support.design.widget.Snackbar;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.togather.me.model.GsonModels;
import com.togather.me.util.LogUtils;

import retrofit.RetrofitError;
import retrofit.mime.TypedByteArray;

/**
 * Created by mouli on 8/24/15.
 */
public class CustomErrorHandler {
    private static String TAG = LogUtils.makeLogTag(CustomErrorHandler.class);
    private static final int HTTP_STATUS_UNAUTHORISED = 401;

    public static GsonModels.ErrorInfo getErrorInfo(Context context, RetrofitError error) {
        GsonModels.ErrorInfo errorInfo = new GsonModels.ErrorInfo();
        String errorMsg = "Unknown error";
        errorInfo.setDuration(Snackbar.LENGTH_LONG);
        if (error.getKind().equals(RetrofitError.Kind.NETWORK)) {
            errorMsg = "Network error. Please check your connection";
            errorInfo.setDuration(Snackbar.LENGTH_INDEFINITE);
        } else if(error.getKind().equals(RetrofitError.Kind.HTTP)) {
            if(4==(error.getResponse().getStatus()/100)) {
                errorMsg = parseError(context, error);
            }
        } else if (error.getKind().equals(RetrofitError.Kind.CONVERSION)) {
            errorMsg = "Sorry! Parse Error";
        } else if (error.getKind().equals(RetrofitError.Kind.UNEXPECTED)) {
            errorMsg = "Unknown error";
        }
        errorInfo.setErrorMessage(errorMsg);
        return errorInfo;
    }

    private static String getErrorByHttpStatusCode(int status){
        switch(status){
            case HTTP_STATUS_UNAUTHORISED:
                return "Unauthorised";
            case 404:
                return "404 Not found";
            case 502:
                return "Server Down";
            case 504:
                return "Gateway Timed out";
            default:
                return "Unknown Error";
        }
    }

    private static String parseError(Context context, RetrofitError error) {
        String response = new String(((TypedByteArray) error.getResponse().getBody()).getBytes());
        try {
            JsonElement jsonElement = new Gson().fromJson(response, JsonElement.class);
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            return jsonObject.get("error").getAsString();
        } catch (Exception e) {
            e.printStackTrace();
//            Tracker t = ((CityfloCustomerApplication)
//                    ((Activity)context).getApplication()).getDefaultTracker();
//            t.send(new HitBuilders.ExceptionBuilder()
//                    .setDescription(new StandardExceptionParser(context, null)
//                            .getDescription(Thread.currentThread().getName(), e))
//                    .setFatal(false)
//                    .build());
            return getErrorByHttpStatusCode(error.getResponse().getStatus());
        }
    }
}
