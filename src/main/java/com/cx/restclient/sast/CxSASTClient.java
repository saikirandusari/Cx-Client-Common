package com.cx.restclient.sast;

import com.cx.restclient.dto.BaseStatus;
import com.cx.restclient.dto.Status;
import com.cx.restclient.common.Waiter;
import com.cx.restclient.configuration.ScanConfiguration;
import com.cx.restclient.httpClient.CxHttpClient;
import com.cx.restclient.httpClient.exception.CxClientException;
import com.cx.restclient.httpClient.exception.CxTokenExpiredException;
import com.cx.restclient.sast.dto.*;
import com.cx.restclient.sast.exception.CxSASTException;
import com.cx.restclient.sast.utils.zip.CxZipUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static com.cx.restclient.httpClient.utils.ClientUtils.convertToJson;
import static com.cx.restclient.httpClient.utils.PARAM.CONTENT_TYPE_APPLICATION_JSON_V1;
import static com.cx.restclient.sast.utils.SASTParam.*;
import static com.cx.restclient.sast.utils.SASTUtils.deleteTempZipFile;

/**
 * Created by Galn on 05/02/2018.
 */

public class CxSASTClient/** implements ICxSASTClient**/ {

    private Logger log;
    private CxHttpClient httpClient;
    private ScanConfiguration config;
    private Integer projectId;

    private int reportTimeoutSec = 500;
    private Waiter<ResponseQueueScanStatus> scanWaiter = new Waiter<ResponseQueueScanStatus>("CxSAST Scan", 20000) {
        @Override
        public ResponseQueueScanStatus getStatus(String id) throws CxClientException, IOException, CxTokenExpiredException {
            return getSASTScanStatus(id);
        }

        @Override
        public void printProgress(ResponseQueueScanStatus scanStatus) {
            printSASTProgress(scanStatus, getStartTime());
        }

        @Override
        public ResponseQueueScanStatus resolveStatus(ResponseQueueScanStatus scanStatus) throws CxClientException {
            return resolveSASTStatus(scanStatus);
        }
    };
    private Waiter<BaseStatus> reportWaiter = new Waiter<BaseStatus>("Scan report", 10000) {
        @Override
        public BaseStatus getStatus(String id) throws CxClientException, IOException, CxTokenExpiredException {
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

    public CxSASTClient(CxHttpClient client, Logger log, ScanConfiguration config, Integer projectId) {
        this.log = log;
        this.httpClient = client;
        this.config = config;
        this.projectId = projectId;
    }

    //**------ API  ------**//

    //CREATE SAST scan
    public int createSASTScan() throws CxSASTException, InterruptedException, IOException {
        log.info("-----------------------------------Create CxSAST Scan:------------------------------------");
        CxLinkObj createScanResponse = null;
        ScanSettingResponse scanSettingResponse;
        ScanSettingRequest scanSettingRequest = new ScanSettingRequest();
        try {

            scanSettingResponse = getScanSetting(projectId);
            scanSettingRequest.setEngineConfigurationId(scanSettingResponse.getEngineConfiguration().getId());
            scanSettingRequest.setEmailNotifications(scanSettingResponse.getEmailNotifications());
                //TODO scanSettingRequest.setPostScanActionId(scanSettingResponse.getPostScanAction());

            scanSettingRequest.setProjectId(projectId);
            scanSettingRequest.setPresetId(config.getPresetId());

            if (config.getEngineConfigurationId() != null) {
                scanSettingRequest.setEngineConfigurationId(config.getEngineConfigurationId());
            }
            //Define createSASTScan settings
            defineScanSetting(scanSettingRequest);

            //prepare sources for scan
            if (config.getZipFile() == null) {
                log.info("Zipping sources");
                File zipTempFile = CxZipUtils.zipWorkspaceFolder(config, MAX_ZIP_SIZE_BYTES, log);
                //Upload zipped source file
                uploadZipFile(zipTempFile, projectId);
                deleteTempZipFile(zipTempFile, log);
            } else {
                uploadZipFile(config.getZipFile(), projectId);
            }

            //Start a new createSASTScan
            CreateScanRequest scanRequest = new CreateScanRequest(projectId, config.getIncremental(), config.getPublic(), config.getForceScan());
            createScanResponse = createScan(scanRequest);
            log.info(String.format("Scan created successfully. CxLink to project state: "+ config.getUrl() + LINK_FORMAT, projectId));

        } catch (Exception ex) {
            throw new CxSASTException(ex.getMessage());//TODO!!!!
        }
        return createScanResponse.getId();
    }

    //GET SAST Scan + Reports
    public SASTResults getSASTResults(int scanId) {
        SASTResults scanResults = new SASTResults();

        try {
            log.info("------------------------------------Get CxSAST Results:-----------------------------------");
            //wait for SAST scan to finish
            log.info("Waiting for CxSAST scan to finish.");
            scanWaiter.waitForTaskToFinish(Integer.toString(scanId), config.getScanTimeoutInMinutes(), log);
            log.info("Scan finished. Retrieving scan results");
    /*if (!StringUtils.isEmpty(config.getScanComment())) {
                Thread.sleep(20000);//Wait for the scan to create
                updateScanComment(config.getScanComment(), createScanResponse.getBaseId());
            }TODO!!  */

            getScanResults();
            //retrieve SAST scan results
            //TODO ProjectScannedData projectScannedData = getScanResults(createScanResponse.getId());
         //   ProjectScannedData projectScannedData = new ProjectScannedData();
         //   projectScannedData.setHighVulnerabilities(3);
        //    projectScannedData.setMediumVulnerabilities(2);
         //   projectScannedData.setLowVulnerabilities(1);
        //    projectScannedData.setInfoVulnerabilities(4);
         /*   projectScannedData.setRiskLevelScore(5);
            projectScannedData.setLoc(5250);
            projectScannedData.setTeamName("CxServer");
            projectScannedData.setTotalVulnerabilities(10);
            projectScannedData.setLastScanID(createScanResponse.getId());
            projectScannedData.setProjectID(config.getProject().getId());

            scanResults.setResults(projectScannedData, config.getUrl());
            PrintUtils.printResultsToConsole(scanResults, log);

            //SAST detailed report
            byte[] cxReport = getScanReport(scanResults.getScanId(), ReportType.XML, CONTENT_TYPE_APPLICATION_XML_V1);
            CxXMLResults reportObj = convertToXMLResult(cxReport);
            scanResults.setScanDetailedReport(reportObj);


            if (config.isGeneratePDFReport()) {
                log.info("Generating PDF report");
                byte[] pdfReport = getScanReport(scanResults.getScanId(), ReportType.PDF, CONTENT_TYPE_APPLICATION_PDF_V1);
                scanResults.setPDFReport(pdfReport);
                if (!StringUtils.isEmpty(config.getReportsDir())) {
                    writePDFReport(pdfReport, config.getReportsDir(), log);
                }
            }*/
            //TODO resolveSASTResults including failing build and fail message
        } catch (Exception e) {
            int i = 0;
            ++i;
            //TODO
        }
        return scanResults;
    }

    public SASTResults getLastSASTResults(Integer projectId) {

      //  TODO return lastscanResults;
        return null;
    }

    //Cancel SAST Scan
    public void cancelSASTScan(long scanId) throws IOException, CxClientException, CxTokenExpiredException {
        log.warn("Scan was canceled");//TODO

        CancelRequest request = new CancelRequest(CurrentStatus.CANCELED.value());
        String json = convertToJson(request);
        StringEntity entity = new StringEntity(json);

        httpClient.patchRequest(SAST_QUEUE_SCAN_STATUS.replace("{scanId}", Long.toString(scanId)), CONTENT_TYPE_APPLICATION_JSON_V1, entity, 200, "cancel SAST scan");
    }


    //**------ Private Methods  ------**//

    private ScanSettingResponse getScanSetting(long projectId) throws IOException, CxClientException, CxTokenExpiredException {
        return httpClient.getRequest(SAST_GET_SCAN_SETTINGS.replace("{projectId}", Long.toString(projectId)), CONTENT_TYPE_APPLICATION_JSON_V1, ScanSettingResponse.class, 200, "Scan setting", false);
    }

//    private ScanSettingRequest updateProjectIncremental(boolean incremental) throws IOException, CxClientException {
//        return httpClient.patchRequest(SAST_GET_SCAN_SETTINGS, CONTENT_TYPE_APPLICATION_JSON_V1, ScanSettingRequest.class, 200, " update Incremental field");
//    }

    private void defineScanSetting(ScanSettingRequest scanSetting) throws IOException, CxClientException, CxTokenExpiredException { //TODO suold return scanId or Void?
        StringEntity entity = new StringEntity(convertToJson(scanSetting));
        httpClient.postRequest(SAST_UPDATE_SCAN_SETTINGS, CONTENT_TYPE_APPLICATION_JSON_V1, entity, CxLinkObj.class, 200, "define scan setting");
    }

    private void updateScanComment(String comment, long scanId) throws CxClientException, IOException, CxTokenExpiredException {
        StringEntity entity = new StringEntity(convertToJson(new Comment(comment)));
        httpClient.patchRequest(SAST_UPDATE_COMMENT.replace("{scanId}", Long.toString(scanId)), CONTENT_TYPE_APPLICATION_JSON_V1, entity, 204, "update comment");
    }

    private void uploadZipFile(File zipFile, long projectId) throws CxClientException, IOException, CxTokenExpiredException {
        InputStreamBody streamBody = new InputStreamBody(new FileInputStream(zipFile.getAbsoluteFile()), ContentType.APPLICATION_OCTET_STREAM, "zippedSource");
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addPart("zippedSource", streamBody);
        HttpEntity entity = builder.build();
        httpClient.postRequest(SAST_ZIP_ATTACHMENTS.replace("{projectId}", Long.toString(projectId)), null, entity, null, 204, "upload ZIP file");
    }

    private CxLinkObj createScan(CreateScanRequest request) throws CxClientException, IOException, CxTokenExpiredException { //TODO suold return scanId or Void?
        StringEntity entity = new StringEntity(convertToJson(request));
        return httpClient.postRequest(SAST_CREATE_SCAN, CONTENT_TYPE_APPLICATION_JSON_V1, entity, CxLinkObj.class, 201, "create new SAST Scan");
    }

    private CxLinkObj getScanResults() throws CxClientException, IOException, CxTokenExpiredException { //TODO suold return scanId or Void?
        return httpClient.getRequest(SAST_CREATE_SCAN, CONTENT_TYPE_APPLICATION_JSON_V1,  CxLinkObj.class, 201, "get SAST results", false);
    }



/*    private ProjectScannedData getScanResults(long projectId) throws CxClientException, IOException {
        ResponseProjectScannedData scanDataResponse = getProjectScannedDisplayData(projectId);
        if (!scanDataResponse.isSuccessful()) {
            throw new CxClientException("Failed to get scan data: " + scanDataResponse.getErrorMessage());
        }
        return scanDataResponse.getProjectScannedData();
    }*/

  /*  private ResponseProjectScannedData getProjectScannedDisplayData(long projectId) throws IOException, CxClientException {
        return httpClient.getRequest(SAST_DISPLAY_DATA_TODO.replace("{projectId}", Long.toString(projectId)), CONTENT_TYPE_APPLICATION_JSON_V1, ResponseProjectScannedData.class, 200, "failed to get SAST results", false);//TODO
    }*/

    private CreateReportResponse createScanReport(CreateReportRequest reportRequest) throws CxClientException, IOException, CxTokenExpiredException {
        StringEntity entity = new StringEntity(convertToJson(reportRequest));
        return httpClient.postRequest(SAST_CREATE_REPORT, CONTENT_TYPE_APPLICATION_JSON_V1, entity, CreateReportResponse.class, 202, "to create " + reportRequest.getReportType() + " scan report");
    }

    private byte[] getScanReport(long scanId, ReportType reportType, String contentType) throws CxClientException, InterruptedException, IOException, CxTokenExpiredException {
        CreateReportRequest reportRequest = new CreateReportRequest(scanId, reportType.name());
        CreateReportResponse createReportResponse = createScanReport(reportRequest);
        int reportId = createReportResponse.getReportId();
        reportWaiter.waitForTaskToFinish(Long.toString(reportId), reportTimeoutSec / 60, log);
        byte[] scanReport = getReport(reportId, contentType);

        return scanReport;
    }

    private byte[] getReport(long reportId, String contentType) throws CxClientException, IOException, CxTokenExpiredException {
        return httpClient.getRequest(SAST_GET_REPORT.replace("{reportId}", Long.toString(reportId)), contentType, byte[].class, 200, " scan report: " + reportId, false);
    }

    //SCAN Waiter - overload methods
    private ResponseQueueScanStatus getSASTScanStatus(String scanId) throws CxClientException, IOException, CxTokenExpiredException {
        ResponseQueueScanStatus scanStatus = httpClient.getRequest(SAST_QUEUE_SCAN_STATUS.replace("{scanId}", scanId), CONTENT_TYPE_APPLICATION_JSON_V1, ResponseQueueScanStatus.class, 200, "SASTScan status", false);//TODO scanId and meddage

        String currentStatus;//TODO
        if(scanStatus == null){ //The scan didn't arrive the Queue
            scanStatus =  new ResponseQueueScanStatus(Status.IN_PROGRESS); //TODO fix after Yeal fixingthe issue
            scanStatus.setStage(new CxValueObj(1, "not started :(")); //TODO fix after Yeal Queueu fix
            return scanStatus;

        }else{
            currentStatus = scanStatus.getStage().getValue();
        }

        if (CurrentStatus.FAILED.value().equals(currentStatus) || CurrentStatus.CANCELED.value().equals(currentStatus) ||
                CurrentStatus.DELETED.value().equals(currentStatus) || CurrentStatus.UNKNOWN.value().equals(currentStatus)) {
            scanStatus.setBaseStatus(Status.FAILED);
        } else if (CurrentStatus.FINISHED.value().equals(currentStatus)) {
            scanStatus.setBaseStatus(Status.SUCCEEDED);
        } else {
            scanStatus.setBaseStatus(Status.IN_PROGRESS);
        }

        return scanStatus;
    }

    private void printSASTProgress(ResponseQueueScanStatus scanStatus, long startTime) {
        long hours = (System.currentTimeMillis() - startTime) / 3600000;
        long minutes = ((System.currentTimeMillis() - startTime) % 3600000) / 60000;
        long seconds = ((System.currentTimeMillis() - startTime) % 60000) / 1000;
        String hoursStr = (hours < 10) ? ("0" + Long.toString(hours)) : (Long.toString(hours));
        String minutesStr = (minutes < 10) ? ("0" + Long.toString(minutes)) : (Long.toString(minutes));
        String secondsStr = (seconds < 10) ? ("0" + Long.toString(seconds)) : (Long.toString(seconds));
        log.info("Waiting for SAST scan results. Elapsed time: " + hoursStr + ":" + minutesStr + ":" + secondsStr + ". " + scanStatus.getTotalPercent() +
                "% processed." + scanStatus.getStageDetails() + " Status: " + scanStatus.getStage().getValue() + "."); //TODO like OSA please. think ont the queue status
    }

    private ResponseQueueScanStatus resolveSASTStatus(ResponseQueueScanStatus scanStatus) throws CxClientException {
        if (Status.SUCCEEDED == scanStatus.getBaseStatus()) {
            log.info("SAST scan finished.");
            return scanStatus;
        } else {
            throw new CxClientException("SAST scan cannot be completed. status [" + scanStatus.getBaseStatus() + "].\n CxValueObj message: ["/**+scanStatus.getStageMessage()+*"]"*/);//TODO Queue nd stuff
        }
    }

    //Report Waiter - overload methods
    private BaseStatus getReportStatus(String reportId) throws CxClientException, IOException, CxTokenExpiredException {
        ReportStatus reportStatus = httpClient.getRequest(SAST_GET_REPORT_STATUS.replace("{reportId}", reportId), CONTENT_TYPE_APPLICATION_JSON_V1, ReportStatus.class, null, " report status", false);
        BaseStatus status = new BaseStatus(reportId);
        if (reportStatus != null) { //not equals 404(not found) and not deleted
            String currentStatus = reportStatus.getStatus().getValue();
            if (currentStatus.equals(ReportStatusEnum.INPROCESS.value())) {
                status.setBaseStatus(Status.IN_PROGRESS);
            } else if (currentStatus.equals(ReportStatusEnum.FAILED.value())) {
                status.setBaseStatus(Status.FAILED);
            } else {
                status.setBaseStatus(Status.SUCCEEDED);
            }
        } else {
            status.setBaseStatus(Status.FAILED);
        }

        return status;
    }

    private void printReportProgress() {
        log.info("Waiting for server to generate pdf report " + (reportTimeoutSec - (System.currentTimeMillis() / 1000)) + " seconds left for timeout");
    }

    private BaseStatus resolveReportStatus(BaseStatus reportStatus) throws CxClientException {
        if (Status.SUCCEEDED == reportStatus.getBaseStatus()) {
            return reportStatus;
        } else {
            throw new CxClientException("Generation of scan report [id=" + reportStatus.getBaseId() + "] failed.");
        }
    }

}
