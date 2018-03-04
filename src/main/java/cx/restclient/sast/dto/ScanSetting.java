package cx.restclient.sast.dto;

/**
 * Created by Galn on 05/02/2018.
 */
public class ScanSetting {
    private long projectId;//TODO int??
    private int presetId;//TODO int??
    private int engineConfigurationId;
    private int postScanActionId;
    private EmailNotifications emailNotifications;

     public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
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

    public int getPostScanActionId() {
        return postScanActionId;
    }

    public void setPostScanActionId(int postScanActionId) {
        this.postScanActionId = postScanActionId;
    }

    public EmailNotifications getEmailNotifications() {
        return emailNotifications;
    }

    public void setEmailNotifications(EmailNotifications emailNotifications) {
        this.emailNotifications = emailNotifications;
    }
}
