package com.cx.restclient.sast.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

import static com.cx.restclient.sast.utils.SASTParam.PROJECT_LINK_FORMAT;
import static com.cx.restclient.sast.utils.SASTParam.SCAN_LINK_FORMAT;


/**
 * Created by Galn on 05/02/2018.
 */
public class SASTResults {

    private long scanId;

    private boolean sastResultsReady = false;
    private int sastHighResults;
    private int sastMediumResults;
    private int sastLowResults;
    private int sastInfoResults;

    private int sastNewHighCount = 0;
    private int sastNewMediumCount = 0;
    private int sastNewLowCount = 0;
    private int sastNewInfoCount = 0;

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
                            sastNewHighCount++;
                            break;
                        case Medium:
                            sastNewMediumCount++;
                            break;
                        case Low:
                            sastNewLowCount++;
                            break;
                        case Info:
                            sastNewInfoCount++;
                            break;
                    }
                }
            }
        }
        this.queryList = reportObj.getQuery();//todo qa
    }

    public void setResults(long scanId, SASTStatisticsResponse statisticsResults, String url, long projectId) {
        setScanId(scanId);
        setSastHighResults(statisticsResults.getHighSeverity());
        setSastMediumResults(statisticsResults.getMediumSeverity());
        setSastLowResults(statisticsResults.getLowSeverity());
        setSastInfoResults(statisticsResults.getInfoSeverity());
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

    public int getSastHighResults() {
        return sastHighResults;
    }

    public void setSastHighResults(int sastHighResults) {
        this.sastHighResults = sastHighResults;
    }

    public int getSastMediumResults() {
        return sastMediumResults;
    }

    public void setSastMediumResults(int sastMediumResults) {
        this.sastMediumResults = sastMediumResults;
    }

    public int getSastLowResults() {
        return sastLowResults;
    }

    public void setSastLowResults(int sastLowResults) {
        this.sastLowResults = sastLowResults;
    }

    public int getSastInfoResults() {
        return sastInfoResults;
    }

    public void setSastInfoResults(int sastInfoResults) {
        this.sastInfoResults = sastInfoResults;
    }

    public int getSastNewHighCount() {
        return sastNewHighCount;
    }

    public void setSastNewHighCount(int sastNewHighCount) {
        this.sastNewHighCount = sastNewHighCount;
    }

    public int getSastNewMediumCount() {
        return sastNewMediumCount;
    }

    public void setSastNewMediumCount(int sastNewMediumCount) {
        this.sastNewMediumCount = sastNewMediumCount;
    }

    public int getSastNewLowCount() {
        return sastNewLowCount;
    }

    public void setSastNewLowCount(int sastNewLowCount) {
        this.sastNewLowCount = sastNewLowCount;
    }

    public int getSastNewInfoCount() {
        return sastNewInfoCount;
    }

    public void setSastNewInfoCount(int sastNewInfoCount) {
        this.sastNewInfoCount = sastNewInfoCount;
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
}
