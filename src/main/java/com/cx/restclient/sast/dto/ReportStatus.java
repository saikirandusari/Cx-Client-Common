package com.cx.restclient.sast.dto;

import com.cx.restclient.common.BaseStatus;

/**
 * Created by Galn on 07/03/2018.
 */
public class ReportStatus extends BaseStatus {
    private Link link;
    private  String contentType;
    private CxValueObj status;

    public Link getLink() {
        return link;
    }

    public void setLink(Link link) {
        this.link = link;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public CxValueObj getStatus() {
        return status;
    }

    public void setStatus(CxValueObj status) {
        this.status = status;
    }
}
