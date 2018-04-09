package com.cx.restclient.sast.dto;

/**
 * Created by Galn on 18/03/2018.
 */
public class UpdateScanStatusRequest {
    private String status;

    public UpdateScanStatusRequest(CurrentStatus status) {
        this.status = status.value();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(CurrentStatus status) {
        this.status = status.value();
    }
}
