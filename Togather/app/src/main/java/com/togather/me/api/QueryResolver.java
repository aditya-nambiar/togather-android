package com.togather.me.api;

import android.content.Context;
import android.support.v4.content.CursorLoader;

import com.togather.me.service.CustomRequest;
import com.togather.me.util.LogUtils;


public class QueryResolver {
    private static final String TAG = LogUtils.makeLogTag(QueryResolver.class);

    public static CursorLoader getLoaderFromRequest(Context context, CustomRequest request) {
        int requestId = request.getRequestId();

        switch (requestId) {

            default:
                return null;

        }
    }

    public static void preExecuteQuery(Context context, CustomRequest customRequest) {
        switch (customRequest.getRequestId()) {
            // TODO run appropriate queries. Close cursors
            default:
                break;
        }
    }

    public static void postExecuteQuery(Context context, CustomRequest customRequest, boolean success) {
        switch (customRequest.getRequestId()) {
            // TODO run appropriate queries. Close cursors
            default:
                break;
        }
    }

}
