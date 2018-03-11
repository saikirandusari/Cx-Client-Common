package com.cx.restclient.sast.dto;

/**
 * Created by Galn on 12/02/2018.
 */
public class CreateReportResponse {
    private int reportId;
    private Links links;

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }
}
