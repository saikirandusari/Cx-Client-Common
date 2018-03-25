package com.cx.restclient.sast.dto;

/**
 * Created by Galn on 18/03/2018.
 */
public class CancelRequest {
    private String status;

    public CancelRequest(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
