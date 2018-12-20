package com.cloudhr.attendancepoc.core.controller;

import android.content.Context;

import com.cloudhr.attendancepoc.core.IResponseSubcriber;
import com.cloudhr.attendancepoc.core.requestbuilder.LoginRequestBuilder;
import com.cloudhr.attendancepoc.core.response.LoginResponse;
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
 * Created by daniyalshaikh on 17/12/18.
 */

public class LoginController implements ILogin {

    LoginRequestBuilder.LoginNetworkService loginNetworkService;
    Context mContext;
    PrefManager prefManager;

    public LoginController(Context context) {
        loginNetworkService = new LoginRequestBuilder().getService();
        mContext = context;
        prefManager = new PrefManager(mContext);
    }


    @Override
    public void login(String phoneno,final IResponseSubcriber iResponseSubcriber) {
        HashMap<String, String> body = new HashMap<>();
        body.put("phoneno", phoneno);
        loginNetworkService.login(body).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.body() != null) {

                    if(response.body().getStatus_code() == 0) {
                        iResponseSubcriber.OnSuccess(response.body(), response.message());
                    } else if(response.body().getStatus_code() == 1) {
                        //failure
                        iResponseSubcriber.OnFailure(new RuntimeException(response.body().getMessage()));
                    }
                    else{
                        iResponseSubcriber.OnFailure(new RuntimeException("Enable to reach server, Try again later"));
                    }

                } else {
                    //failure
                    iResponseSubcriber.OnFailure(new RuntimeException("Enable to reach server, Try again later"));
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
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
