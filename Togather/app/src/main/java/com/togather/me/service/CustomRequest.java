package com.togather.me.service;

import android.os.Parcel;
import android.os.Parcelable;

import com.togather.me.util.JsonUtils;
import com.togather.me.util.LogUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.Arrays;

public class CustomRequest implements Parcelable {
    private static final String TAG = LogUtils.makeLogTag(CustomRequest.class);
    int mRequestId;
    String mUrl;
    String[] mParams;
    int mRequestType;
    JsonElement mJsonBody;

    /**
     * result of the request. whether successful or an error occured
     */
    boolean mSuccess;

    /**
     * status of the request in process. Useful only for BaseHttpService
     * whether it's being processed or in queue
     */
    int mStatus;

    public CustomRequest(int requestId, String url, String[] params, int requestType, JsonElement jsonElement, boolean success) {
        this.mRequestId = requestId;
        this.mUrl = url;
        this.mParams = params;
        this.mRequestType = requestType;
        this.mJsonBody = jsonElement;
        this.mSuccess = success;
        this.mStatus = com.togather.me.service.BaseHttpService.REQUEST_STATUS_DEFAULT;
    }

    public CustomRequest(int requestId, String url, String[] params, int requestType, JsonElement jsonElement) {
        this(requestId, url, params, requestType, jsonElement, false);
    }

    public CustomRequest(int requestId, String url, String[] params, int requestType) {
        this(requestId, url, params, requestType, null);
    }

    public CustomRequest(int requestId, String url, String[] params) {
        this(requestId, url, params, com.togather.me.service.BaseHttpService.DEFAULT_REQUEST_TYPE);
    }

    public CustomRequest(Parcel parcel) {
        setRequestId(parcel.readInt());
        setUrl(parcel.readString());
        setParams(parcel.createStringArray());
        setRequestType(parcel.readInt());
        setJsonBody(parcel.readString());
        setSuccess(parcel.readInt() != 0);
    }

    public boolean equals(CustomRequest compareTo) {
        boolean isEqual = (mRequestId == compareTo.getRequestId());
        if (mUrl != null) {
            isEqual = isEqual && (mUrl.equals(compareTo.getUrl()));
        } else if (compareTo.getUrl() != null) {
            isEqual = false;
        }
        isEqual = isEqual && (Arrays.equals(mParams, compareTo.getParams()));
        isEqual = isEqual && (JsonUtils.areJsonEqual(this.getJsonBody(), compareTo.getJsonBody()));
        return isEqual;
    }

    public boolean isSyncRequest() {
        return mRequestType == com.togather.me.service.BaseHttpService.SYNC_REQUEST;
    }

    public boolean isAsyncRequest() {
        return mRequestType == com.togather.me.service.BaseHttpService.ASYNC_REQUEST;
    }

    public int getRequestId() {
        return mRequestId;
    }

    public void setRequestId(int mRequestId) {
        this.mRequestId = mRequestId;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String mGetUrl) {
        this.mUrl = mGetUrl;
    }

    public String[] getParams() {
        return mParams;
    }

    public void setParams(String[] mParams) {
        this.mParams = mParams;
    }

    public int getRequestType() {
        return mRequestType;
    }

    public void setRequestType(int mRequestType) {
        this.mRequestType = mRequestType;
    }

    public JsonElement getJsonBody() {
        return mJsonBody;
    }

    public void setJsonBody(JsonElement mJsonBody) {
        this.mJsonBody = mJsonBody;
    }

    public void setJsonBody(String json) {
        LogUtils.LOGD(TAG, "json string: " + json);
        Gson gson = new Gson();
        this.mJsonBody = gson.fromJson(json, JsonElement.class);
//        LogUtils.LOGD(TAG, "json constructed: " + mJsonBody.toString());
    }


    public boolean isSuccess() {
        return mSuccess;
    }

    public void setSuccess(boolean success) {
        this.mSuccess = success;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        this.mStatus = status;
    }

    public String toString() {
        return "id: " + Integer.toString(mRequestId) + " url: " + mUrl + " params: " + Arrays.toString(mParams) + " jsonBody: " + mJsonBody;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mRequestId);
        dest.writeString(mUrl);
        dest.writeStringArray(mParams);
        dest.writeInt(mRequestType);
        if (mJsonBody != null) {
            dest.writeString(mJsonBody.toString());
        }
        dest.writeInt(mSuccess ? 1 : 0);
    }

    public static final Creator<CustomRequest> CREATOR = new Creator<CustomRequest>() {
        @Override
        public CustomRequest createFromParcel(Parcel source) {
            return new CustomRequest(source);
        }

        @Override
        public CustomRequest[] newArray(int size) {
            return new CustomRequest[size];
        }
    };

}
