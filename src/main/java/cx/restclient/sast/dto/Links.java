package cx.restclient.sast.dto;

/**
 * Created by Galn on 12/02/2018.
 */
public class Links {
    private Link report;
    private Link status;

    public Link getReport() {
        return report;
    }

    public void setReport(Link report) {
        this.report = report;
    }

    public Link getStatus() {
        return status;
    }

    public void setStatus(Link status) {
        this.status = status;
    }
}
