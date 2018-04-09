package com.cx.restclient.common;

import com.cx.restclient.configuration.CxScanConfig;
import com.cx.restclient.osa.dto.OSASummaryResults;
import com.cx.restclient.sast.dto.SASTResults;

/**
 * Created by Galn on 4/9/2018.
 */
public class Utils {
   /* public boolean assertVulnerabilities(SASTResults sastResults, OSASummaryResults osaSummaryResults, StringBuilder res, CxScanConfig config ) throws TaskException {

        boolean failByThreshold = false;
        if (config.isSASTThresholdEffectivelyEnabled() && sastResults != null) {
            failByThreshold = isFail(sastResults.getSastHighResults(), config.getSastHighThreshold(), res, "high", "CxSAST ");
            failByThreshold |= isFail(sastResults.getMediumSeverityResults(), config.getMediumThreshold(), res, "medium", "CxSAST ");
            failByThreshold |= isFail(sastResults.getLowSeverityResults(), config.getLowThreshold(), res, "low", "CxSAST ");
        }
        if (config.isOSAThresholdEffectivelyEnabled() && osaSummaryResults != null) {
            failByThreshold |= isFail(osaSummaryResults.getTotalHighVulnerabilities(), config.getOsaHighThreshold(), res, "high", "CxOSA ");
            failByThreshold |= isFail(osaSummaryResults.getTotalMediumVulnerabilities(), config.getOsaMediumThreshold(), res, "medium", "CxOSA ");
            failByThreshold |= isFail(osaSummaryResults.getTotalLowVulnerabilities(), config.getOsaLowThreshold(), res, "low", "CxOSA ");
        }
        return failByThreshold;
    }

    public boolean isFail(int result, Integer threshold, StringBuilder res, String severity, String severityType) {
        boolean fail = false;
        if (threshold != null && result > threshold) {
            res.append(severityType).append(severity).append(" severity results are above threshold. Results: ").append(result).append(". Threshold: ").append(threshold).append("\n");
            fail = true;
        }
        return fail;
    }*/
}
