package com.cx.restclient.sast.utils;

import com.cx.restclient.exception.CxClientException;
import com.cx.restclient.sast.dto.CxXMLResults;
import com.cx.restclient.sast.dto.SASTResults;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.cx.restclient.common.CxPARAM.CX_REPORT_LOCATION;
import static com.cx.restclient.sast.utils.SASTParam.PDF_REPORT_NAME;

/**
 * Created by Galn on 07/02/2018.
 */
public abstract class SASTUtils {

    public static void deleteTempZipFile(File zipTempFile, Logger log) {
        if (zipTempFile.exists() && !zipTempFile.delete()) {
            log.warn("Failed to delete temporary zip file: " + zipTempFile.getAbsolutePath());
        } else {
            log.info("Temporary file deleted");
        }
    }

    public static CxXMLResults convertToXMLResult(byte[] cxReport) throws CxClientException {
        CxXMLResults reportObj = null;
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(cxReport);
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(CxXMLResults.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            reportObj = (CxXMLResults) unmarshaller.unmarshal(byteArrayInputStream);

        } catch (JAXBException e) {
            throw new CxClientException("Failed to parse xml report: " + e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(byteArrayInputStream);
        }
        return reportObj;
    }

    public static void printSASTResultsToConsole(SASTResults scanResults, Logger logger) {

        String highNew = scanResults.getNewHigh() > 0 ? " (" + scanResults.getNewHigh() + " new)" : "";
        String mediumNew = scanResults.getNewMedium() > 0 ? " (" + scanResults.getNewMedium() + " new)" : "";
        String lowNew = scanResults.getNewLow() > 0 ? " (" + scanResults.getNewLow() + " new)" : "";
        String infoNew = scanResults.getNewInfo() > 0 ? " (" + scanResults.getNewInfo() + " new)" : "";

        logger.info("----------------------------Checkmarx Scan Results(CxSAST):-------------------------------");
        logger.info("High severity results: " + scanResults.getHigh() + highNew);
        logger.info("Medium severity results: " + scanResults.getMedium() + mediumNew);
        logger.info("Low severity results: " + scanResults.getLow() + lowNew);
        logger.info("Information severity results: " + scanResults.getInformation() + infoNew);
        logger.info("");
        logger.info("Scan results location: " + scanResults.getSastScanLink());
        logger.info("------------------------------------------------------------------------------------------\n");
    }

    //PDF Report
    public static void writePDFReport(byte[] scanReport, File workspace, Logger log) {
        try {
            String now = new SimpleDateFormat("dd_MM_yyyy-HH_mm_ss").format(new Date());
            String pdfFileName = PDF_REPORT_NAME + "_" + now + ".pdf";
            FileUtils.writeByteArrayToFile(new File(workspace + CX_REPORT_LOCATION, pdfFileName), scanReport);
            log.info("PDF report location: " + workspace + CX_REPORT_LOCATION + File.separator + pdfFileName);
        } catch (Exception e) {
            log.error("Failed to write PDF report to workspace: ", e.getMessage());
        }
    }
}
