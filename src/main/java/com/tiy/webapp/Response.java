package com.tiy.webapp;

/**
 * Created by Paul Dennis on 2/6/2017.
 */
public class Response {

    private boolean success;
    private Integer id;

    public Response (boolean success) {
        this.success = success;
    }

    public Response(boolean success, Integer id) {
        this.success = success;
        this.id = id;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
