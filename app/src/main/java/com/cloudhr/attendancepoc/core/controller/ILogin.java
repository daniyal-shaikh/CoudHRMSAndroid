package com.cloudhr.attendancepoc.core.controller;

import com.cloudhr.attendancepoc.core.IResponseSubcriber;

/**
 * Created by daniyalshaikh on 17/12/18.
 */

public interface ILogin {

    void login(String phoneno, IResponseSubcriber iResponseSubcriber);
}
