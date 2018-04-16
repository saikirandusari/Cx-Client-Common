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
    private int high = 0;
    private int medium = 0;
    private int low = 0;
    private int info = 0;

    private int newHigh = 0;
    private int newMedium = 0;
    private int newLow = 0;
    private int newInfo = 0;

    private String sastScanLink;
    private String sastProjectLink;

    private String scanStart;
    private String scanTime;
    private String filesScanned;
    private String LOC;
    private List<CxXMLResults.Query> queryList;

    private byte[] PDFReport;

    private boolean isSynchronous;
    private boolean thresholdfExceeded;

    private enum Severity {
        High, Medium, Low, Info;
    }

    public void setScanDetailedReport(CxXMLResults reportObj) {
        this.scanStart = reportObj.getScanStart();
        this.scanTime = reportObj.getScanTime();
        this.LOC = reportObj.getLinesOfCodeScanned();
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
                            newHigh++;
                            break;
                        case Medium:
                            newMedium++;
                            break;
                        case Low:
                            newLow++;
                            break;
                        case Info:
                            newInfo++;
                            break;
                    }
                }
            }
        }
        this.queryList = reportObj.getQuery();//todo qa
    }

    public void setResults(long scanId, SASTStatisticsResponse statisticsResults, String url, long projectId) {
        setScanId(scanId);
        setHigh(statisticsResults.getHighSeverity());
        setMedium(statisticsResults.getMediumSeverity());
        setLow(statisticsResults.getLowSeverity());
        setInfo(statisticsResults.getInfoSeverity());
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

    public int getHigh() {
        return high;
    }

    public void setHigh(int high) {
        this.high = high;
    }

    public int getMedium() {
        return medium;
    }

    public void setMedium(int medium) {
        this.medium = medium;
    }

    public int getLow() {
        return low;
    }

    public void setLow(int low) {
        this.low = low;
    }

    public int getInfo() {
        return info;
    }

    public void setInfo(int info) {
        this.info = info;
    }

    public int getNewHigh() {
        return newHigh;
    }

    public void setNewHigh(int newHigh) {
        this.newHigh = newHigh;
    }

    public int getNewMedium() {
        return newMedium;
    }

    public void setNewMedium(int newMedium) {
        this.newMedium = newMedium;
    }

    public int getNewLow() {
        return newLow;
    }

    public void setNewLow(int newLow) {
        this.newLow = newLow;
    }

    public int getNewInfo() {
        return newInfo;
    }

    public void setNewInfo(int newInfo) {
        this.newInfo = newInfo;
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

    public String getLOC() {
        return LOC;
    }

    public void setLOC(String LOC) {
        this.LOC = LOC;
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

    public boolean getSynchronous() {
        return isSynchronous;
    }

    public void setSynchronous(boolean synchronous) {
        isSynchronous = synchronous;
    }

    public boolean hasNewResults() {
        return newHigh + newMedium + newLow > 0;
    }


}
