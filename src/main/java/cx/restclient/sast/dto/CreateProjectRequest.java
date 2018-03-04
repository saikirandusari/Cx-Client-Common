package cx.restclient.sast.dto;

/**
 * Created by Galn on 13/02/2018.
 */
public class CreateProjectRequest {
    private String name;
    private String owningTeam;
    private boolean isPublic;

    public CreateProjectRequest(String name, String owningTeam, boolean isPublic) {
        this.name = name;
        this.owningTeam = owningTeam;
        this.isPublic = isPublic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwningTeam() {
        return owningTeam;
    }

    public void setOwningTeam(String owningTeam) {
        this.owningTeam = owningTeam;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }
}
