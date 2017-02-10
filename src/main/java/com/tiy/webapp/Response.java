package com.tiy.webapp;

/**
 * Created by Paul Dennis on 2/6/2017.
 */
public class Response {

    private boolean success;

    public Response (boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
