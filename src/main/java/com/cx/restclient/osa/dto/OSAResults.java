package com.cx.restclient.osa.dto;

import com.cx.restclient.dto.CxScanConfiguration;

import java.util.List;

/**
 * Created by Galn on 07/02/2018.
 */
public class OSAResults {
    private boolean thresholdEnabled;
    private Integer osaHighThreshold;
    private Integer osaMediumThreshold;
    private Integer osaLowThreshold;

    private OSASummaryResults results;
    private List<Library> osaLibraries;
    private List<CVE> osaVulnerabilities;
    private OSAScanStatus osaScanStatus;
    private String osaProjectSummaryLink;
    private boolean osaResultsReady = false;


    public OSAResults() {
    }

    public void setResults(OSASummaryResults osaSummaryResults, List<Library> OSALibraries, List<CVE> OSAVulnerabilities, OSAScanStatus osaScanStatus) {
        setResults(osaSummaryResults);
        setOsaLibraries(OSALibraries);
        setOsaVulnerabilities(OSAVulnerabilities);
        setOsaScanStatus(osaScanStatus);
        setOsaResultsReady(true);
    }

    public void setConfig(CxScanConfiguration config) {
        setThresholdEnabled(config.isThresholdsEnabled());
        setOsaHighThreshold(config.getOsaHighThreshold());
        setOsaMediumThreshold(config.getOsaMediumThreshold());
        setOsaLowThreshold(config.getOsaLowThreshold());
    }

    public Integer getOsaHighThreshold() {
        return osaHighThreshold;
    }

    public void setOsaHighThreshold(Integer osaHighThreshold) {
        this.osaHighThreshold = osaHighThreshold;
    }

    public Integer getOsaMediumThreshold() {
        return osaMediumThreshold;
    }

    public void setOsaMediumThreshold(Integer osaMediumThreshold) {
        this.osaMediumThreshold = osaMediumThreshold;
    }

    public Integer getOsaLowThreshold() {
        return osaLowThreshold;
    }

    public void setOsaLowThreshold(Integer osaLowThreshold) {
        this.osaLowThreshold = osaLowThreshold;
    }

    public List<Library> getOsaLibraries() {
        return osaLibraries;
    }

    public void setOsaLibraries(List<Library> osaLibraries) {
        this.osaLibraries = osaLibraries;
    }

    public List<CVE> getOsaVulnerabilities() {
        return osaVulnerabilities;
    }

    public void setOsaVulnerabilities(List<CVE> osaVulnerabilities) {
        this.osaVulnerabilities = osaVulnerabilities;
    }

    public OSASummaryResults getResults() {
        return results;
    }

    public void setResults(OSASummaryResults results) {
        this.results = results;
    }

    public OSAScanStatus getOsaScanStatus() {
        return osaScanStatus;
    }

    public void setOsaScanStatus(OSAScanStatus osaScanStatus) {
        this.osaScanStatus = osaScanStatus;
    }

    public String getOsaProjectSummaryLink() {
        return osaProjectSummaryLink;
    }

    public void setOsaProjectSummaryLink(String osaProjectSummaryLink) {
        this.osaProjectSummaryLink = osaProjectSummaryLink;
    }

    public boolean isOsaResultsReady() {
        return osaResultsReady;
    }

    public void setOsaResultsReady(boolean osaResultsReady) {
        this.osaResultsReady = osaResultsReady;
    }

    public boolean isThresholdEnabled() {
        return thresholdEnabled;
    }

    public void setThresholdEnabled(boolean thresholdEnabled) {
        this.thresholdEnabled = thresholdEnabled;
    }
}
