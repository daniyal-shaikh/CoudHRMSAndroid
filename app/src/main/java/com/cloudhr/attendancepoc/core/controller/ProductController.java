package com.cloudhr.attendancepoc.core.controller;

import android.content.Context;

import com.cloudhr.attendancepoc.core.IResponseSubcriber;
import com.cloudhr.attendancepoc.core.requestbuilder.ProductRequestBuilder;
import com.cloudhr.attendancepoc.core.response.SwipeDetailResponse;
import com.cloudhr.attendancepoc.dbControl.PrefManager;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by IN-RB on 10-12-2018.
 */

public class ProductController implements IProduct {

    ProductRequestBuilder.ProductNetworkService productNetworkService;
    Context mContext;
    PrefManager prefManager;

    public ProductController(Context context) {
        productNetworkService = new ProductRequestBuilder().getService();
        mContext = context;
        prefManager = new PrefManager(mContext);
    }


    @Override
    public void getSwipe(int userid, String lat, String lon, String Type, final IResponseSubcriber iResponseSubcriber) {

        HashMap<String, String> body = new HashMap<>();

        body.put("userid", String.valueOf(userid));
        body.put("lat", lat);
        body.put("long", lon);
        body.put("type", Type);

        productNetworkService.getSwipe(body).enqueue(new Callback<SwipeDetailResponse>() {
            @Override
            public void onResponse(Call<SwipeDetailResponse> call, Response<SwipeDetailResponse> response) {
                if (response.body() != null) {

                    if(response.body().getStatus_code() == 0) {
                        iResponseSubcriber.OnSuccess(response.body(), response.message());
                    } else {
                        //failure
                        iResponseSubcriber.OnFailure(new RuntimeException("Enable to reach server, Try again later"));
                    }

                } else {
                    //failure
                    iResponseSubcriber.OnFailure(new RuntimeException("Enable to reach server, Try again later"));
                }
            }

            @Override
            public void onFailure(Call<SwipeDetailResponse> call, Throwable t) {
                if (t instanceof ConnectException) {
                    iResponseSubcriber.OnFailure(t);
                } else if (t instanceof SocketTimeoutException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Check your internet connection"));
                } else if (t instanceof UnknownHostException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Check your internet connection"));
                } else if (t instanceof NumberFormatException) {
                    iResponseSubcriber.OnFailure(new RuntimeException("Unexpected server response"));
                } else {
                    iResponseSubcriber.OnFailure(new RuntimeException(t.getMessage()));
                }
            }
        });

    }
}
