package cx.restclient.sast.dto;

import cx.restclient.common.BaseStatus;

/**
 * Created by Galn on 05/02/2018.
 */
public class ResponseQueueScanStatus extends BaseStatus{

    private Stage stage;
    private String stageDetails;
    private String stepDetails;
    private Project project;
    private String engine;
    private String languages;
    private String teamId;
    private String dateCreated;
    private String queuedOn;
    private String engineStartedOn;
    private String completedOn;
    private int loc;
    private boolean isIncremental;
    private boolean isPublic;
    private String origin;
    private int queuePosition;
    private int totalPercent;
    private int stagePercent;


 //   public Stage getStage() {
    //    return stage;
  //  }

  //  public void setStage(Stage stage) {
    //    this.stage = stage;
   // }

    public String getStageDetails() {
        return stageDetails;
    }

    public void setStageDetails(String stageDetails) {
        this.stageDetails = stageDetails;
    }

    public String getStepDetails() {
        return stepDetails;
    }

    public void setStepDetails(String stepDetails) {
        this.stepDetails = stepDetails;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getQueuedOn() {
        return queuedOn;
    }

    public void setQueuedOn(String queuedOn) {
        this.queuedOn = queuedOn;
    }

    public String getEngineStartedOn() {
        return engineStartedOn;
    }

    public void setEngineStartedOn(String engineStartedOn) {
        this.engineStartedOn = engineStartedOn;
    }

    public String getCompletedOn() {
        return completedOn;
    }

    public void setCompletedOn(String completedOn) {
        this.completedOn = completedOn;
    }

    public int getLoc() {
        return loc;
    }

    public void setLoc(int loc) {
        this.loc = loc;
    }

    public boolean isIncremental() {
        return isIncremental;
    }

    public void setIncremental(boolean incremental) {
        isIncremental = incremental;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public int getQueuePosition() {
        return queuePosition;
    }

    public void setQueuePosition(int queuePosition) {
        this.queuePosition = queuePosition;
    }

    public int getTotalPercent() {
        return totalPercent;
    }

    public void setTotalPercent(int totalPercent) {
        this.totalPercent = totalPercent;
    }

    public int getStagePercent() {
        return stagePercent;
    }

    public void setStagePercent(int stagePercent) {
        this.stagePercent = stagePercent;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
