package cx.restclient.sast.dto;

/**
 * Created by: dorg.
 * Date: 05/03/2018.
 */
public class ScanSettingResponse {


    public CxLinkObj project;
    public CxLinkObj preset;
    public CxLinkObj engineConfiguration;
    public CxLinkObj postScanAction;
    public EmailNotifications emailNotifications;

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
