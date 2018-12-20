package com.cloudhr.attendancepoc.core;

/**
 * Created by Rajeev Ranjan on 22/01/2018.
 */

public class APIResponse {

    /**
     * message : Record Saved Successfully
     * status : Success
     * status_code : 1
     */

    private String message;
    private String status;
    private int status_code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }
}
