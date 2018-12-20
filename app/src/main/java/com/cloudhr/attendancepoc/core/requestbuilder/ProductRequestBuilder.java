package com.cloudhr.attendancepoc.core.requestbuilder;



import com.cloudhr.attendancepoc.core.RetroRequestBuilder;
import com.cloudhr.attendancepoc.core.response.SwipeDetailResponse;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;

import retrofit2.http.POST;

public class ProductRequestBuilder extends RetroRequestBuilder {

    public ProductRequestBuilder.ProductNetworkService getService() {

        return super.build().create(ProductRequestBuilder.ProductNetworkService.class);
    }

    public interface ProductNetworkService {



        @POST("/api/mark-attendance")
        Call<SwipeDetailResponse> getSwipe(@Body HashMap<String, String> body);






    }
}