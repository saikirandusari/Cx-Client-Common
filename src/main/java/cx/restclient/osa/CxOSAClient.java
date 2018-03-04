package cx.restclient.osa;

import com.fasterxml.jackson.databind.type.TypeFactory;
import cx.restclient.common.Status;
import cx.restclient.common.Waiter;
import cx.restclient.dto.CxScanConfiguration;
import cx.restclient.httpClient.CxHttpClient;
import cx.restclient.httpClient.exception.CxClientException;
import cx.restclient.osa.dto.*;
import cx.restclient.osa.exception.CxOSAException;
import cx.restclient.osa.utils.OSAUtils;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.whitesource.fs.ComponentScan;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import static cx.restclient.httpClient.utils.ClientUtils.convertToJson;
import static cx.restclient.osa.utils.CxOSAParam.*;
import static cx.restclient.osa.utils.OSAUtils.composeProjectOSASummaryLink;
import static cx.restclient.sast.utils.CxSASTParam.CONTENT_TYPE_APPLICATION_JSON_V1;

/**
 * Created by Galn on 05/02/2018.
 */
public class CxOSAClient implements ICxOSAClient {

    private CxHttpClient httpClient;
    private Logger log;
    private CxScanConfiguration config;
    private OSAUtils OSAUtils = new OSAUtils(log);
    private OSAResults osaResults = new OSAResults();
    private Waiter<OSAScanStatus> osaWaiter = new Waiter<OSAScanStatus>("CxOSA", 20000) {
        @Override
        public OSAScanStatus getStatus(long id) throws CxClientException, IOException {
            return getOSAScanStatus(id);
        }

        @Override
        public void printProgress(OSAScanStatus scanStatus) {
            printOSAProgress(scanStatus, getStartTime());
        }

        @Override
        public OSAScanStatus resolveStatus(OSAScanStatus scanStatus) throws CxClientException {
            return resolveOSAStatus(scanStatus);
        }
    };

    public CxOSAClient(CxHttpClient client, Logger log, CxScanConfiguration config) {
        this.log = log;
        this.httpClient = client;
        this.config = config;
    }

    //API
    public CreateOSAScanResponse createOSAScan() throws IOException, InterruptedException, CxClientException {
        log.info("Creating OSA scan");
        String osaDependenciesJson = config.getOsaDependenciesJson();
        if (osaDependenciesJson == null) {
            osaDependenciesJson = resolveOSADependencies();
        }
        return sendOSAScan(osaDependenciesJson);
    }

    private String resolveOSADependencies() {
        log.info("Scanning for CxOSA compatible files");
        Properties scannerProperties = OSAUtils.generateOSAScanConfiguration(config.getOsaFilterPattern(),
                config.getOsaArchiveIncludePatterns(), config.getCheckoutDir().getAbsolutePath(), config.isOsaInstallBeforeScan());
        ComponentScan componentScan = new ComponentScan(scannerProperties);
        String osaDependenciesJson = componentScan.scan();
        OSAUtils.writeToOsaListToTemp(osaDependenciesJson);

        return osaDependenciesJson;
    }

    public OSAResults getOSAResults(String scanId) throws CxClientException, IOException, CxOSAException, InterruptedException {
        log.info("Waiting for OSA scan to finish");
        OSAScanStatus osaScanStatus = osaWaiter.waitForScanToFinish(Integer.getInteger(scanId), -1, log);
        log.info("OSA scan finished successfully");
        log.info("Creating OSA reports");
        OSASummaryResults osaSummaryResults = getOSAScanSummaryResults(scanId);

        List<Library> osaLibraries = getOSALibraries(scanId);
        List<CVE> osaVulnerabilities = getOSAVulnerabilities(scanId);
        osaResults.setResults(osaSummaryResults, osaLibraries, osaVulnerabilities, osaScanStatus);
        OSAUtils.printOSAResultsToConsole(osaResults);

        return osaResults;
    }

    //Private Methods
    private CreateOSAScanResponse sendOSAScan(String osaDependenciesJson) throws CxClientException, IOException {
        log.info("Sending OSA scan request");
        CreateOSAScanResponse osaScan = scanOSA(config.getProjectId(), osaDependenciesJson);
        String osaProjectSummaryLink = composeProjectOSASummaryLink(config.getUrl(), config.getProjectId());
        osaResults.setOsaProjectSummaryLink(osaProjectSummaryLink);
        log.info("OSA scan created successfully");

        return osaScan;
    }

    private CreateOSAScanResponse scanOSA(long projectId, String osaDependenciesJson) throws IOException, CxClientException {
        CreateOSAScanRequest req = new CreateOSAScanRequest(projectId, osaDependenciesJson);
        StringEntity entity = new StringEntity(convertToJson(req));
        return httpClient.postRequest(OSA_SCAN_PROJECT, CONTENT_TYPE_APPLICATION_JSON_V1, entity, CreateOSAScanResponse.class, 201, "create OSA scan");
    }

    private OSASummaryResults getOSAScanSummaryResults(String scanId) throws CxClientException, IOException {
        String relativePath = OSA_SCAN_SUMMARY + SCAN_ID_QUERY_PARAM + scanId;
        return (OSASummaryResults) httpClient.getRequest(relativePath, CONTENT_TYPE_APPLICATION_JSON_V1, OSASummaryResults.class, 200, "OSA scan summary results");
    }

    private List<Library> getOSALibraries(String scanId) throws CxClientException, IOException {
        String relPath = OSA_SCAN_LIBRARIES + SCAN_ID_QUERY_PARAM + scanId + ITEM_PER_PAGE_QUERY_PARAM + MAX_ITEMS;
        return httpClient.getRequest(relPath, CONTENT_TYPE_APPLICATION_JSON_V1, TypeFactory.defaultInstance().constructCollectionType(List.class, Library.class), 200, "OSA libraries");
    }

    private List<CVE> getOSAVulnerabilities(String scanId) throws CxClientException, IOException {
        String relPath = OSA_SCAN_VULNERABILITIES + SCAN_ID_QUERY_PARAM + scanId + ITEM_PER_PAGE_QUERY_PARAM + MAX_ITEMS;
        return httpClient.getRequest(relPath, CONTENT_TYPE_APPLICATION_JSON_V1, TypeFactory.defaultInstance().constructCollectionType(List.class, CVE.class), 200, "OSA vulnerabilities");
    }

    //Waiter - overload methods
    private OSAScanStatus getOSAScanStatus(long scanId) throws CxClientException, IOException {
        String relPath = OSA_SCAN_STATUS.replace("{scanId}", String.valueOf(scanId)); //TODO scanId int or string or both?
        OSAScanStatus scanStatus = httpClient.getRequest(relPath, CONTENT_TYPE_APPLICATION_JSON_V1, OSASummaryResults.class, 200, "OSA scan status");
        int stateId = scanStatus.getState().getId();

        if (OSAScanStatusEnum.SUCCEEDED.getNum() == stateId) {
            scanStatus.setStatus(Status.SUCCEEDED);
        } else if (OSAScanStatusEnum.IN_PROGRESS.getNum() == stateId || OSAScanStatusEnum.NOT_STARTED.getNum() == stateId) {
            scanStatus.setStatus(Status.IN_PROGRESS);
        } else {
            scanStatus.setStatus(Status.FAILED);
        }
        return scanStatus;
    }

    private void printOSAProgress(OSAScanStatus scanStatus, long startTime) {
        long hours = (System.currentTimeMillis() - startTime) / 3600000;
        long minutes = ((System.currentTimeMillis() - startTime) % 3600000) / 60000;
        long seconds = ((System.currentTimeMillis() - startTime) % 60000) / 1000;
        String hoursStr = (hours < 10) ? ("0" + Long.toString(hours)) : (Long.toString(hours));
        String minutesStr = (minutes < 10) ? ("0" + Long.toString(minutes)) : (Long.toString(minutes));
        String secondsStr = (seconds < 10) ? ("0" + Long.toString(seconds)) : (Long.toString(seconds));
        log.info("Waiting for OSA scan results. Elapsed time: " + hoursStr + ":" + minutesStr + ":" + secondsStr + ". " +
                "Status: " + scanStatus.getState().getName());
    }

    private OSAScanStatus resolveOSAStatus(OSAScanStatus scanStatus) throws CxClientException {
        if (Status.FAILED == scanStatus.getStatus()) {
            String failedMsg = scanStatus.getState() == null ? "" : "status [" + scanStatus.getState().getName() + "]. Reason: " + scanStatus.getState().getFailureReason();
            throw new CxClientException("OSA scan cannot be completed. " + failedMsg);
        }
        if (Status.SUCCEEDED == scanStatus.getStatus()) {
            log.info("OSA scan finished.");
            return scanStatus;
        }
        return scanStatus;
    }
}

