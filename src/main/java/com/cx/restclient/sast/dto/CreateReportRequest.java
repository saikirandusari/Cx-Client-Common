package com.cx.restclient.sast.dto;

/**
 * Created by Galn on 12/02/2018.
 */
public class CreateReportRequest {
    private long scanId;
    private String reportType;

    public CreateReportRequest(long scanId, String reportType) {
        this.scanId = scanId;
        this.reportType = reportType;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public long getScanId() {
        return scanId;
    }

    public void setScanId(long scanId) {
        this.scanId = scanId;
    }
}
