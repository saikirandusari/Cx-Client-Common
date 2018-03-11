package com.cx.restclient.common;

/**
 * Created by Galn on 13/02/2018.
 */
public class BaseStatus {
    private String baseId;
    private Status baseStatus;

    public BaseStatus() {
    }

    public BaseStatus(String id) {
        this.baseId = id;
    }

    public Status getBaseStatus() {
        return baseStatus;
    }

    public void setBaseStatus(Status baseStatus) {
        this.baseStatus = baseStatus;
    }

    public String getBaseId() {
        return baseId;
    }

    public void setBaseId(String baseId) {
        this.baseId = baseId;
    }
}
