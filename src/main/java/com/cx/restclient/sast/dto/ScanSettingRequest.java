package com.cx.restclient.sast.dto;

/**
 * Created by Galn on 05/02/2018.
 */
public class ScanSettingRequest {
    private int projectId;//TODO int??
    private int presetId;//TODO int??
    private int engineConfigurationId;//TODO int??
    private Integer postScanActionId;
    private EmailNotifications emailNotifications;

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getPresetId() {
        return presetId;
    }

    public void setPresetId(int presetId) {
        this.presetId = presetId;
    }

    public int getEngineConfigurationId() {
        return engineConfigurationId;
    }

    public void setEngineConfigurationId(int engineConfigurationId) {
        this.engineConfigurationId = engineConfigurationId;
    }

    public Integer getPostScanActionId() {
        return postScanActionId;
    }

    public void setPostScanActionId(Integer postScanActionId) {
        this.postScanActionId = postScanActionId;
    }

    public EmailNotifications getEmailNotifications() {
        return emailNotifications;
    }

    public void setEmailNotifications(EmailNotifications emailNotifications) {
        this.emailNotifications = emailNotifications;
    }
}
