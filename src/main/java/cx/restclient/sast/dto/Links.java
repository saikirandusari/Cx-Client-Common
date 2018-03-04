package cx.restclient.sast.dto;

/**
 * Created by Galn on 12/02/2018.
 */
public class Links {
    private CxLink report;
    private CxLink status;

    public CxLink getReport() {
        return report;
    }

    public void setReport(CxLink report) {
        this.report = report;
    }

    public CxLink getStatus() {
        return status;
    }

    public void setStatus(CxLink status) {
        this.status = status;
    }
}
