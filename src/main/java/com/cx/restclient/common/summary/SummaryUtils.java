package com.cx.restclient.common.summary;

import com.cx.restclient.configuration.ScanConfiguration;
import com.cx.restclient.osa.dto.OSAResults;
import com.cx.restclient.sast.dto.SASTResults;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import static com.cx.restclient.common.CxPARAM.OPTION_FALSE;
import static com.cx.restclient.common.CxPARAM.OPTION_TRUE;
import static com.cx.restclient.common.summary.SummaryConst.*;


/**
 * Created by Galn on 11/03/2018.
 */
public abstract class SummaryUtils {

    //  public final Logger log = LoggerFactory.getLogger(SummaryUtils.class);//TODO

    public static String generateJellySummary(SASTResults sastResults, OSAResults osaResults, Logger log) {
             String summaryStr = "";//TODO generateSummary(sastResults, osaResults, log);
           return convertToJelly(summaryStr);
    }


    private static String convertToJelly(String summaryStr) {
        return "";
    }


    public static String generateSummary(SASTResults sastResults, OSAResults osaResults, ScanConfiguration config, Logger log) {
        String resultsTemplate = getResultsTemplate(log);
        if (resultsTemplate == null) {
            return "";
        }

        String ret = resultsTemplate;
        if (sastResults != null && sastResults.isSastResultsReady()) {
            //SAST: fill html with results
            ret = ret
                    .replace(SAST_RESULTS_READY, OPTION_TRUE)
                    .replaceAll(HIGH_RESULTS, String.valueOf(sastResults.getHighResults()))
                    .replace(MEDIUM_RESULTS, String.valueOf(sastResults.getMediumResults()))
                    .replace(LOW_RESULTS, String.valueOf(sastResults.getLowResults()))
                    .replace(SAST_SUMMARY_RESULTS_LINK, String.valueOf(sastResults.getSastProjectLink()))
                    .replace(SAST_SCAN_RESULTS_LINK, String.valueOf(sastResults.getSastScanLink()))
                    .replace(THRESHOLD_ENABLED, String.valueOf(config.getThresholdsEnabled()))
                    .replace(HIGH_THRESHOLD, String.valueOf(config.getHighThreshold()))
                    .replace(MEDIUM_THRESHOLD, String.valueOf(config.getMediumThreshold()))
                    .replace(LOW_THRESHOLD, String.valueOf(config.getLowThreshold()))
                    .replace(SCAN_START_DATE, String.valueOf(sastResults.getScanStart()))
                    .replace(SCAN_TIME, String.valueOf(sastResults.getScanTime()))
                    .replace(SCAN_FILES_SCANNED, String.valueOf(sastResults.getFilesScanned()))
                    .replace(SCAN_LOC_SCANNED, String.valueOf(sastResults.getLinesOfCodeScanned()))
                    .replace(SCAN_QUERY_LIST, String.valueOf(sastResults.getQueryList()));
        } else {
            //SAST: fill html with empty values
            ret = ret
                    .replace(SAST_RESULTS_READY, OPTION_FALSE)
                    .replace(SYNC_MODE, String.valueOf(config.getSynchronous()))
                    .replace(SAST_SUMMARY_RESULTS_LINK, StringUtils.defaultString(sastResults.getSastProjectLink()))
                    .replaceAll(HIGH_RESULTS, "0")
                    .replace(MEDIUM_RESULTS, "0")
                    .replace(LOW_RESULTS, "0")
                    .replace(SAST_SCAN_RESULTS_LINK, "")
                    .replace(THRESHOLD_ENABLED, OPTION_FALSE)
                    .replace(HIGH_THRESHOLD, "0")
                    .replace(MEDIUM_THRESHOLD, "0")
                    .replace(LOW_THRESHOLD, "0")
                    .replace(SCAN_START_DATE, "")
                    .replace(SCAN_TIME, "")
                    .replace(SCAN_FILES_SCANNED, "null")
                    .replace(SCAN_LOC_SCANNED, "null")
                    .replace(SCAN_QUERY_LIST, "null");
        }

        if (osaResults != null && osaResults.isOsaResultsReady()) {
            //OSA: fill html with results
            ret = ret
                    .replace(OSA_RESULTS_READY, OPTION_TRUE)
                    .replace(OSA_ENABLED, OPTION_TRUE)
                    .replace(OSA_HIGH_RESULTS, String.valueOf(osaResults.getResults().getTotalHighVulnerabilities()))
                    .replace(OSA_MEDIUM_RESULTS, String.valueOf(osaResults.getResults().getTotalMediumVulnerabilities()))
                    .replace(OSA_LOW_RESULTS, String.valueOf(osaResults.getResults().getTotalLowVulnerabilities()))
                    .replace(OSA_SUMMARY_RESULTS_LINK, String.valueOf(osaResults.getOsaProjectSummaryLink()))
                    .replace(OSA_THRESHOLD_ENABLED, String.valueOf(config.getThresholdsEnabled()))
                    .replace(OSA_HIGH_THRESHOLD, String.valueOf(config.getOsaHighThreshold()))
                    .replace(OSA_MEDIUM_THRESHOLD, String.valueOf(config.getOsaMediumThreshold()))
                    .replace(OSA_LOW_THRESHOLD, String.valueOf(config.getOsaLowThreshold()))
                    .replace(OSA_VULNERABLE_LIBRARIES, String.valueOf(osaResults.getResults().getHighVulnerabilityLibraries()))
                    .replace(OSA_OK_LIBRARIES, String.valueOf(osaResults.getResults().getNonVulnerableLibraries()))
                    .replace(OSA_CVE_LIST, String.valueOf(osaResults.getOsaVulnerabilities()))
                    .replace(OSA_LIBRARIES, String.valueOf(osaResults.getOsaLibraries()))
                    .replace(OSA_START_TIME, osaResults.getOsaScanStatus().getStartAnalyzeTime())
                    .replace(OSA_END_TIME, String.valueOf(osaResults.getOsaScanStatus().getEndAnalyzeTime()));

        } else {
            //SAST: fill html with empty values
            ret = ret
                    .replace(OSA_RESULTS_READY, OPTION_FALSE)
                    .replace(OSA_ENABLED, OPTION_FALSE)
                    .replace(OSA_HIGH_RESULTS, "0")
                    .replace(OSA_MEDIUM_RESULTS, "0")
                    .replace(OSA_LOW_RESULTS, "0")
                    .replace(OSA_SUMMARY_RESULTS_LINK, "")
                    .replace(OSA_THRESHOLD_ENABLED, OPTION_FALSE)
                    .replace(OSA_HIGH_THRESHOLD, "0")
                    .replace(OSA_MEDIUM_THRESHOLD, "0")
                    .replace(OSA_LOW_THRESHOLD, "0")
                    .replace(OSA_VULNERABLE_LIBRARIES, "0")
                    .replace(OSA_OK_LIBRARIES, "0")
                    .replace(OSA_CVE_LIST, "null")
                    .replace(OSA_LIBRARIES, "null")
                    .replace(OSA_START_TIME, "")
                    .replace(OSA_END_TIME, "");
        }
        ret = ret.replace(SYNC_MODE, String.valueOf(config.getSynchronous()));

        return ret;
    }

    private static String getResultsTemplate(Logger log) {
        String ret = null;
        InputStream resourceAsStream = SummaryUtils.class.getResourceAsStream("/com/cx/plugin/resultsTemplate.html");
        if (resourceAsStream != null) {
            try {
                ret = IOUtils.toString(resourceAsStream, Charset.defaultCharset().name());
            } catch (IOException e) {
                log.warn("Failed to get results template", e.getMessage());
            } finally {
                try {
                    resourceAsStream.close();
                } catch (IOException e) {
                    log.warn("Failed to close streams", e.getMessage());
                }
            }
        }
        return ret;
    }


  }

