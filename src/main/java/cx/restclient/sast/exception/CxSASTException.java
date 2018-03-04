package cx.restclient.sast.exception;

/**
 * Created by Galn on 05/02/2018.
 */
public class CxSASTException extends Exception {
    public CxSASTException() {
        super();
    }

    public CxSASTException(String message) {
        super(message);
    }

    public CxSASTException(String message, Throwable cause) {
        super(message, cause);
    }

    public CxSASTException(Throwable cause) {
        super(cause);
    }

}
