package com.cx.restclient.osa.dto;

import java.util.List;

/**
 * Created by Galn on 07/02/2018.
 */
public class OSAResults {
    private String osaScanId;
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

    public void setOsaProjectSummaryLink(String url, long projectId) {
        this.osaProjectSummaryLink = String.format(url + "/CxWebClient/SPA/#/viewer/project/%s", projectId);
    }

    public boolean isOsaResultsReady() {
        return osaResultsReady;
    }

    public void setOsaResultsReady(boolean osaResultsReady) {
        this.osaResultsReady = osaResultsReady;
    }

    public String getOsaScanId() {
        return osaScanId;
    }

    public void setOsaScanId(String osaScanId) {
        this.osaScanId = osaScanId;
    }
}
