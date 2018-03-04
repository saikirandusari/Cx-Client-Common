package cx.restclient.sast.dto;

/**
 * Created by Galn on 05/02/2018.
 */
public class CreateScanResponse extends Object {
    private long id;//TODO String??
    private String link;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
