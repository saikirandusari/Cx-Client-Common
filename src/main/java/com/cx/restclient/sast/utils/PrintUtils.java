package com.cx.restclient.sast.utils;

import com.cx.restclient.sast.dto.SASTResults;
import org.slf4j.Logger;

/**
 * Created by Galn on 12/02/2018.
 */
public abstract class PrintUtils {
    public static void printResultsToConsole(SASTResults scanResults, Logger logger) {
        logger.info("----------------------------Checkmarx Scan Results(CxSAST):-------------------------------");
        logger.info("High severity results: " + scanResults.getSastHighResults());
        logger.info("Medium severity results: " + scanResults.getSastMediumResults());
        logger.info("Low severity results: " + scanResults.getSastLowResults());
        logger.info("Info severity results: " + scanResults.getSastInfoResults());
        logger.info("");
        logger.info("Scan results location: " + scanResults.getSastScanLink());
        logger.info("------------------------------------------------------------------------------------------\n");
    }

    public static void printBuildFailure(StringBuilder res, Exception sastBuildFailException, Exception osaBuildFailException, Logger log) { //TODO!!
        log.error("********************************************");
        log.error(" The Build Failed for the Following Reasons: ");
        log.error("********************************************");

        if (sastBuildFailException != null) {
            log.error(sastBuildFailException.getMessage() + (sastBuildFailException.getCause() == null ? "" : sastBuildFailException.getCause().getMessage()));
        }
        if (osaBuildFailException != null) {
            log.error(osaBuildFailException.getMessage() + (osaBuildFailException.getCause() == null ? "" : osaBuildFailException.getCause().getMessage()));
        }

        String[] lines = res.toString().split("\\n");
        for (String s : lines) {
            log.error(s);
            log.info(s);
        }
        log.error("-----------------------------------------------------------------------------------------\n");
        log.error("");
    }
}
