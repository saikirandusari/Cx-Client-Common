package cx.restclient.sast.dto;

/**
 * Created by Galn on 05/02/2018.
 */
public class CreateScanRequest {
    private long projectId;//TODO int??
    private boolean isIncremental = false;//TODO default param?
    private boolean isPublic = true;
    private boolean forceScan = true;

    public CreateScanRequest(long projectId, boolean isIncremental, boolean isPublic, boolean forceScan) {
        this.projectId = projectId;
        this.isIncremental = isIncremental;
        this.isPublic = isPublic;
        this.forceScan = forceScan;
    }

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public boolean isIncremental() {
        return isIncremental;
    }

    public void setIncremental(boolean incremental) {
        isIncremental = incremental;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public boolean isForceScan() {
        return forceScan;
    }

    public void setForceScan(boolean forceScan) {
        this.forceScan = forceScan;
    }
}
