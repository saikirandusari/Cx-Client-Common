package com.cx.restclient.osa.utils;

import com.cx.restclient.osa.dto.OSAResults;
import com.cx.restclient.osa.dto.OSASummaryResults;
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
public abstract class OSAUtils {

    public static void writeToOsaListToTemp(String osaDependenciesJson, Logger log) {
        try {
            File temp = new File(FileUtils.getTempDirectory(), "CxOSAFileList.json");
            FileUtils.writeStringToFile(temp, osaDependenciesJson, Charset.defaultCharset());
            log.info("OSA file list saved to file: ["+temp.getAbsolutePath()+"]");
        } catch (Exception e) {
            log.info("Failed to write OSA file list to temp directory: " + e.getMessage());
        }

    }

    public static String composeProjectOSASummaryLink(String url, long projectId) {
        return String.format( url + "/CxWebClient/SPA/#/viewer/project/%s", projectId);
    }

    public static Properties generateOSAScanConfiguration(String filterPatterns, String archiveIncludes, String scanFolder, boolean installBeforeScan) {
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

        String includesString = String.join(",", inclusions);
        String excludesString = String.join(",", exclusions);

        if(StringUtils.isNotEmpty(includesString)) {
            ret.put("includes",includesString);
        }

        if(StringUtils.isNotEmpty(excludesString)) {
            ret.put("excludes",excludesString);
        }

        if(StringUtils.isNotEmpty(archiveIncludes)) {
            String[] archivePatterns = archiveIncludes.split("\\s*,\\s*"); //split by comma and trim (spaces + newline)
            for (int i = 0; i < archivePatterns.length; i++) {
                if(StringUtils.isNotEmpty(archivePatterns[i]) && archivePatterns[i].startsWith("*.")) {
                    archivePatterns[i] = "**/" + archivePatterns[i];
                }
            }
            archiveIncludes = String.join(",", archivePatterns);
            ret.put("archiveIncludes", archiveIncludes);
        } else {
            ret.put("archiveIncludes", "**/.*jar,**/*.war,**/*.ear,**/*.sca,**/*.gem,**/*.whl,**/*.egg,**/*.tar,**/*.tar.gz,**/*.tgz,**/*.zip,**/*.rar");
        }

        ret.put("archiveExtractionDepth", "4");

        if(installBeforeScan) {
            ret.put("npm.runPreStep", "true");
            ret.put("bower.runPreStep", "true");
        }

        ret.put("d", scanFolder);

        return ret;
    }

    public static void printOSAResultsToConsole(OSAResults osaResults, Logger log) {
        OSASummaryResults osaSummaryResults = osaResults.getResults();
        log.info("----------------------------Checkmarx Scan Results(CxOSA):-------------------------------");
        log.info("");
        log.info("------------------------");
        log.info("Vulnerabilities Summary:");
        log.info("------------------------");
        log.info("OSA high severity results: " + osaSummaryResults.getTotalHighVulnerabilities());
        log.info("OSA medium severity results: " + osaSummaryResults.getTotalMediumVulnerabilities());
        log.info("OSA low severity results: " + osaSummaryResults.getTotalLowVulnerabilities());
        log.info("Vulnerability score: " + osaSummaryResults.getVulnerabilityScore());
        log.info("");
        log.info("-----------------------");
        log.info("Libraries Scan Results:");
        log.info("-----------------------");
        log.info("Open-source libraries: " + osaSummaryResults.getTotalLibraries());
        log.info("Vulnerable and outdated: " + osaSummaryResults.getVulnerableAndOutdated());
        log.info("Vulnerable and updated: " + osaSummaryResults.getVulnerableAndUpdated());
        log.info("Non-vulnerable libraries: " + osaSummaryResults.getNonVulnerableLibraries());
        log.info("");
        log.info("OSA scan results location: " + osaResults.getOsaProjectSummaryLink());
        log.info("-----------------------------------------------------------------------------------------");
    }



}
