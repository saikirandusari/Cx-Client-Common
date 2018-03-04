package cx.restclient.sast.dto;

/**
 * Created by Galn on 12/02/2018.
 */
public class GetReportResponse {
    private byte[] scanResults;

    public byte[] getScanResults() {
        return scanResults;
    }

    public void setScanResults(byte[] scanResults) {
        this.scanResults = scanResults;
    }
}
