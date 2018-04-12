package com.cx.restclient.sast.dto;

import java.util.List;

import static com.cx.restclient.sast.utils.SASTParam.PROJECT_LINK_FORMAT;
import static com.cx.restclient.sast.utils.SASTParam.SCAN_LINK_FORMAT;


/**
 * Created by Galn on 05/02/2018.
 */
public class SASTResults {

    private long scanId;

    private boolean sastResultsReady = false;
    private int highResults = 0;
    private int mediumResults = 0;
    private int lowResults = 0;
    private int infoResults = 0;

    private int newHighCount = 0;
    private int newMediumCount = 0;
    private int newLowCount = 0;
    private int newInfoCount = 0;

    private String sastScanLink;
    private String sastProjectLink;

    private String scanStart;
    private String scanTime;
    private String filesScanned;
    private String linesOfCodeScanned;
    private List<CxXMLResults.Query> queryList;

    private byte[] PDFReport;

    private Boolean isSynchronous;

    private enum Severity {
        High, Medium, Low, Info;
    }

    public void setScanDetailedReport(CxXMLResults reportObj) {
        this.scanStart = reportObj.getScanStart();
        this.scanTime = reportObj.getScanTime();
        this.linesOfCodeScanned = reportObj.getLinesOfCodeScanned();
        this.filesScanned = reportObj.getFilesScanned();

        for (CxXMLResults.Query q : reportObj.getQuery()) {
            List<CxXMLResults.Query.Result> qResult = q.getResult();
            for (int i = 0; i < qResult.size(); i++) {
                CxXMLResults.Query.Result result = qResult.get(i);
                if ("True".equals(result.getFalsePositive())) {
                    qResult.remove(i);
                } else if ("New".equals(result.getStatus())){
                    Severity sev = Severity.valueOf(result.getSeverity());
                    switch (sev) {
                        case High:
                            newHighCount++;
                            break;
                        case Medium:
                            newMediumCount++;
                            break;
                        case Low:
                            newLowCount++;
                            break;
                        case Info:
                            newInfoCount++;
                            break;
                    }
                }
            }
        }
        this.queryList = reportObj.getQuery();//todo qa
    }

    public void setResults(long scanId, SASTStatisticsResponse statisticsResults, String url, long projectId) {
        setScanId(scanId);
        setHighResults(statisticsResults.getHighSeverity());
        setMediumResults(statisticsResults.getMediumSeverity());
        setLowResults(statisticsResults.getLowSeverity());
        setInfoResults(statisticsResults.getInfoSeverity());
        setSastScanLink(url, scanId, projectId);
        setSastProjectLink(url, projectId);
        setSastResultsReady(true);
    }

    public long getScanId() {
        return scanId;
    }

    public void setScanId(long scanId) {
        this.scanId = scanId;
    }

    public int getHighResults() {
        return highResults;
    }

    public void setHighResults(int highResults) {
        this.highResults = highResults;
    }

    public int getMediumResults() {
        return mediumResults;
    }

    public void setMediumResults(int mediumResults) {
        this.mediumResults = mediumResults;
    }

    public int getLowResults() {
        return lowResults;
    }

    public void setLowResults(int lowResults) {
        this.lowResults = lowResults;
    }

    public int getInfoResults() {
        return infoResults;
    }

    public void setInfoResults(int infoResults) {
        this.infoResults = infoResults;
    }

    public int getNewHighCount() {
        return newHighCount;
    }

    public void setNewHighCount(int newHighCount) {
        this.newHighCount = newHighCount;
    }

    public int getNewMediumCount() {
        return newMediumCount;
    }

    public void setNewMediumCount(int newMediumCount) {
        this.newMediumCount = newMediumCount;
    }

    public int getNewLowCount() {
        return newLowCount;
    }

    public void setNewLowCount(int newLowCount) {
        this.newLowCount = newLowCount;
    }

    public int getNewInfoCount() {
        return newInfoCount;
    }

    public void setNewInfoCount(int newInfoCount) {
        this.newInfoCount = newInfoCount;
    }

    public String getSastScanLink() {
        return sastScanLink;
    }

    public void setSastScanLink(String sastScanLink) {
        this.sastScanLink = sastScanLink;
    }

    public void setSastScanLink(String url, long scanId, long projectId) {
        this.sastScanLink = String.format(url + SCAN_LINK_FORMAT, scanId, projectId);
    }

    public String getSastProjectLink() {
        return sastProjectLink;
    }

    public void setSastProjectLink(String sastProjectLink) {
        this.sastProjectLink = sastProjectLink;
    }

    public void setSastProjectLink(String url, long projectId) {
        this.sastProjectLink = String.format(url + PROJECT_LINK_FORMAT, projectId);
    }

    public String getScanStart() {
        return scanStart;
    }

    public void setScanStart(String scanStart) {
        this.scanStart = scanStart;
    }

    public String getScanTime() {
        return scanTime;
    }

    public void setScanTime(String scanTime) {
        this.scanTime = scanTime;
    }

    public String getFilesScanned() {
        return filesScanned;
    }

    public void setFilesScanned(String filesScanned) {
        this.filesScanned = filesScanned;
    }

    public boolean isSastResultsReady() {
        return sastResultsReady;
    }

    public void setSastResultsReady(boolean sastResultsReady) {
        this.sastResultsReady = sastResultsReady;
    }

    public String getLinesOfCodeScanned() {
        return linesOfCodeScanned;
    }

    public void setLinesOfCodeScanned(String linesOfCodeScanned) {
        this.linesOfCodeScanned = linesOfCodeScanned;
    }

    public void setQueryList(List<CxXMLResults.Query> queryList) {
        this.queryList = queryList;
    }

    public List<CxXMLResults.Query> getQueryList() {
        return queryList;
    }

    public byte[] getPDFReport() {
        return PDFReport;
    }

    public void setPDFReport(byte[] PDFReport) {
        this.PDFReport = PDFReport;
    }

    public Boolean getSynchronous() {
        return isSynchronous;
    }

    public void setSynchronous(Boolean synchronous) {
        isSynchronous = synchronous;
    }

    public boolean hasNewResults() {
        return newHighCount + newMediumCount + newLowCount > 0;
    }
}
