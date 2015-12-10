package com.togather.me.util;

import com.togather.me.service.CustomRequest;

import java.util.List;

public class ListUtils {

    public static int findIndex(List<CustomRequest> requestList, CustomRequest request) {
        if(requestList != null && request != null) {
            for (int i = 0; i < requestList.size(); i++) {
                if(requestList.get(i).equals(request)) {
                    return i;
                }
            }
            return -1;
        } else {
            return -1;
        }
    }
}
