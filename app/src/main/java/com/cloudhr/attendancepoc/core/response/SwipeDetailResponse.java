package com.cloudhr.attendancepoc.core.response;

import com.cloudhr.attendancepoc.core.APIResponse;
import com.cloudhr.attendancepoc.core.model.SwipeDetailEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by IN-RB on 10-12-2018.
 */

public class SwipeDetailResponse extends APIResponse {


    private List<SwipeDetailEntity> Data;

    public List<SwipeDetailEntity> getData() {
        return Data;
    }

    public void setData(List<SwipeDetailEntity> Data) {
        this.Data = Data;
    }


}
