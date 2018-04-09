package com.cx.restclient.sast.utils;

/**
 * Created by Galn on 06/02/2018.
 */
public class SASTParam {
    public static final String SAST_ENGINE_CONFIG = "sast/engineConfigurations";
    public static final String SAST_UPDATE_SCAN_SETTINGS = "sast/scanSettings"; //Update preset and configuration
    public static final String SAST_GET_SCAN_SETTINGS = "/sast/scanSettings/{projectId}"; //Update preset and configuration
    public static final String SAST_CREATE_SCAN = "sast/scans"; //Run a new Scan
    public static final String SAST_SCAN_STATUS = "sast/scans/{scanId}"; //Get Scan status (by scan ID)
    public static final String SAST_SCAN_QUEUE = "sast/scansQueue";
    public static final String SAST_QUEUE_SCAN_STATUS = "sast/scansQueue/{scanId}";
    public static final String SAST_DISPLAY_DATA_TODO = "sast/displayData/{projectId}";//TODO
    public static final String SAST_UPDATE_COMMENT = "sast/scans/{scanId}";

    public static final String SAST_GET_PROJECT = "projects?projectname={name}&teamid={teamId}";// Get  project)
    public static final String SAST_PROJECT_BY_ID = "projects/{projectId}"; //GetProjectConfiguration
    public static final String SAST_ZIP_ATTACHMENTS = "projects/{projectId}/sourceCode/attachments";//Attach ZIP file

    //Once it has results
    public static final String SAST_CREATE_REPORT = "reports/sastScan/"; //Create new report (get ID)
    public static final String SAST_GET_REPORT_STATUS = "reports/sastScan/{reportId}/status"; //Get report status
    public static final String SAST_GET_REPORT = "reports/sastScan/{reportId}"; //Get report status

    //Zip param
    public static final long MAX_ZIP_SIZE_BYTES = 209715200;
    public static final String TEMP_FILE_NAME_TO_ZIP = "zippedSource";


    public static final String LINK_FORMAT = "/CxWebClient/portal#/projectState/%d/Summary";
}
