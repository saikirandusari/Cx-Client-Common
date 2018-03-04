package cx.restclient.sast.dto;

/**
 * Created by Galn on 12/02/2018.
 */
public class ResponseProjectScannedData {

    private boolean isSuccessful;
    private String errorMessage;
    private ProjectScannedData projectScannedData;

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ProjectScannedData getProjectScannedData() {
        return projectScannedData;
    }

    public void setProjectScannedData(ProjectScannedData projectScannedData) {
        this.projectScannedData = projectScannedData;
    }
}
