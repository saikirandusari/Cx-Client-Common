package cx.restclient.osa.utils;

import cx.restclient.osa.dto.OSAResults;
import cx.restclient.osa.dto.OSASummaryResults;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by Galn on 07/02/2018.
 */
public class OSAUtils {

    private Logger logger;

    public OSAUtils(Logger logger) {
        this.logger = logger;
    }

    public void writeToOsaListToTemp(String osaDependenciesJson) {
        try {
            File temp = new File(FileUtils.getTempDirectory(), "CxOSAFileList.json");
            FileUtils.writeStringToFile(temp, osaDependenciesJson, Charset.defaultCharset());
            logger.info("OSA file list saved to file: ["+temp.getAbsolutePath()+"]");
        } catch (Exception e) {
            logger.info("Failed to write OSA file list to temp directory: " + e.getMessage());
        }

    }

    public static String composeProjectOSASummaryLink(String url, long projectId) {
        return String.format( url + "/CxWebClient/SPA/#/viewer/project/%s", projectId);
    }

    public Properties generateOSAScanConfiguration(String filterPatterns, String archiveIncludes, String scanFolder, boolean installBeforeScan) {
        Properties ret = new Properties();
        filterPatterns = StringUtils.defaultString(filterPatterns);
        archiveIncludes = StringUtils.defaultString(archiveIncludes);

        List<String> inclusions = new ArrayList<String>();
        List<String> exclusions = new ArrayList<String>();
        String[] filters = filterPatterns.split("\\s*,\\s*"); //split by comma and trim (spaces + newline)
        for (String filter : filters) {
            if(StringUtils.isNotEmpty(filter)) {
                if (!filter.startsWith("!") ) {
                    inclusions.add(filter);
                } else if(filter.length() > 1){
                    filter = filter.substring(1); // Trim the "!"
                    exclusions.add(filter);
                }
            }
        }

        String includesString = StringUtils.join(",", inclusions);
        String excludesString = StringUtils.join(",", exclusions);

        if(StringUtils.isNotEmpty(includesString)) {
            ret.put("includes",includesString);
        }

        if(StringUtils.isNotEmpty(excludesString)) {
            ret.put("excludes",excludesString);
        }

        if(StringUtils.isNotEmpty(archiveIncludes)) {
            ret.put("archiveIncludes", archiveIncludes);
        }

        ret.put("archiveExtractionDepth", "4");

        if(installBeforeScan) {
            ret.put("npm.runPreStep", "true");
            ret.put("bower.runPreStep", "true");
        }

        ret.put("d", scanFolder);

        return ret;
    }

    public void printOSAResultsToConsole(OSAResults osaResults) {
        OSASummaryResults osaSummaryResults = osaResults.getOsaSummaryResults();
        logger.info("----------------------------Checkmarx Scan Results(CxOSA):-------------------------------");
        logger.info("");
        logger.info("------------------------");
        logger.info("Vulnerabilities Summary:");
        logger.info("------------------------");
        logger.info("OSA high severity results: " + osaSummaryResults.getTotalHighVulnerabilities());
        logger.info("OSA medium severity results: " + osaSummaryResults.getTotalMediumVulnerabilities());
        logger.info("OSA low severity results: " + osaSummaryResults.getTotalLowVulnerabilities());
        logger.info("Vulnerability score: " + osaSummaryResults.getVulnerabilityScore());
        logger.info("");
        logger.info("-----------------------");
        logger.info("Libraries Scan Results:");
        logger.info("-----------------------");
        logger.info("Open-source libraries: " + osaSummaryResults.getTotalLibraries());
        logger.info("Vulnerable and outdated: " + osaSummaryResults.getVulnerableAndOutdated());
        logger.info("Vulnerable and updated: " + osaSummaryResults.getVulnerableAndUpdated());
        logger.info("Non-vulnerable libraries: " + osaSummaryResults.getNonVulnerableLibraries());
        logger.info("");
        logger.info("OSA scan results location: " + osaResults.getOsaProjectSummaryLink());
        logger.info("-----------------------------------------------------------------------------------------");
    }



}
