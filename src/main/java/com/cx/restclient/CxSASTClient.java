package com.cx.restclient;

import com.cx.restclient.common.Waiter;
import com.cx.restclient.configuration.CxScanConfig;
import com.cx.restclient.dto.Status;
import com.cx.restclient.exception.CxClientException;
import com.cx.restclient.exception.CxSASTException;
import com.cx.restclient.exception.CxTokenExpiredException;
import com.cx.restclient.httpClient.CxHttpClient;
import com.cx.restclient.sast.dto.*;
import com.cx.restclient.sast.utils.SASTUtils;
import com.cx.restclient.sast.utils.zip.CxZipUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.slf4j.Logger;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static com.cx.restclient.httpClient.utils.ContentType.*;
import static com.cx.restclient.httpClient.utils.HttpClientHelper.convertToJson;
import static com.cx.restclient.sast.utils.SASTParam.*;
import static com.cx.restclient.sast.utils.SASTUtils.*;


/**
 * Created by Galn on 05/02/2018.
 */

class CxSASTClient/** implements ICxSASTClient**/
{
    private Logger log;
    private CxHttpClient httpClient;
    private CxScanConfig config;
    private int reportTimeoutSec = 500;
    private Waiter<ResponseQueueScanStatus> sastWaiter = new Waiter<ResponseQueueScanStatus>("CxSAST scan", 1) {
        @Override
        public ResponseQueueScanStatus getStatus(String id) throws CxClientException, IOException, CxTokenExpiredException {
            return getSASTScanStatus(id);
        }

        @Override
        public void printProgress(ResponseQueueScanStatus scanStatus) {
            printSASTProgress(scanStatus, getStartTimeSec());
        }

        @Override
        public ResponseQueueScanStatus resolveStatus(ResponseQueueScanStatus scanStatus) throws CxClientException {
            return resolveSASTStatus(scanStatus);
        }
    };
    private Waiter<ReportStatus> reportWaiter = new Waiter<ReportStatus>("Scan report", 5) {
        @Override
        public ReportStatus getStatus(String id) throws CxClientException, IOException, CxTokenExpiredException {
            return getReportStatus(id);
        }

        @Override
        public void printProgress(ReportStatus reportStatus) {
            printReportProgress(reportStatus, getStartTimeSec());
        }

        @Override
        public ReportStatus resolveStatus(ReportStatus reportStatus) throws CxClientException {
            return resolveReportStatus(reportStatus);
        }
    };

    CxSASTClient(CxHttpClient client, Logger log, CxScanConfig config) {
        this.log = log;
        this.httpClient = client;
        this.config = config;
    }

    //**------ API  ------**//

    //CREATE SAST scan
    long createSASTScan(long projectId) throws CxSASTException, InterruptedException, IOException {
        log.info("-----------------------------------Create CxSAST Scan:------------------------------------");
        CxID createScanResponse = null;
        try {
            ScanSettingResponse scanSettingResponse = getScanSetting(projectId);

            ScanSettingRequest scanSettingRequest = new ScanSettingRequest();
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
            log.info(String.format("SAST Scan created successfully. Link to project state: " + config.getUrl() + LINK_FORMAT, projectId));

        } catch (Exception ex) {
            throw new CxSASTException(ex.getMessage());//TODO!!!!
        }


        return createScanResponse.getId();
    }

    //GET SAST results + reports
    public SASTResults getSASTResults(long scanId, long projectId) throws Exception {
        SASTResults sastResults = new SASTResults();
        try {
            log.info("------------------------------------Get CxSAST Results:-----------------------------------");
            //wait for SAST scan to finish
            log.info("Waiting for CxSAST scan to finish.");
            sastWaiter.waitForTaskToFinish(Long.toString(scanId), config.getSastScanTimeoutInMinutes() * 60, log);
            log.info("Retrieving SAST scan results");
            //Update comment if needed
            if (!StringUtils.isEmpty(config.getScanComment())) {
                updateScanComment(config.getScanComment(), scanId);
            }
            //retrieve SAST scan results
            sastResults = retrieveSASTResults(scanId, projectId);

            //PDF report
            if (config.getGeneratePDFReport()) {
                log.info("Generating PDF report");
                byte[] pdfReport = getScanReport(sastResults.getScanId(), ReportType.PDF, CONTENT_TYPE_APPLICATION_PDF_V1);
                sastResults.setPDFReport(pdfReport);
                if (config.getReportsDir() != null) {
                    writePDFReport(pdfReport, config.getReportsDir(), log);
                }
            }
        } catch (Exception e) {
            int i = 0;
            ++i;
            //TODO
            throw e;
        }
        return sastResults;
    }

    private SASTResults retrieveSASTResults(long scanId, long projectId) throws InterruptedException, CxTokenExpiredException, CxClientException, IOException, JAXBException {
        SASTResults sastResults = new SASTResults();
        SASTStatisticsResponse statisticsResults = getScanStatistics(scanId);
        sastResults.setResults(scanId, statisticsResults, config.getUrl(), projectId);

        //SAST detailed report
        byte[] cxReport = getScanReport(sastResults.getScanId(), ReportType.XML, CONTENT_TYPE_APPLICATION_XML_V1);
        CxXMLResults reportObj = convertToXMLResult(cxReport);
        sastResults.setScanDetailedReport(reportObj);


        SASTUtils.printSASTResultsToConsole(sastResults, log);
        return sastResults;
    }

    SASTResults getLastSASTResults(long projectId) throws Exception {
        log.info("---------------------------------Get Last CxSAST Results:--------------------------------");
        List<LastScanResponse> scanList = getLastSASTStatus(projectId);
        for (LastScanResponse s : scanList) {
            if (CurrentStatus.FINISHED.value().equals(s.getStatus().getName())) {
                return retrieveSASTResults(s.getId(), projectId);
            }
        }
        return null;
    }

    //Cancel SAST Scan
    public void cancelSASTScan(long scanId) throws IOException, CxClientException, CxTokenExpiredException {
        UpdateScanStatusRequest request = new UpdateScanStatusRequest(CurrentStatus.CANCELED);
        String json = convertToJson(request);
        StringEntity entity = new StringEntity(json);
        httpClient.patchRequest(SAST_QUEUE_SCAN_STATUS.replace("{scanId}", Long.toString(scanId)), CONTENT_TYPE_APPLICATION_JSON_V1, entity, 200, "cancel SAST scan");
        log.info("Scan was canceled");
    }


    //**------ Private Methods  ------**//

    private ScanSettingResponse getScanSetting(long projectId) throws IOException, CxClientException, CxTokenExpiredException {
        return httpClient.getRequest(SAST_GET_SCAN_SETTINGS.replace("{projectId}", Long.toString(projectId)), CONTENT_TYPE_APPLICATION_JSON_V1, ScanSettingResponse.class, 200, "Scan setting", false);
    }

    private void defineScanSetting(ScanSettingRequest scanSetting) throws IOException, CxClientException, CxTokenExpiredException {
        StringEntity entity = new StringEntity(convertToJson(scanSetting));
        httpClient.postRequest(SAST_UPDATE_SCAN_SETTINGS, CONTENT_TYPE_APPLICATION_JSON_V1, entity, CxID.class, 200, "define scan setting");
    }

    private void updateScanComment(String comment, long scanId) throws CxClientException, IOException, CxTokenExpiredException {
        StringEntity entity = new StringEntity(convertToJson(new Comment(comment)));
        httpClient.patchRequest(SAST_SCAN.replace("{scanId}", Long.toString(scanId)), CONTENT_TYPE_APPLICATION_JSON_V1, entity, 204, "update comment");
    }

    private void uploadZipFile(File zipFile, long projectId) throws CxClientException, IOException, CxTokenExpiredException {
        InputStreamBody streamBody = new InputStreamBody(new FileInputStream(zipFile.getAbsoluteFile()), ContentType.APPLICATION_OCTET_STREAM, "zippedSource");
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addPart("zippedSource", streamBody);
        HttpEntity entity = builder.build();
        httpClient.postRequest(SAST_ZIP_ATTACHMENTS.replace("{projectId}", Long.toString(projectId)), null, entity, null, 204, "upload ZIP file");
    }

    private CxID createScan(CreateScanRequest request) throws CxClientException, IOException, CxTokenExpiredException {
        StringEntity entity = new StringEntity(convertToJson(request));
        return httpClient.postRequest(SAST_CREATE_SCAN, CONTENT_TYPE_APPLICATION_JSON_V1, entity, CxID.class, 201, "create new SAST Scan");
    }

    private SASTStatisticsResponse getScanStatistics(long scanId) throws CxClientException, IOException, CxTokenExpiredException {
        return httpClient.getRequest(SAST_SCAN_RESULTS_STATISTICS.replace("{scanId}", Long.toString(scanId)), CONTENT_TYPE_APPLICATION_JSON_V1, SASTStatisticsResponse.class, 200, "SAST scan statistics", false);
    }

    private List<LastScanResponse> getLastSASTStatus(long projectId) throws CxClientException, IOException, CxTokenExpiredException {
        return (List<LastScanResponse>) httpClient.getRequest(SAST_GET_PROJECT_SCANS.replace("{projectId}", Long.toString(projectId)), CONTENT_TYPE_APPLICATION_JSON_V1, LastScanResponse.class, 200, "last SAST scan ID", true);
    }

    private CreateReportResponse createScanReport(CreateReportRequest reportRequest) throws CxClientException, IOException, CxTokenExpiredException {
        StringEntity entity = new StringEntity(convertToJson(reportRequest));
        return httpClient.postRequest(SAST_CREATE_REPORT, CONTENT_TYPE_APPLICATION_JSON_V1, entity, CreateReportResponse.class, 202, "to create " + reportRequest.getReportType() + " scan report");
    }

    private byte[] getScanReport(long scanId, ReportType reportType, String contentType) throws CxClientException, IOException, CxTokenExpiredException, InterruptedException {
        CreateReportRequest reportRequest = new CreateReportRequest(scanId, reportType.name());
        CreateReportResponse createReportResponse = createScanReport(reportRequest);
        int reportId = createReportResponse.getReportId();
        reportWaiter.waitForTaskToFinish(Long.toString(reportId), reportTimeoutSec, log);

        return getReport(reportId, contentType);
    }

    private byte[] getReport(long reportId, String contentType) throws CxClientException, IOException, CxTokenExpiredException {
        return httpClient.getRequest(SAST_GET_REPORT.replace("{reportId}", Long.toString(reportId)), contentType, byte[].class, 200, " scan report: " + reportId, false);
    }

    //SCAN Waiter - overload methods
    private ResponseQueueScanStatus getSASTScanStatus(String scanId) throws CxClientException, IOException, CxTokenExpiredException {
        ResponseQueueScanStatus scanStatus = httpClient.getRequest(SAST_QUEUE_SCAN_STATUS.replace("{scanId}", scanId), CONTENT_TYPE_APPLICATION_JSON_V1, ResponseQueueScanStatus.class, 200, "SAST scan status", false);
        String currentStatus = scanStatus.getStage().getValue();

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
        long elapsedSec = System.currentTimeMillis() / 1000 - startTime;
        long hours = elapsedSec / 3600;
        long minutes = elapsedSec % 3600 / 60;
        long seconds = elapsedSec % 60;
        String hoursStr = (hours < 10) ? ("0" + Long.toString(hours)) : (Long.toString(hours));
        String minutesStr = (minutes < 10) ? ("0" + Long.toString(minutes)) : (Long.toString(minutes));
        String secondsStr = (seconds < 10) ? ("0" + Long.toString(seconds)) : (Long.toString(seconds));
        log.info("Waiting for SAST scan results. Elapsed time: " + hoursStr + ":" + minutesStr + ":" + secondsStr + ". " + scanStatus.getTotalPercent() +
                "% processed. Status: " + scanStatus.getStage().getValue() + ".");
    }

    private ResponseQueueScanStatus resolveSASTStatus(ResponseQueueScanStatus scanStatus) throws CxClientException {
        if (Status.SUCCEEDED == scanStatus.getBaseStatus()) {
            log.info("SAST scan finished successfully.");
            return scanStatus;
        } else {
            throw new CxClientException("SAST scan cannot be completed. status [" + scanStatus.getStage().getValue() + "]");
        }
    }

    //Report Waiter - overload methods
    private ReportStatus getReportStatus(String reportId) throws CxClientException, IOException, CxTokenExpiredException {
        ReportStatus reportStatus = httpClient.getRequest(SAST_GET_REPORT_STATUS.replace("{reportId}", reportId), CONTENT_TYPE_APPLICATION_JSON_V1, ReportStatus.class, 200, " report status", false);

        String currentStatus = reportStatus.getStatus().getValue();
        if (currentStatus.equals(ReportStatusEnum.INPROCESS.value())) {
            reportStatus.setBaseStatus(Status.IN_PROGRESS);
        } else if (currentStatus.equals(ReportStatusEnum.FAILED.value())) {
            reportStatus.setBaseStatus(Status.FAILED);
        } else {
            reportStatus.setBaseStatus(Status.SUCCEEDED);
        }

        return reportStatus;
    }

    private void printReportProgress(ReportStatus reportStatus, long startTime) {
        String reportType = reportStatus.getContentType().replace("application/", "");
        log.info("Waiting for server to generate " + reportType + " report. " + (startTime + reportTimeoutSec - (System.currentTimeMillis() / 1000)) + " seconds left to timeout");
    }

    private ReportStatus resolveReportStatus(ReportStatus reportStatus) throws CxClientException {
        if (Status.SUCCEEDED == reportStatus.getBaseStatus()) {
            return reportStatus;
        } else {
            throw new CxClientException("Generation of scan report [id=" + reportStatus.getBaseId() + "] failed.");
        }
    }
}
