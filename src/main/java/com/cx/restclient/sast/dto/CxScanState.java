package com.cx.restclient.sast.dto;

/**
 * Created by Galn on 29/03/2018.
 */
public class CxScanState {
    private String path;
    private String sourceId;
    private int filesCount;
    private int linesOfCode;
    private int failedLinesOfCode;
    private String cxVersion;
    private String languageStateCollection;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public int getFilesCount() {
        return filesCount;
    }

    public void setFilesCount(int filesCount) {
        this.filesCount = filesCount;
    }

    public int getLinesOfCode() {
        return linesOfCode;
    }

    public void setLinesOfCode(int linesOfCode) {
        this.linesOfCode = linesOfCode;
    }

    public int getFailedLinesOfCode() {
        return failedLinesOfCode;
    }

    public void setFailedLinesOfCode(int failedLinesOfCode) {
        this.failedLinesOfCode = failedLinesOfCode;
    }

    public String getCxVersion() {
        return cxVersion;
    }

    public void setCxVersion(String cxVersion) {
        this.cxVersion = cxVersion;
    }

    public String getLanguageStateCollection() {
        return languageStateCollection;
    }

    public void setLanguageStateCollection(String languageStateCollection) {
        this.languageStateCollection = languageStateCollection;
    }
}
