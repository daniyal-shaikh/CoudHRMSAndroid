package com.cloudhr.attendancepoc.core.requestbuilder;

import com.cloudhr.attendancepoc.core.RetroRequestBuilder;
import com.cloudhr.attendancepoc.core.response.LoginResponse;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by daniyalshaikh on 17/12/18.
 */

public class LoginRequestBuilder extends RetroRequestBuilder {

    public LoginRequestBuilder.LoginNetworkService getService() {

        return super.build().create(LoginRequestBuilder.LoginNetworkService.class);
    }

    public interface LoginNetworkService {

        @POST("/api/hrms-login")
        Call<LoginResponse> login(@Body HashMap<String, String> body);
    }
}
