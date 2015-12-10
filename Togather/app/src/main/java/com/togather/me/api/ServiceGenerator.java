package com.togather.me.api;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.togather.me.BuildConfig;
import com.togather.me.util.LogUtils;
import com.togather.me.util.PrefUtils;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

;

public class ServiceGenerator {
    private static final String TAG = LogUtils.makeLogTag(ServiceGenerator.class);

    // No need to instantiate this class.
    private ServiceGenerator() {
    }

    public static <S> S createService(Class<S> serviceClass, String baseUrl, Context context) {
        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(baseUrl)
                .setClient(new OkClient(Client.getClientInstance(context)));

        GsonBuilder gsonBuilder = new GsonBuilder();
        // TODO register deserializers if necessary
        // gsonBuilder.registerTypeAdapter(Model.class, new ModelDeserializer());
       // gsonBuilder.registerTypeAdapter(UpcomingVehicleRide.class, new com.cityflo.customer.model.Parser.UpcomingVehicleRideParser());
       // gsonBuilder.registerTypeAdapter(RunningRouteWithStop.class, new com.cityflo.customer.model.Parser.RunningRouteWithStopParser());

        Gson gson = gsonBuilder.create();
        builder.setConverter(new GsonConverter(gson));

        final String token = PrefUtils.getString(context, PrefUtils.PREF_KEY_TOKEN);

        if(token!=null && !"".equals(token) && PrefUtils.getBoolean(context, PrefUtils.PREF_KEY_IS_LOGGED_IN, false)) {
            builder.setRequestInterceptor(new RequestInterceptor() {
                @Override
                public void intercept(RequestFacade request) {
                    LogUtils.LOGD(TAG, "Intercepted");
                    request.addHeader("Accept", "application/json");
                    request.addHeader("Authorization", "Token " + token);
                }
            });
        }

        RestAdapter adapter = builder.build();
        adapter.setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE);

        return adapter.create(serviceClass);
    }
}
