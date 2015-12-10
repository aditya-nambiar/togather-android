package com.togather.me.service;

import retrofit.RetrofitError;

/**
 * Created by manidesto on 24/03/15.
 */
public class HttpResult {
    public boolean isError = false;

    public RetrofitError retrofitError;

    public Object successResponse;

    public String successMsg = "";

    public boolean intimateOnSuccess = false;

    public CustomRequest request;
}
