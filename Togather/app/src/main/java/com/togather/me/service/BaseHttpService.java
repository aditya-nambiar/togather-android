package com.togather.me.service;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;

import com.togather.me.api.RequestResolver;
import com.togather.me.api.QueryResolver;
import com.togather.me.util.ListUtils;
import com.togather.me.util.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.togather.me.util.LogUtils.LOGD;



public class BaseHttpService extends Service {
    private static final String TAG = LogUtils.makeLogTag(BaseHttpService.class);
    private Context mContext;

    /**
     * the default value if Request is not in queue
     */
    private static final int NO_SUCH_REQUEST = -1;
    /**
     * if set as the Request status, it implies the Request is already in queue.
     */
    public static final int REQUEST_STATUS_DEFAULT = 0;
    private static final int REQUEST_STATUS_QUEUED = 1;
    private static final int REQUEST_STATUS_QUERYING = 2;

    /**
     * specifies the type of request. ASYNC_REQUEST executes the task
     * on a new thread. Sync requests queues the task in existing thread
     * the default is SYNC_REQUEST unless specified otherwise
     */
    public static final int SYNC_REQUEST = 0;
    public static final int ASYNC_REQUEST = 1;
    public static final int DEFAULT_REQUEST_TYPE = SYNC_REQUEST;

    /**
     * the list of requests to be processed + the one being processed
     */
    private static List<CustomRequest> mRequestQueue;

    /**
     * List of async task requests being processed.
     * An activity/fragment can demand for it's task
     * to be executed on a separate thread of it's own.
     */
    private static List<CustomRequest> mAsyncRequests;

    /**
     * Hash map of request and callbacks.
     */
    private static HashMap<CustomRequest, com.togather.me.service.RequestCallback> mCallbacksMap;

    /**
     * The keys which are to be used for providing the intent.
     * All the following keys are required fields.
     */
    public static final String REQUEST_ID = "request_id";
    public static final String URL = "url";
    public static final String REQUEST_TYPE = "request_type";
    public static final String PARAMS = "params";
    public static final String JSON_BODY = "json_body";
    public static final String REQUEST_PARCELABLE = "request_parcelable";

    /**
     * Flag to retain the state of activity. Activities set
     * this to true in onResume and false in onPause
     */
    private static boolean isListenerActive = false;

    private boolean clearRequests = false;

    private static com.togather.me.service.HttpResultListener httpResultListener;

    private final IBinder mBinder = new HttpGetBinder();

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class HttpGetBinder extends Binder {
        public BaseHttpService getService() {
            // Return this instance of BaseHttpGetService so clients can call public methods
            return BaseHttpService.this;
        }
    }

    public BaseHttpService() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAsyncRequests = new ArrayList<>();
        mRequestQueue = new ArrayList<>();
        mCallbacksMap = new HashMap<>();
        mContext = this;
        LOGD(TAG, "onCreate called");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        CustomRequest request = null;
        if (intent != null) {
            if (intent.hasExtra(REQUEST_ID)) {
                // retrieving the intent data
                int requestId = intent.getIntExtra(REQUEST_ID, 0);
                String url = intent.getStringExtra(URL);
                int requestType = intent.getIntExtra(REQUEST_TYPE, DEFAULT_REQUEST_TYPE);
                String[] params = intent.getStringArrayExtra(PARAMS);

                // check to see if data is valid and proceed
                if (requestId != 0) {
                    request = new CustomRequest(requestId, url, params, requestType);
                    newRequest(request);
                } else {
                    LogUtils.LOGE(TAG, "The intent to BaseHttpService is malformed");
                    LogUtils.LOGE(TAG, "Received: requestId = " + requestId);
                    LogUtils.LOGE(TAG, "Received: url = " + url);
                    LogUtils.LOGE(TAG, "Received: requestType = " + requestType);
                }
            } else if (intent.hasExtra(REQUEST_PARCELABLE)) {
                request = intent.getParcelableExtra(REQUEST_PARCELABLE);
                newRequest(request);
            }
        }

        LOGD(TAG, "onStartCommand called with request: " + request);
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        LOGD(TAG, "on Bind Called");
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LOGD(TAG, "onDestroy called");
    }

    /**
     * sets the status of given intent
     * Call this at the end of each phase:
     * - starting network service
     * - parsing
     * - saving to database
     *
     * @param request the request whose status is to be set
     * @param status    the status of the intent's task
     */
    private void setRequestStatus(CustomRequest request, int status) {
        request.setStatus(status);
    }

    /**
     * tells the status of the given intentId
     * The status is one of the constants defined in BaseHttpGetService
     *
     * @param request the request whose status is required
     * @return the value of the status as int
     */
    private int getRequestStatus(CustomRequest request) {
        return request.getStatus();
    }

    /**
     * register the start of another async request.
     *
     * @param customRequest the request to be added to async requests
     */
    private void addAsyncRequest(CustomRequest customRequest) {
        mAsyncRequests.add(customRequest);
    }

    /**
     * whenever an asynchronous request that wasn't running
     * on the queue has finished call this.
     * Note: this does not stop the request. This simply
     * registers the end of the request
     *
     * @param customRequest the async request to be removed
     */
    private void removeAsyncRequest(CustomRequest customRequest) {
        int index = ListUtils.findIndex(mAsyncRequests, customRequest);
        if (index > -1) {
            mAsyncRequests.remove(index);
        }
        setRequestStatus(customRequest, NO_SUCH_REQUEST);
    }

    /**
     * register the addition of another sync request.
     *
     * @param customRequest the request to be added to sync requests
     */
    private void addSyncRequest(CustomRequest customRequest) {
        mRequestQueue.add(customRequest);
    }

    /**
     * whenever a synchronous request that was running
     * on the queue has finished, call this.
     * Note: this does not stop the request. This simply
     * registers the end of the request
     *
     * @param customRequest the sync request to be removed
     */
    private void removeSyncRequest(CustomRequest customRequest) {
        int index = ListUtils.findIndex(mRequestQueue, customRequest);
        if (index > -1) {
            mRequestQueue.remove(index);
        }
        setRequestStatus(customRequest, NO_SUCH_REQUEST);
    }

    /**
     * removes the request from queue or async task list
     *
     * @param customRequest the request to be removed
     */
    private void removeRequest(CustomRequest customRequest) {
        if (customRequest.getRequestType() == SYNC_REQUEST) {
            removeSyncRequest(customRequest);
        } else {
            removeAsyncRequest(customRequest);
        }
    }

    /**
     * tells whether the given request is already in queue for execution
     *
     * @param customRequest request to be found in queue
     * @return returns true if request is a sync request
     */
    private boolean syncRequestExists(CustomRequest customRequest) {
        for (CustomRequest request : mRequestQueue) {
            if (request.equals(customRequest)) {
                return true;
            }
        }
        return false;
    }

    /**
     * tells whether the given request is already being executed async
     *
     * @param customRequest id of the request to be found in queue
     * @return returns true if request is async request
     */
    private boolean asyncRequestExists(CustomRequest customRequest) {
        for (CustomRequest request : mAsyncRequests) {
            if (request.equals(customRequest)) {
                return true;
            }
        }
        return false;
    }

    /**
     * tells whether the request exists in queue or as async request
     *
     * @param request the request to be found in queue
     * @return true if request exists
     */
    private boolean requestExists(CustomRequest request) {
        return syncRequestExists(request) || asyncRequestExists(request);
    }

    /**
     * Specifies whether a request is being processed
     *
     * @param request the request
     * @return true if status is higher than being queued
     */
    private boolean isRequestBeingProcessed(CustomRequest request) {
        return requestExists(request) && (getRequestStatus(request) > REQUEST_STATUS_QUEUED);
    }

    /**
     * Called by activities when they become active.
     * onResume possibly
     */
    public static void registerHttpResultListener(com.togather.me.service.HttpResultListener listener) {
        isListenerActive = true;
        httpResultListener = listener;
    }

    /**
     * Called by activities when they go to background.
     * onPause possibly
     */
    public static void unregisterHttpResultListener() {
        isListenerActive = false;
    }

    /**
     * checks the conditions for termination and stops itself if met.
     * the conditions for termination are:
     * No async tasks pending
     * No requests pending in queue
     * No activities active
     */
    public void checkForTermination() {
        boolean shouldTerminate = (mAsyncRequests.isEmpty()) && (mRequestQueue.isEmpty()) && !isListenerActive;
        if (shouldTerminate) {
            stopSelf();
        } else {
            LOGD(TAG, "AsyncRequest " + mAsyncRequests.isEmpty());
            LOGD(TAG, "SyncRequest " + mRequestQueue.isEmpty());
            LOGD(TAG, "ActivityActive " + isListenerActive);
        }
    }

    private void stopSync() {
        if (mAsyncRequests.isEmpty() && mRequestQueue.isEmpty()) {
            // TODO intimate stop of sync
        }
    }

    /**
     * adds a new async request if it isn't already in queue
     *
     * @param request the request to be added in queue
     */
    public void newSyncRequest(CustomRequest request) {
        if (requestExists(request)) {
            LogUtils.LOGW(TAG, "Request: " + request.getRequestId() + "already exists. Skipping adding syncRequest.");
            LogUtils.LOGW(TAG, "Request: {{ " + request + " }}");
        } else {
            request.setRequestType(SYNC_REQUEST);
            mRequestQueue.add(request);
            setRequestStatus(request, REQUEST_STATUS_QUEUED);
            startNextSyncRequest();
        }
    }

    /**
     * adds a new async request if it isn't already being processed
     *
     * @param request id of the request to be added
     */
    public void newAsyncRequest(CustomRequest request) {
        if (asyncRequestExists(request)) {
            LogUtils.LOGW(TAG, "Request: " + request.getRequestId() + " already is an async request. Skipping adding.");
            LogUtils.LOGW(TAG, "Request: {{ " + request + " }}");
        } else if (isRequestBeingProcessed(request)) {
            LogUtils.LOGW(TAG, "Request: " + request.getRequestId() + " is currently being processed. Skipping adding.");
            LogUtils.LOGW(TAG, "Request: {{ " + request + " }}");
        } else {
            request.setRequestType(ASYNC_REQUEST);
            mAsyncRequests.add(request);
            setRequestStatus(request, REQUEST_STATUS_QUERYING);
            startAsyncRequest(request);
        }
    }

    /**
     * adds a new request either to queue (as sync request)
     * or executes as async request depending on requestType
     *
     * @param request the request to be added
     */
    public void newRequest(CustomRequest request) {
        if (request.getRequestType() == SYNC_REQUEST) {
            newSyncRequest(request);
        } else {
            newAsyncRequest(request);
        }
    }

    /**
     * adds a new request and adds the callback to the hashmap
     * @param request the request to be added
     * @param callback the callback to be called after execution of request
     */
    public void newRequest(CustomRequest request, com.togather.me.service.RequestCallback callback) {
        mCallbacksMap.put(request, callback);
        newRequest(request);
    }

    /**
     * checks if a sync request is running
     * if no request is running starts a new one from queue
     */
    private void startNextSyncRequest() {
        LOGD(TAG, "Next sync request start called");
        if(clearRequests){
            mRequestQueue.clear();
        }
        for (CustomRequest customRequest : mRequestQueue) {
            if (getRequestStatus(customRequest) > REQUEST_STATUS_QUEUED) {
                return;
            }
        }
        if (!mRequestQueue.isEmpty()) {
            LOGD(TAG, "starting sync request : " + mRequestQueue.get(0));
            setRequestStatus(mRequestQueue.get(0), REQUEST_STATUS_QUERYING);
            new HttpRequestTask().execute(mRequestQueue.get(0));
        }
    }


    /**
     * starts an async task to process the given getRequest
     *
     * @param customRequest the async request to be processed
     */
    private void startAsyncRequest(CustomRequest customRequest) {
        new HttpRequestTask().execute(customRequest);
    }

    /**
     * calls the callback if it's defined and removes it from the hashmap
     * @param request request which is completed
     */
    private void startCallback(CustomRequest request) {
        if(mCallbacksMap.containsKey(request)) {
            if(mCallbacksMap.get(request) != null) {
                com.togather.me.service.RequestCallback callback = mCallbacksMap.get(request);
                if(request.isSuccess()) {
                    callback.onSuccess(request);
                } else {
                    callback.onFail(request);
                }
                mCallbacksMap.remove(request);
            } else {
                LOGD(TAG, "Callback object null");
            }
        } else {
            LOGD(TAG, "No callback.");
        }
    }

    /**
     * Sets the clearRequests boolean so that when starting
     * the next request the queue is cleared. We should not
     * access the queue here because it might be accessed in
     * other thread simultaneously
     */
    public void clearRequests() {
        clearRequests = true;
        /*Do not iterate over mRequestQueue or mAsyncRequests here
        * They might be changing in other thread.*/
    }

    /**
     * AsyncTask that handles the requests
     */
    private class HttpRequestTask extends AsyncTask<CustomRequest, CustomRequest, CustomRequest> {
        private com.togather.me.service.HttpResult httpResult;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // TODO intimate sync start
        }

        @Override
        protected CustomRequest doInBackground(CustomRequest... params) {
            CustomRequest customRequest = params[0];
            QueryResolver.preExecuteQuery(mContext, customRequest);
            publishProgress(customRequest);
            httpResult = RequestResolver.getByRequestId(customRequest, mContext);
            customRequest.setSuccess(!httpResult.isError);
            QueryResolver.postExecuteQuery(mContext, customRequest, !httpResult.isError);
            return customRequest;
        }

        protected void onProgressUpdate(CustomRequest... progress) {
            setRequestStatus(progress[0], REQUEST_STATUS_QUERYING);
        }

        protected void onPostExecute(CustomRequest request) {
            if (request != null)
                removeRequest(request);
            startNextSyncRequest();
            if (request != null) {
                startCallback(request);
                removeRequest(request);
            }
            if(httpResultListener != null) {
                httpResultListener.onHttpResult(httpResult);
            }else{
                isListenerActive = false;
            }
            checkForTermination();
            stopSync();
        }
    }

}
