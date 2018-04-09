package com.cx.restclient.sast.dto;

/**
 * Created by Galn on 29/03/2018.
 */
public class SASTResultsResponse {
    private long id;
    private CxNameLinkObj project;
    private Status status;
    private CxValueObj scanType;
    private String comment;
    private CxScanDate dateAndTime;
    private String resultsStatistics;
    private CxScanState scanState;
    private String owner;
    private String origin;
    private String initiatorName;
    private String owningTeamId;
    private boolean isPublic;
    private boolean isLocked;
    private boolean isIncremental;
    private int scanRisk;
    private int scanRiskSeverity;
    private CxNameLinkObj engineServer;
    private CxValueObj finishedScanStatus;
    private String partialScanReasons;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public CxNameLinkObj getProject() {
        return project;
    }

    public void setProject(CxNameLinkObj project) {
        this.project = project;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public CxValueObj getScanType() {
        return scanType;
    }

    public void setScanType(CxValueObj scanType) {
        this.scanType = scanType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public CxScanDate getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(CxScanDate dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public String getResultsStatistics() {
        return resultsStatistics;
    }

    public void setResultsStatistics(String resultsStatistics) {
        this.resultsStatistics = resultsStatistics;
    }

    public CxScanState getScanState() {
        return scanState;
    }

    public void setScanState(CxScanState scanState) {
        this.scanState = scanState;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getInitiatorName() {
        return initiatorName;
    }

    public void setInitiatorName(String initiatorName) {
        this.initiatorName = initiatorName;
    }

    public String getOwningTeamId() {
        return owningTeamId;
    }

    public void setOwningTeamId(String owningTeamId) {
        this.owningTeamId = owningTeamId;
    }

    public boolean isIsPublic() {
        return isPublic;
    }

    public void setIsPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public boolean isIsLocked() {
        return isLocked;
    }

    public void setIsLocked(boolean locked) {
        isLocked = locked;
    }

    public boolean isIsIncremental() {
        return isIncremental;
    }

    public void setIsIncremental(boolean incremental) {
        isIncremental = incremental;
    }

    public int getScanRisk() {
        return scanRisk;
    }

    public void setScanRisk(int scanRisk) {
        this.scanRisk = scanRisk;
    }

    public int getScanRiskSeverity() {
        return scanRiskSeverity;
    }

    public void setScanRiskSeverity(int scanRiskSeverity) {
        this.scanRiskSeverity = scanRiskSeverity;
    }

    public CxNameLinkObj getEngineServer() {
        return engineServer;
    }

    public void setEngineServer(CxNameLinkObj engineServer) {
        this.engineServer = engineServer;
    }

    public CxValueObj getFinishedScanStatus() {
        return finishedScanStatus;
    }

    public void setFinishedScanStatus(CxValueObj finishedScanStatus) {
        this.finishedScanStatus = finishedScanStatus;
    }

    public String getPartialScanReasons() {
        return partialScanReasons;
    }

    public void setPartialScanReasons(String partialScanReasons) {
        this.partialScanReasons = partialScanReasons;
    }
}
