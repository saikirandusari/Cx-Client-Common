package com.cx.restclient.sast.dto;

/**
 * Created by Galn on 01/04/2018.
 */
public class Status {
    private int id;
    private String name;
    private Details details;

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

    public Details getDetails() {
        return details;
    }

    public void setDetails(Details details) {
        this.details = details;
    }
}
