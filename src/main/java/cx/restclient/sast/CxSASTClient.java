package cx.restclient.sast;

import cx.restclient.common.BaseStatus;
import cx.restclient.common.Status;
import cx.restclient.common.Waiter;
import cx.restclient.dto.CxScanConfiguration;
import cx.restclient.httpClient.CxHttpClient;
import cx.restclient.httpClient.exception.CxClientException;
import cx.restclient.sast.dto.*;
import cx.restclient.sast.exception.CxSASTException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;

import static cx.restclient.httpClient.utils.ClientUtils.convertToJson;
import static cx.restclient.sast.utils.CxSASTParam.*;
import static cx.restclient.sast.utils.PrintUtils.printBuildFailure;
import static cx.restclient.sast.utils.PrintUtils.printResultsToConsole;
import static cx.restclient.sast.utils.ReportsUtils.writePDFReport;
import static cx.restclient.sast.utils.SASTUtils.*;

/**
 * Created by Galn on 05/02/2018.
 */

public class CxSASTClient implements ICxSASTClient {

    private Logger log;
    private CxHttpClient httpClient;
    private CxScanConfiguration config;
    private SASTResults scanResults = new SASTResults();
    private int reportTimeoutSec = 500;
    private Waiter<SASTScanStatus> scanWaiter = new Waiter<SASTScanStatus>("CxSAST Scan", 20000) {
        @Override
        public SASTScanStatus getStatus(long id) throws CxClientException, IOException {
            return getSASTScanStatus(id);
        }
        @Override
        public void printProgress(SASTScanStatus scanStatus) {
            printSASTProgress(scanStatus, getStartTime());
        }
        @Override
        public SASTScanStatus resolveStatus(SASTScanStatus scanStatus) throws CxClientException {
            return resolveSASTStatus(scanStatus);
        }
    };
    private Waiter<BaseStatus> reportWaiter = new Waiter<BaseStatus>("Scan report", 10000) {
        @Override
        public BaseStatus getStatus(long id) throws CxClientException, IOException {
            return getReportStatus(id);
        }
        @Override
        public void printProgress(BaseStatus scanStatus) {
            printReportProgress();
        }
        @Override
        public BaseStatus resolveStatus(BaseStatus scanStatus) throws CxClientException {
            return resolveReportStatus(scanStatus);
        }
    };

    public CxSASTClient(CxHttpClient client, Logger log, CxScanConfiguration config) {
        this.log = log;
        this.httpClient = client;
        this.config = config;
    }

    //**------ API  ------**//

    //CREATE SAST scan
    public CreateScanResponse createSASTScan() throws CxSASTException, InterruptedException, IOException {

        log.info("-----------------------------------Create CxSAST Scan:------------------------------------");

        CreateScanResponse createScanResponse = null;
        ScanSetting scanSetting = new ScanSetting();
        try {
            Project project = getProjectByName(config.getProjectName(), config.getTeamId());
            if (project == null) { // Project is new
                if (config.isDenyProject()) {
                    {
                        return denyProject();
                    }
                }
                //Create newProject
                CreateProjectRequest request = new CreateProjectRequest(config.getProjectName(), config.getTeamId(), config.isPublic());
                project = createNewProject(request);

            } else { //Project already exist
                scanSetting = getScanSetting(project.getId());

            }
            long projectId = project.getId();
            scanSetting.setProjectId(projectId);
            scanSetting.setPresetId(config.getPresetId());
            if (config.getEngineConfigurationId() != null) {
                scanSetting.setEngineConfigurationId(config.getEngineConfigurationId());
            }
            if (!StringUtils.isEmpty(config.getScanComment())) {
                updateScanComment(config.getScanComment());
            }
            //Define createSASTScan settings
            defineScanSetting(scanSetting);

            //prepare sources for scan
            if (config.getZipFile() == null) {
                log.info("Zipping sources");
                File zipTempFile = zipWorkspaceFolder(config, MAX_ZIP_SIZE_BYTES, log);
                //Upload zipped source file
                uploadZipFile(zipTempFile, projectId);
                deleteTempZipFile(zipTempFile, log);
            } else {
                uploadZipFile(config.getZipFile(), projectId);
            }

            //Start a new createSASTScan
            ScanRequest scanRequest = new ScanRequest(projectId, config.isIncremental(), config.isPublic(), config.isForceScan());
            ;
            createScanResponse = createScan(scanRequest);
            scanResults = addSASTConfig(config, createScanResponse.getId());
            log.info("Scan created successfully. CxLink to project state: " + scanResults.getSastProjectLink());

        } catch (Exception ex) {
            throw new CxSASTException(ex.getMessage());//TODO!!!!
        }
        return createScanResponse;
    }

    //GET Scan + Reports
    public SASTResults getSASTResults(CreateScanResponse createScanResponse) {
        try {
            log.info("------------------------------------Get CxSAST Results:-----------------------------------");
            //wait for SAST scan to finish
            log.info("Waiting for CxSAST scan to finish.");
            scanWaiter.waitForScanToFinish(createScanResponse.getId(), config.getScanTimeoutInMinutes(), log);
            log.info("Scan finished. Retrieving scan results");

            //retrieve SAST scan results
            ProjectScannedData projectScannedData = getScanResults(createScanResponse.getId());

            //SAST detailed report
            byte[] cxReport = getScanReport(scanResults.getScanId(), ReportType.XML, CONTENT_TYPE_APPLICATION_XML_V1);
            CxXMLResults reportObj = convertToXMLResult(cxReport);
            scanResults.setScanDetailedReport(reportObj);

            scanResults = addSASTResults(scanResults, projectScannedData, config.getUrl());
            printResultsToConsole(scanResults, log);

            if (config.isGeneratePDFReport()) {

                log.info("Generating PDF report");
                byte[] pdfReport = getScanReport(scanResults.getScanId(), ReportType.PDF, CONTENT_TYPE_APPLICATION_PDF_V1);
                scanResults.setPDFReport(pdfReport);
                if (config.getReportDir() != null) {
                    writePDFReport(pdfReport, config.getReportDir(), log);
                }
            }
            //TODO resolveSASTResults including failing build and fail message
        } catch (Exception e) {
            //TODO
        }
        return scanResults;
    }

    //Cancel Scan
    public void cancelSASTScan(String scanId) throws IOException, CxClientException {
        log.warn("Scan was canceled");//TODO
        httpClient.patchRequest(SAST_QUEUE_SCAN_STATUS.replace("{scanId}", scanId), CONTENT_TYPE_APPLICATION_JSON_V1, null, 200, "cancel SAST scan");
    }


    //**------ Private Methods  ------**//
    private CreateScanResponse denyProject() { //TODO
        StringBuilder str = new StringBuilder("Creation of the new project [" + config.getProjectName() + "] is not authorized. Please use an existing project.");
        str.append("\nYou can enable the creation of new projects by disabling the Deny new Checkmarx projects creation checkbox in the Checkmarx plugin global settings.\n");
        printBuildFailure(str, null, null, log);
        //return taskResultBuilder.failed().build();
        return null;//TODO
    }

    private Project createNewProject(CreateProjectRequest request) throws CxClientException, IOException {
        StringEntity entity = new StringEntity(convertToJson(request));
        return httpClient.postRequest(SAST_SCAN_PROJECT, CONTENT_TYPE_APPLICATION_JSON_V1, entity, Project.class, 200, "create new project: " + request.getName());
    }

    private Project getProjectByName(String projectName, String teamId) throws IOException, CxClientException {
        String projectNamePath = SAST_GET_PROJECT.replace("{name}", projectName).replace("{teamId}", teamId);
        return httpClient.getRequest(projectNamePath, CONTENT_TYPE_APPLICATION_JSON_V1, Project.class, 200, " Project by projectId and teamId");
    }

    private ScanSetting getScanSetting(long projectId) throws IOException, CxClientException {
        return httpClient.getRequest(SAST_GET_SCAN_SETTINGS, CONTENT_TYPE_APPLICATION_JSON_V1, ScanSetting.class, 200, " Scan setting");
    }

//    private ScanSetting updateProjectIncremental(boolean incremental) throws IOException, CxClientException {
//        return httpClient.patchRequest(SAST_GET_SCAN_SETTINGS, CONTENT_TYPE_APPLICATION_JSON_V1, ScanSetting.class, 200, " update Incremental field");
//    }

    private void defineScanSetting(ScanSetting scanSetting) throws IOException, CxClientException { //TODO suold return scanId or Void?
        StringEntity entity = new StringEntity(convertToJson(scanSetting));
        httpClient.postRequest(SAST_UPDATE_SCAN_SETTINGS, CONTENT_TYPE_APPLICATION_JSON_V1, entity, CreateScanResponse.class, 200, "define scan setting");
    }

    private void updateScanComment(String comment) throws CxClientException, IOException {
        StringEntity entity = new StringEntity(convertToJson(new Comment(comment)));
        httpClient.patchRequest(SAST_UPDATE_COMMENT, CONTENT_TYPE_APPLICATION_JSON_V1, entity, 200, "update comment");
    }

    private void uploadZipFile(File zipFile, long projectId) throws CxClientException, IOException {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE); //TODO need it???
        builder.addPart("zippedSource", new FileBody(zipFile));
        HttpEntity entity = builder.build();

        httpClient.postRequest(SAST_ZIP_ATTACHMENT.replace("{projectId}", Long.toString(projectId)), CONTENT_TYPE_APPLICATION_JSON_V1, entity, CreateScanResponse.class, 204, "upload ZIP file");
    }

    private CreateScanResponse createScan(ScanRequest request) throws CxClientException, IOException { //TODO suold return scanId or Void?
        StringEntity entity = new StringEntity(convertToJson(request));
        return httpClient.postRequest(SAST_CREATE_SCAN, CONTENT_TYPE_APPLICATION_JSON_V1, entity, CreateScanResponse.class, 201, "create new createSASTScan");
    }

    private ProjectScannedData getScanResults(long projectId) throws CxClientException, IOException {
        ResponseProjectScannedData scanDataResponse = getProjectScannedDisplayData(projectId);
        if (!scanDataResponse.isSuccessful()) {
            throw new CxClientException("Failed to get scan data: " + scanDataResponse.getErrorMessage());
        }
        return scanDataResponse.getProjectScannedData();
    }

    private ResponseProjectScannedData getProjectScannedDisplayData(long projectId) throws IOException, CxClientException {
        return (ResponseProjectScannedData) httpClient.getRequest(SAST_DISPLAY_DATA_TODO.replace("{projectId}", Long.toString(projectId)), CONTENT_TYPE_APPLICATION_JSON_V1, SASTResults.class, 200, "failed to get SAST results");//TODO
    }

    private CreateReportResponse createScanReport(CreateReportRequest reportRequest) throws CxClientException, IOException {
        StringEntity entity = new StringEntity(convertToJson(reportRequest));
        return httpClient.postRequest(SAST_CREATE_REPORT, CONTENT_TYPE_APPLICATION_JSON_V1, entity, CreateReportResponse.class, 202, "to create " + reportRequest.getReportType() + " scan report");
    }

    private byte[] getScanReport(long scanId, ReportType reportType, String contentType) throws CxClientException, InterruptedException, IOException {
        CreateReportRequest reportRequest = new CreateReportRequest(scanId, reportType.name());
        CreateReportResponse createReportResponse = createScanReport(reportRequest);
        int reportId = createReportResponse.getReportId();
        reportWaiter.waitForScanToFinish(reportId, reportTimeoutSec / 60, log);
        GetReportResponse scanReport = getReport(reportId, contentType);

        return scanReport.getScanResults();
    }

    private GetReportResponse getReport(long reportId, String contentType) throws CxClientException, IOException {
        return httpClient.getRequest(SAST_CREATE_REPORT.replace("{reportId}", Long.toString(reportId)), contentType, GetReportResponse.class, 200, "to retrieve scan report: " + reportId);
    }

    //SCAN Waiter - overload methods
    private SASTScanStatus getSASTScanStatus(long scanId) throws CxClientException, IOException {
        SASTScanStatus scanStatus = httpClient.getRequest(SAST_SCAN_QUEUE.replace("{scanId}", String.valueOf(scanId)), CONTENT_TYPE_APPLICATION_JSON_V1, SASTScanStatus.class, 200, "failed to get createSASTScan status");//TODO scanId and meddage
        CurrentStatus currentStatus = scanStatus.getName();

        if (CurrentStatus.FAILED.equals(currentStatus) || CurrentStatus.CANCELED.equals(currentStatus) ||
                CurrentStatus.DELETED.equals(currentStatus) || CurrentStatus.UNKNOWN.equals(currentStatus)) {
            scanStatus.setStatus(Status.FAILED);
        } else if (CurrentStatus.FINISHED.equals(currentStatus)) {
            scanStatus.setStatus(Status.SUCCEEDED);
        } else {
            scanStatus.setStatus(Status.IN_PROGRESS);
        }

        return scanStatus;
    }

    private void printSASTProgress(SASTScanStatus scanStatus, long startTime) {
        long hours = (System.currentTimeMillis() - startTime) / 3600000;
        long minutes = ((System.currentTimeMillis() - startTime) % 3600000) / 60000;
        long seconds = ((System.currentTimeMillis() - startTime) % 60000) / 1000;
        String hoursStr = (hours < 10) ? ("0" + Long.toString(hours)) : (Long.toString(hours));
        String minutesStr = (minutes < 10) ? ("0" + Long.toString(minutes)) : (Long.toString(minutes));
        String secondsStr = (seconds < 10) ? ("0" + Long.toString(seconds)) : (Long.toString(seconds));
        //  log.info("Waiting for SAST scan results. Elapsed time: " + hoursStr + ":" + minutesStr + ":" + secondsStr + ". " +  scanStatus.getTotalPercent() +
        //  "% processed. Status: " + scanStatus.getDetails().getStage() + "."); //TODO like OSA please. think ont the queue status
    }

    private SASTScanStatus resolveSASTStatus(SASTScanStatus scanStatus) throws CxClientException {
        if (Status.SUCCEEDED == scanStatus.getStatus()) {
            log.info("SAST scan finished.");
            return scanStatus;
        } else {
            throw new CxClientException("SAST scan cannot be completed. status [" + scanStatus.getName() + "].\n Stage message: ["/**+scanStatus.getStageMessage()+*"]"*/);//TODO Queue nd stuff
        }
    }

    //Report Waiter - overload methods
    private BaseStatus getReportStatus(long reportId) throws CxClientException, IOException {
        StatusLine reportStatus = httpClient.getRequestNoValid(SAST_GET_REPORT_STATUS.replace("{reportId}", Long.toString(reportId)), CONTENT_TYPE_APPLICATION_JSON_V1, StatusLine.class);
        BaseStatus status = new BaseStatus(reportId);
        if (reportStatus.getStatusCode() == 200) {
            status.setStatus(Status.SUCCEEDED);
        }
        if (reportStatus.getStatusCode() == 404) {
            status.setStatus(Status.FAILED);
        }
        return status;
    }

    private void printReportProgress() {
        log.info("Waiting for server to generate pdf report " + (reportTimeoutSec - (System.currentTimeMillis() / 1000)) + " sec left to timeout");
    }

    private BaseStatus resolveReportStatus(BaseStatus reportStatus) throws CxClientException {
        if (Status.SUCCEEDED == reportStatus.getStatus()) {
            return reportStatus;
        } else {
            throw new CxClientException("Generation of scan report [id=" + reportStatus.getId() + "] failed.");
        }
    }

}
