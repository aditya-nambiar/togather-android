package com.togather.me.service;

public interface RequestCallback {

    void onSuccess(CustomRequest request);

    void onFail(CustomRequest request);

}
