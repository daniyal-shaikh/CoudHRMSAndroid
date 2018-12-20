package com.cloudhr.attendancepoc.core.response;

import com.cloudhr.attendancepoc.core.APIResponse;
import com.cloudhr.attendancepoc.core.model.LoginDetailEntity;

import java.util.List;

/**
 * Created by daniyalshaikh on 17/12/18.
 */

public class LoginResponse extends APIResponse {

    /**
     * Message : Login successfully
     * Status : Success
     * StatusCode : 0
     * Data : [{"employeeid":1,"employeename":"Daniyal","employeecode":"CHR1","phoneno1":"9702943935","phoneno2":"8355992808","emailid":"shaikhdani26@gmail.com","branchid":1,"branchname":"Clour HR - Mumbai","branchlocation":"MUmbai","companyid":1,"companyname":"Cloud HR Technology Pvt. Ltd","SaveStatus":"0"}]
     */


    private List<LoginDetailEntity> Data;

    public List<LoginDetailEntity> getData() {
        return Data;
    }

    public void setData(List<LoginDetailEntity> Data) {
        this.Data = Data;
    }


}
