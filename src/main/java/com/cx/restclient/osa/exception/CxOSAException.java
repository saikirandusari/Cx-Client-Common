package com.cx.restclient.osa.exception;

/**
 * Created by Galn on 05/02/2018.
 */
public class CxOSAException extends Exception {
    public CxOSAException() {
        super();
    }

    public CxOSAException(String message) {
        super(message);
    }

    public CxOSAException(String message, Throwable cause) {
        super(message, cause);
    }

    public CxOSAException(Throwable cause) {
        super(cause);
    }

}
