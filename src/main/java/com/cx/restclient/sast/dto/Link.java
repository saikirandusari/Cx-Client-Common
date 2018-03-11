package com.cx.restclient.sast.dto;

/**
 * Created by Galn on 06/02/2018.
 */
public class Link {
    private String rel;
    private String uri;

    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
