package com.cx.restclient.sast.dto;

/**
 * Created by Galn on 04/03/2018.
 */
public class SourceSettingsLink {
    String type;
    String rel;
    String uri;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getRel() {

        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
