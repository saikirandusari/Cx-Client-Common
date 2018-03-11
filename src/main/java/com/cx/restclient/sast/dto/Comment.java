package com.cx.restclient.sast.dto;

/**
 * Created by Galn on 14/02/2018.
 */
public class Comment {
    private String comment;

    public Comment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

