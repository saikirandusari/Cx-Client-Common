package cx.restclient.sast.dto;

/**
 * Created by Galn on 12/02/2018.
 */
public class ProjectScannedData {

    private long projectID;
    private String projectName;
    private long lastScanID;
    private long lastScanDate;
    private String teamName;
    private int riskLevelScore;
    private long loc;
    private int highVulnerabilities;
    private int mediumVulnerabilities;
    private int lowVulnerabilities;
    private int infoVulnerabilities;
    private int totalVulnerabilities;
    private long queueTime;
    private String failedLOC;
    private long statisticsCalculationDate;


    //Finishedtime
    //Initiator name
    //isIncremental
    //CxVersion
    //Comments
    //serverName
    //Origin
    //Partial scan indication
    //is locked
    //is allow to delete
    //scan display type
    //Files count (GetScansSummary)
    //Scan Type (GetScansSummary)
    //Scan language state collection (GetScansSummary)
    //Engine start (GetScansSummary)
    //Engine End (GetScansSummary)

     public long getProjectID() {
        return projectID;
    }

    public void setProjectID(long projectID) {
        this.projectID = projectID;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public long getLastScanID() {
        return lastScanID;
    }

    public void setLastScanID(long lastScanID) {
        this.lastScanID = lastScanID;
    }

    public long getLastScanDate() {
        return lastScanDate;
    }

    public void setLastScanDate(long lastScanDate) {
        this.lastScanDate = lastScanDate;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int getRiskLevelScore() {
        return riskLevelScore;
    }

    public void setRiskLevelScore(int riskLevelScore) {
        this.riskLevelScore = riskLevelScore;
    }

    public long getLoc() {
        return loc;
    }

    public void setLoc(long loc) {
        this.loc = loc;
    }

    public int getHighVulnerabilities() {
        return highVulnerabilities;
    }

    public void setHighVulnerabilities(int highVulnerabilities) {
        this.highVulnerabilities = highVulnerabilities;
    }

    public int getMediumVulnerabilities() {
        return mediumVulnerabilities;
    }

    public void setMediumVulnerabilities(int mediumVulnerabilities) {
        this.mediumVulnerabilities = mediumVulnerabilities;
    }

    public int getLowVulnerabilities() {
        return lowVulnerabilities;
    }

    public void setLowVulnerabilities(int lowVulnerabilities) {
        this.lowVulnerabilities = lowVulnerabilities;
    }

    public int getInfoVulnerabilities() {
        return infoVulnerabilities;
    }

    public void setInfoVulnerabilities(int infoVulnerabilities) {
        this.infoVulnerabilities = infoVulnerabilities;
    }

    public int getTotalVulnerabilities() {
        return totalVulnerabilities;
    }

    public void setTotalVulnerabilities(int totalVulnerabilities) {
        this.totalVulnerabilities = totalVulnerabilities;
    }

    public long getQueueTime() {
        return queueTime;
    }

    public void setQueueTime(long queueTime) {
        this.queueTime = queueTime;
    }


    public String getFailedLOC() {
        return failedLOC;
    }

    public void setFailedLOC(String failedLOC) {
        this.failedLOC = failedLOC;
    }

    public long getStatisticsCalculationDate() {
        return statisticsCalculationDate;
    }

    public void setStatisticsCalculationDate(long statisticsCalculationDate) {
        this.statisticsCalculationDate = statisticsCalculationDate;
    }
}

