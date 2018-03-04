package cx.restclient.sast.dto;

/**
 * Created by Galn on 05/02/2018.
 */
public class EmailNotifications {
    private String failedScan;
    private String beforeScan;
    private String afterScan;

    public String getFailedScan() {
        return failedScan;
    }

    public void setFailedScan(String failedScan) {
        this.failedScan = failedScan;
    }

    public String getBeforeScan() {
        return beforeScan;
    }

    public void setBeforeScan(String beforeScan) {
        this.beforeScan = beforeScan;
    }

    public String getAfterScan() {
        return afterScan;
    }

    public void setAfterScan(String afterScan) {
        this.afterScan = afterScan;
    }
}
