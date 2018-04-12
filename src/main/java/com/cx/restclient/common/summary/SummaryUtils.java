package com.cx.restclient.common.summary;

import com.cx.restclient.configuration.CxScanConfig;
import com.cx.restclient.osa.dto.OSAResults;
import com.cx.restclient.osa.dto.OSASummaryResults;
import com.cx.restclient.sast.dto.SASTResults;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import static com.cx.restclient.common.CxPARAM.OPTION_FALSE;
import static com.cx.restclient.common.CxPARAM.OPTION_TRUE;
import static com.cx.restclient.common.ShragaUtils.isThresholdExceeded;
import static com.cx.restclient.common.summary.SummaryConst.*;


/**
 * Created by Galn on 11/03/2018.
 */
public abstract class SummaryUtils {

    //place holderim
    public static final String SAST_SUMMARY_PH = "<!--sast-summary-->";
    public static final String SAST_LINKS_PH = "<!--sast-links-->";
    public static final String SAST_CHART_PH = "<!--sast-chart-->";
    public static final String LEGEND_PH = "<!--legend-->";
    public static final String SAST_ERROR_PH = "<!--sast-error-->";
    public static final String SAST_FULL_PH = "<!--sast-full-->";


    public static final String SAST_HIGH_PH = "<!--sast-high-->";
    public static final String SAST_MEDIUM_PH = "<!--sast-medium-->";
    public static final String SAST_LOW_PH = "<!--sast-low-->";


    public static final String SAST_NEW_HIGH_PH = "<!--sast-new-high-->";
    public static final String SAST_NEW_MEDIUM_PH = "<!--sast-new-medium-->";
    public static final String SAST_NEW_LOW_PH = "<!--sast-new-low-->";


    public static final String OSA_SUMMARY_PH = "<!--osa-summary-->";
    public static final String OSA_LINKS_PH = "<!--osa-links-->";
    public static final String OSA_CHART_PH = "<!--osa-chart-->";
    public static final String OSA_ERROR_PH = "<!--osa-error-->";
    public static final String OSA_FULL_PH = "<!--osa-full-->";

    public static final String OSA_HIGH_PH = "<!--osa-high-->";
    public static final String OSA_MEDIUM_PH = "<!--osa-medium-->";
    public static final String OSA_LOW_PH = "<!--osa-low-->";
    public static final String OSA_OK_LIB_PH = "<!--ok-libraries-->";
    public static final String OSA_VUL_LIB_PH = "<!--vulnerable-libraries-->";


    /*public static void main(String[] args) throws IOException {

        SASTResults sastResults = new SASTResults();
        sastResults.setHighResults(3);
        sastResults.setMediumResults(2);
        sastResults.setLowResults(1);
        sastResults.setNewLowCount(1);
        sastResults.setSastResultsReady(true);
        OSAResults osaResults = new OSAResults();
        OSASummaryResults osaSummaryResults = new OSASummaryResults();
        osaSummaryResults.setTotalHighVulnerabilities(3);
        osaSummaryResults.setTotalMediumVulnerabilities(2);
        osaSummaryResults.setTotalLowVulnerabilities(7);
        osaResults.setResults(osaSummaryResults);
        osaResults.setOsaResultsReady(true);


        CxScanConfig config = new CxScanConfig();
        config.setSastEnabled(true);
        config.setOsaEnabled(true);
        config.setSastThresholdsEnabled(true);
        config.setSastHighThreshold(8);
        config.setSastMediumThreshold(2);
        config.setSastLowThreshold(1);
        config.setOsaThresholdsEnabled(true);
        config.setOsaHighThreshold(8);
        config.setOsaMediumThreshold(8);
        config.setOsaLowThreshold(8);
        generateSummaryPOC(sastResults, osaResults, config, null);
    }*/

    public static String generateSummaryPOC(SASTResults sastResults, OSAResults osaResults, CxScanConfig config) throws IOException {

        //if sast enabled
        //if sast successful
        //put sast summary
        //put sast full
        //if sast not successfull
        //put sast error tile

        //if osa enabled
        //if osa successful
        //put osa summary
        //put osa full
        //if osa not successfull
        //put osa error tile


        String summaryTemplate = getResultsTemplate("summary-template.html");
        String sastSummary = "";
        String sastLinks = "";
        String sastChart = "";
        String sastError = "";
        String sastFull = "";

        String osaSummary = "";
        String osaLinks = "";
        String osaChart = "";
        String osaError = "";
        String osaFull = "";


        if (sastResults != null && config.getSastEnabled()) {
            sastSummary = getResultsTemplate("sast/sast-summary.html");
            if (sastResults.isSastResultsReady()) {
                sastLinks = getResultsTemplate("sast/sast-links.html");
                sastChart = resolveSASTChart(sastResults, config);
                sastFull = getResultsTemplate("sast/sast-full.html");

            } else {
                sastError = getResultsTemplate("sast/sast-error.html");
            }
        }


        if (osaResults != null && config.getOsaEnabled()) {

            osaSummary = getResultsTemplate("osa/osa-summary.html");
            if (osaResults.isOsaResultsReady()) {
                osaLinks = getResultsTemplate("osa/osa-links.html");

                osaChart = resolveOSAChart(osaResults, config);

                osaFull = getResultsTemplate("osa/osa-full.html");

            } else {
                osaError = getResultsTemplate("osa/osa-error.html");
            }
        }

        String report = summaryTemplate.
                replace(SAST_SUMMARY_PH, sastSummary).
                replace(SAST_LINKS_PH, sastLinks).
                replace(SAST_CHART_PH, sastChart).
                replace(SAST_ERROR_PH, sastError).
                replace(SAST_FULL_PH, sastFull).


                replace(OSA_SUMMARY_PH, osaSummary).
                replace(OSA_LINKS_PH, osaLinks).
                replace(OSA_CHART_PH, osaChart).
                replace(OSA_ERROR_PH, osaError).
                replace(OSA_FULL_PH, osaFull);


        FileUtils.writeStringToFile(new File("C:\\cxdev\\source\\Cx-Client-Common\\src\\main\\resources\\com\\cx\\report.html"), report);


        return "";
    }


    private static String resolveSASTChart(SASTResults results, CxScanConfig config) {
        String sastChart = getResultsTemplate("sast/sast-chart.html");
        sastChart = resolveSASTChartBar(results, sastChart);
        if (results.hasNewResults()){
            String legend = getResultsTemplate("legend.html");
            sastChart = sastChart.replace(LEGEND_PH, legend);
        }
        sastChart = resolveSASTChartThreshold(results, config, sastChart);
        sastChart = sastChart.
                replace(SAST_HIGH_PH, String.valueOf(results.getHighResults())).
                replace(SAST_MEDIUM_PH, String.valueOf(results.getMediumResults())).
                replace(SAST_LOW_PH, String.valueOf(results.getLowResults()));

        return sastChart;
    }

    private static String resolveSASTChartBar(SASTResults results, String template) {
        float maxHeight = Math.max(results.getHighResults(), Math.max(results.getMediumResults(), results.getLowResults())) * 10f / 9f;
        template = resolveBarStyle(results.getHighResults(), results.getNewHighCount(), maxHeight, "high", template);
        template = resolveBarStyle(results.getMediumResults(), results.getNewMediumCount(), maxHeight, "medium", template);
        template = resolveBarStyle(results.getLowResults(), results.getNewLowCount(), maxHeight, "low", template);

        return template;
    }

    private static String resolveBarStyle(int count, int newCount, float maxHeight, String severity, String template) {
        int minimalVisibilityHeight = 5;
        int totalPx = 238;

        //High- total
        float totalHeightPx = count / maxHeight * totalPx;
        template = template.replace("bar-" + severity + "-height", "style=\"height: " + totalHeightPx + "px\"");

        if (newCount > 0) {
            //New
            float newHeightPx = ((float) newCount) / ((float) count) * totalHeightPx;
            //if new height is between 1 and 9 - give it a minimum height and if theres enough spce in total height
            if (isNewNeedChange(totalHeightPx, newHeightPx, minimalVisibilityHeight)) {
                newHeightPx = minimalVisibilityHeight;
            }
            template = template.replace(severity + "-new-scans-height", "style=\"height: " + newHeightPx + "px\"");

            //Recurrent
            float recurrentPx = totalHeightPx - newHeightPx;
            template = template.replace(severity + "-recurrent-scans-height", "style=\"height: " + recurrentPx + "px\"");

        }
        return template;
    }


    private static String resolveOSAChart(OSAResults results, CxScanConfig config) {
        String osaChart = getResultsTemplate("osa/osa-chart.html");
        osaChart = resolveOSAChartBar(results.getResults(), osaChart);
        osaChart = resolveOSAChartThreshold(results, config, osaChart);
        osaChart = osaChart.
                replace(OSA_HIGH_PH, String.valueOf(results.getResults().getTotalHighVulnerabilities())).
                replace(OSA_MEDIUM_PH, String.valueOf(results.getResults().getTotalMediumVulnerabilities())).
                replace(OSA_LOW_PH, String.valueOf(results.getResults().getTotalLowVulnerabilities())).
                replace(OSA_VUL_LIB_PH, String.valueOf(results.getResults().getVulnerableAndOutdated())).
                replace(OSA_OK_LIB_PH, String.valueOf((results.getResults().getNonVulnerableLibraries())));

        return osaChart;
    }


    private static String resolveOSAChartBar(OSASummaryResults results, String template) {
        float maxHeight = Math.max(results.getTotalHighVulnerabilities(), Math.max(results.getTotalMediumVulnerabilities(), results.getTotalLowVulnerabilities())) * 10f / 9f;
        template = resolveBarStyle(results.getTotalHighVulnerabilities(), 0, maxHeight, "high", template);
        template = resolveBarStyle(results.getTotalMediumVulnerabilities(), 0, maxHeight, "medium", template);
        template = resolveBarStyle(results.getTotalLowVulnerabilities(), 0, maxHeight, "low", template);

        return template;
    }

    private static String resolveOSAChartThreshold(OSAResults osaResults, CxScanConfig config, String template) {
        OSASummaryResults results = osaResults.getResults();
        if (config.isOSAThresholdEffectivelyEnabled()) {
            template = resolveBySeverity(results.getTotalHighVulnerabilities(), config.getOsaHighThreshold(), "high", template);
            template = resolveBySeverity(results.getTotalMediumVulnerabilities(), config.getOsaMediumThreshold(), "medium", template);
            template = resolveBySeverity(results.getTotalLowVulnerabilities(), config.getOsaLowThreshold(), "low", template);

            boolean isThresholdExceeded = isThresholdExceeded(null, osaResults, new StringBuilder(), config);
            String thresholdExceeded = "";
            if (isThresholdExceeded) {
                thresholdExceeded = getResultsTemplate("threshold-exceeded.html");

            } else {
                thresholdExceeded = getResultsTemplate("threshold-compliance.html");
            }
            template = template.replace("<!--threshold-message-->", thresholdExceeded);
        }
        return template;
    }


    private static boolean isNewNeedChange(float highTotalHeightPx, float highNewHeightPx, int minimalVisibilityHeight) {
        return highNewHeightPx > 0 && highNewHeightPx < minimalVisibilityHeight && highTotalHeightPx > minimalVisibilityHeight * 2;
    }


    private static String resolveSASTChartThreshold(SASTResults sastResults, CxScanConfig config, String template) {
        if (config.isSASTThresholdEffectivelyEnabled()) {
            template = resolveBySeverity(sastResults.getHighResults(), config.getSastHighThreshold(), "high", template);
            template = resolveBySeverity(sastResults.getMediumResults(), config.getSastMediumThreshold(), "medium", template);
            template = resolveBySeverity(sastResults.getLowResults(), config.getSastLowThreshold(), "low", template);

            boolean isThresholdExceeded = isThresholdExceeded(sastResults, null, new StringBuilder(), config);
       /* TODO newwww if (isThresholdForNewResultExceeded) {
            isThresholdExceeded = true;
        }*/
            String thresholdExceeded = "";
            if (isThresholdExceeded) {
                thresholdExceeded = getResultsTemplate("threshold-exceeded.html");

            } else {
                thresholdExceeded = getResultsTemplate("threshold-compliance.html");
            }
            template = template.replace("<!--threshold-message-->", thresholdExceeded);
        }
        return template;
    }

    private static String resolveBySeverity(int count, Integer threshold, String severity, String template) {
        if (threshold != null && count > threshold) {
            float thresholdHeight = ((float) threshold) * 100f / (float) count;
            String toolTip = getResultsTemplate("threshold-tooltip.html"). //todo remove log
                    replace("threshold-tooltip-style", "style=\"bottom:calc(" + thresholdHeight + "% - 1px)\"").
                    replace("<!--threshold-->", String.valueOf(threshold));
            template = template.replace("<!--tooltip-" + severity + "-->", toolTip);
        }
        return template;
    }


    private static String getResultsTemplate(String templateName) {
        String ret = null;
        InputStream resourceAsStream = SummaryUtils.class.getResourceAsStream("/com/cx/report/" + templateName);
        if (resourceAsStream != null) {
            try {
                ret = IOUtils.toString(resourceAsStream, Charset.defaultCharset().name());
            } catch (IOException e) {
             //   log.warn("Failed to get results template", e.getMessage());
            } finally {
                try {
                    resourceAsStream.close();
                } catch (IOException e) {
                   // log.warn("Failed to close streams", e.getMessage());
                }
            }
        }
        return ret;
    }


}

