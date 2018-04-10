package com.cx.restclient.sast.utils;

/**
 * Created by Galn on 12/02/2018.
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Galn on 25/10/2017.
 */
public abstract class ReportsUtils {
    private static final String PDF_REPORT_NAME = "CxSASTReport";
    private static final String CX_REPORT_LOCATION = File.separator + "Checkmarx" + File.separator + "Reports";
    public static final String OSA_LIBRARIES_NAME = "CxOSALibraries";
    public static final String OSA_VULNERABILITIES_NAME = "CxOSAVulnerabilities";
    public static final String OSA_SUMMARY_NAME = "CxOSASummary";

    private SimpleDateFormat ft = new SimpleDateFormat("dd_MM_yyyy-HH_mm_ss");
    private ObjectMapper objectMapper = new ObjectMapper();

    //SAST PDF
    public static void writePDFReport(byte[] scanReport, String workspace, Logger log) throws InterruptedException {

        try {
            SimpleDateFormat df = new SimpleDateFormat("dd_MM_yyyy-HH_mm_ss");
            String now = df.format(new Date());
            String pdfFileName = PDF_REPORT_NAME + "_" + now + ".pdf";
            FileUtils.writeByteArrayToFile(new File(workspace + CX_REPORT_LOCATION, pdfFileName), scanReport);
            log.info("PDF report location: " + workspace + CX_REPORT_LOCATION + File.separator + pdfFileName);
        } catch (Exception e) {
            log.error("Failed to write PDF report to workspace: ", e.getMessage());
        }
    }

   /* public void createOSASummaryJsonReport(File workDirectory, CxLoggerAdapter log, OSASummaryResults osaSummaryResults) throws IOException, CxClientException {
        writeJsonToFile(OSA_SUMMARY_NAME, osaSummaryResults, workDirectory, log);
    }

    public List<Library> createOSALibrariesJsonReport(File workDirectory, String scanId, CxLoggerAdapter log, CxClientService cxClientService) throws IOException, CxClientException {
        List<Library> libraries = cxClientService.getOSALibraries(scanId);
        writeJsonToFile(OSA_LIBRARIES_NAME, libraries, workDirectory, log);

        return libraries;
    }

    public List<CVE> createOSAVulnerabilitiesJsonReport(File workDirectory, String scanId, CxLoggerAdapter log, CxClientService cxClientService) throws IOException, CxClientException {
        List<CVE> osaVulnerabilities = cxClientService.getOSAVulnerabilities(scanId);
        writeJsonToFile(OSA_VULNERABILITIES_NAME, osaVulnerabilities, workDirectory, log);

        return osaVulnerabilities;
    }

    private void writeJsonToFile(String name, Object jsonObj, File workDirectory, CxLoggerAdapter log) throws IOException {
        String fileName = name + "_" + now + ".json";
        File jsonFile = new File(workDirectory + CX_REPORT_LOCATION, fileName);
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObj);
        FileUtils.writeStringToFile(jsonFile, json);
        log.info(name + " json location: " + workDirectory + CX_REPORT_LOCATION + File.separator + fileName);

    }*/

}
