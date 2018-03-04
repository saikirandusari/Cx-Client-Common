package cx.restclient.sast.dto;

/**
 * Created by Galn on 14/02/2018.
 */
public class Comment {
    private String content;

    public Comment(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
