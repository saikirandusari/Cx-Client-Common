package com.cx.restclient.osa.dto;

/**
 * Created by Galn on 7/8/2018.
 */
public class OsaViolation {
    private String libraryName;
    private String policyName;
    private String ruleName;
    private String detectionDate;

    public OsaViolation() {
    }

    public OsaViolation(String libraryName, String policyName, String ruleName, String detectionDate) {
        this.libraryName = libraryName;
        this.policyName = policyName;
        this.ruleName = ruleName;
        this.detectionDate = detectionDate;
    }

    public String getLibraryName() {
        return libraryName;
    }

    public void setLibraryName(String libraryName) {
        this.libraryName = libraryName;
    }

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getDetectionDate() {
        return detectionDate;
    }

    public void setDetectionDate(String detectionDate) {
        this.detectionDate = detectionDate;
    }
}
