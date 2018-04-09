package com.cx.restclient.sast.dto;

/**
 * Created by Galn on 12/02/2018.
 */
public class CxNameLinkObj {
    private int id;
    private String name;
    private Link link;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Link getLink() {
        return link;
    }

    public void setLink(Link link) {
        this.link = link;
    }
}

