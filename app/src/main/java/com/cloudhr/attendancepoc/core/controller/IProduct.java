package com.cloudhr.attendancepoc.core.controller;

import com.cloudhr.attendancepoc.core.IResponseSubcriber;

/**
 * Created by IN-RB on 10-12-2018.
 */

public interface IProduct {

    void getSwipe( int userid,String lat,String lon, String Type, IResponseSubcriber iResponseSubcriber);
}
