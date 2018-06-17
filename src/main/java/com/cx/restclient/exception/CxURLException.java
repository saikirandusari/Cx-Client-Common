package com.cx.restclient.exception;

import java.net.MalformedURLException;

/**
 * Created by shaulv on 6/14/2018.
 */
public class CxURLException extends MalformedURLException {

    public CxURLException(String msg) {
        super(msg);
    }
}
