package com.cx.restclient.sast.dto;

/**
 * Created by Galn on 13/02/2018.
 */
public class Project { //TODO
    private int id;
    private String name;
    private String teamId;
    private boolean isPublic;
    private Link link;
    private SourceSettingsLink sourceSettingsLink;


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

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public Link getLink() {
        return link;
    }

    public void setLink(Link link) {
        this.link = link;
    }

    public SourceSettingsLink getSourceSettingsLink() {
        return sourceSettingsLink;
    }

    public void setSourceSettingsLink(SourceSettingsLink sourceSettingsLink) {
        this.sourceSettingsLink = sourceSettingsLink;
    }
}
