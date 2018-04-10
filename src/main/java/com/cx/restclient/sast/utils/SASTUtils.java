package com.cx.restclient.sast.utils;

import com.cx.restclient.sast.dto.CxXMLResults;
import com.cx.restclient.sast.dto.SASTResults;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

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

    public static CxXMLResults convertToXMLResult(byte[] cxReport) throws IOException, JAXBException {
        CxXMLResults reportObj = null;
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(cxReport);
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(CxXMLResults.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            reportObj = (CxXMLResults) unmarshaller.unmarshal(byteArrayInputStream);

        } finally {
            IOUtils.closeQuietly(byteArrayInputStream);
        }
        return reportObj;
    }

    public static void printSastResultsToConsole(SASTResults scanResults, Logger logger) {

        String highNew = scanResults.getSastNewHighCount() > 0 ? " (" + scanResults.getSastNewHighCount() + " new)" : "";
        String mediumNew = scanResults.getSastNewMediumCount() > 0 ? " (" + scanResults.getSastNewMediumCount() + " new)" : "";
        String lowNew = scanResults.getSastNewLowCount() > 0 ? " (" + scanResults.getSastNewLowCount() + " new)" : "";
        String infoNew = scanResults.getSastNewInfoCount() > 0 ? " (" + scanResults.getSastNewInfoCount() + " new)" : "";

        logger.info("----------------------------Checkmarx Scan Results(CxSAST):-------------------------------");
        logger.info("High severity results: " + scanResults.getSastHighResults() + highNew);
        logger.info("Medium severity results: " + scanResults.getSastMediumResults() + mediumNew);
        logger.info("Low severity results: " + scanResults.getSastLowResults() + lowNew);
        logger.info("Information severity results: " + scanResults.getSastInfoResults() + infoNew);
        logger.info("");
        logger.info("Scan results location: " + scanResults.getSastScanLink());
        logger.info("------------------------------------------------------------------------------------------\n");
    }
}
