package com.cx.restclient.dto;

/**
 * Created by Galn on 4/10/2018.
 */
public class ThresholdResult {
    private boolean isFail;
    private String failDescription;

    public ThresholdResult(boolean isFail, String failDescription) {
        this.isFail = isFail;
        this.failDescription = failDescription;
    }

    public boolean isFail() {
        return isFail;
    }

    public void setFail(boolean fail) {
        isFail = fail;
    }

    public String getFailDescription() {
        return failDescription;
    }

    public void setFailDescription(String failDescription) {
        this.failDescription = failDescription;
    }
}
