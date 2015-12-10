package com.togather.me.api;

import com.google.gson.JsonElement;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

public class RetrofitInterface {



    public interface RecentRide{
        @GET(ApiEndPoints.GET_RECENT_RIDE_URL)
        JsonElement getData(@Query("time_of_day") String time_of_day);
    }

    public interface VerbClient {
        @POST("/")
        JsonElement postData(@Body JsonElement jsonElement);

        @GET("/")
        JsonElement getData();
    }



    public interface LoginClient {
        @POST(ApiEndPoints.LOGIN_URL)
        JsonElement login(@Body JsonElement jsonElement);
    }

}
