package com.cx.restclient.exception;

/**
 * Created by shaulv on 6/17/2018.
 */
public class CxCredentialsException extends  CxClientException{

    public CxCredentialsException() {
        super();
    }

    public CxCredentialsException(String message) {
        super(message);
    }

    public CxCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }

    public CxCredentialsException(Throwable cause) {
        super(cause);
    }
}
