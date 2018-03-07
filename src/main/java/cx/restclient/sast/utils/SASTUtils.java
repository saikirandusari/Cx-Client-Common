package cx.restclient.sast.utils;

import cx.restclient.dto.CxScanConfiguration;
import cx.restclient.sast.dto.CxXMLResults;
import cx.restclient.sast.dto.ProjectScannedData;
import cx.restclient.sast.dto.SASTResults;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;

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

    public static SASTResults addSASTConfig(CxScanConfiguration config) {
        SASTResults sastResults = new SASTResults();
        sastResults.setThresholdEnabled(config.isThresholdsEnabled());
        if (config.isThresholdsEnabled()) {
            String highThreshold = (config.getHighThreshold() == null ? "null" : String.valueOf(config.getHighThreshold()));
            String mediumThreshold = (config.getMediumThreshold() == null ? "null" : String.valueOf(config.getMediumThreshold()));
            String lowThreshold = (config.getLowThreshold() == null ? "null" : String.valueOf(config.getLowThreshold()));

            sastResults.setHighThreshold(highThreshold);
            sastResults.setMediumThreshold(mediumThreshold);
            sastResults.setLowThreshold(lowThreshold);
        }
        sastResults.setSastProjectLink(config.getUrl(), config.getProject().getId());

        return sastResults;
    }

    public static SASTResults addSASTResults(SASTResults sastResults, ProjectScannedData projectScannedData, String url) {
        sastResults.setScanId(projectScannedData.getLastScanID());   //TODO!!!!! Need it??
        sastResults.setHighResults(Integer.toString(projectScannedData.getHighVulnerabilities())); //TODO what if null??
        sastResults.setMediumResults(Integer.toString(projectScannedData.getMediumVulnerabilities())); //TODO what if null??
        sastResults.setLowResults(Integer.toString(projectScannedData.getLowVulnerabilities())); //TODO what if null??
        sastResults.setInfoResults(Integer.toString(projectScannedData.getInfoVulnerabilities())); //TODO what if null??
        sastResults.setSastScanLink(url, projectScannedData.getLastScanID(), projectScannedData.getProjectID());
        sastResults.setSastResultsReady(true); //TODO check

        return sastResults;
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


}
