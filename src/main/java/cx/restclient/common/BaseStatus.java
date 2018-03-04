package cx.restclient.common;

/**
 * Created by Galn on 13/02/2018.
 */
public class BaseStatus {
    private long id;
    private Status status;

    public BaseStatus() {
    }

    public BaseStatus(long id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


}
