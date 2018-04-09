package com.cx.restclient.sast.dto;

/**
 * Created by: dorg.
 * Date: 05/03/2018.
 */
public class ScanSettingResponse {

    private CxLinkObj project;
    private CxLinkObj preset;
    private CxLinkObj engineConfiguration;
    private CxLinkObj postScanAction;
    private EmailNotifications emailNotifications;

    public EmailNotifications getEmailNotifications() {
        return emailNotifications;
    }

    public void setEmailNotifications(EmailNotifications emailNotifications) {
        this.emailNotifications = emailNotifications;
    }

    public CxLinkObj getPostScanAction() {
        return postScanAction;
    }

    public void setPostScanAction(CxLinkObj postScanAction) {
        this.postScanAction = postScanAction;
    }

    public CxLinkObj getEngineConfiguration() {
        return engineConfiguration;
    }

    public void setEngineConfiguration(CxLinkObj engineConfiguration) {
        this.engineConfiguration = engineConfiguration;
    }

    public CxLinkObj getPreset() {
        return preset;
    }

    public void setPreset(CxLinkObj preset) {
        this.preset = preset;
    }

    public CxLinkObj getProject() {
        return project;
    }

    public void setProject(CxLinkObj project) {
        this.project = project;
    }
}
