package com.cx.restclient.common;

import com.cx.restclient.configuration.CxScanConfig;
import com.cx.restclient.osa.dto.OSAResults;
import com.cx.restclient.sast.dto.SASTResults;

/**
 * Created by: dorg.
 * Date: 4/12/2018.
 */
public abstract class ShragaUtils {
    //Util methods
    public static boolean isThresholdExceeded(SASTResults sastResults, OSAResults osaResults, StringBuilder res, CxScanConfig config) {
        boolean thresholdExceeded = false;
        if (config.isSASTThresholdEffectivelyEnabled() && sastResults != null) {
            thresholdExceeded = isSeverityExceeded(sastResults.getHigh(), config.getSastHighThreshold(), res, "high", "CxSAST ");
            thresholdExceeded |= isSeverityExceeded(sastResults.getMedium(), config.getSastMediumThreshold(), res, "medium", "CxSAST ");
            thresholdExceeded |= isSeverityExceeded(sastResults.getLow(), config.getSastLowThreshold(), res, "low", "CxSAST ");
        }
        if (config.isOSAThresholdEffectivelyEnabled() && osaResults != null) {
            thresholdExceeded |= isSeverityExceeded(osaResults.getResults().getTotalHighVulnerabilities(), config.getOsaHighThreshold(), res, "high", "CxOSA ");
            thresholdExceeded |= isSeverityExceeded(osaResults.getResults().getTotalMediumVulnerabilities(), config.getOsaMediumThreshold(), res, "medium", "CxOSA ");
            thresholdExceeded |= isSeverityExceeded(osaResults.getResults().getTotalLowVulnerabilities(), config.getOsaLowThreshold(), res, "low", "CxOSA ");
        }
        return thresholdExceeded;
    }

    private static boolean isSeverityExceeded(int result, Integer threshold, StringBuilder res, String severity, String severityType) {
        boolean fail = false;
        if (threshold != null && result > threshold) {
            res.append(severityType).append(severity).append(" severity results are above threshold. Results: ").append(result).append(". Threshold: ").append(threshold).append("\n");
            fail = true;
        }
        return fail;
    }

}
