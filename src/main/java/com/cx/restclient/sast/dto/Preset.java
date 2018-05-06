package com.cx.restclient.sast.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Galn on 14/02/2018.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Preset {
    private int id;
    private String name;

    public Preset() {
    }

    public Preset(int id, String name) {
        this.id = id;
        this.name = name;
    }

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
}
