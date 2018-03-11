package com.cx.restclient.common.summary;

import com.cx.restclient.osa.dto.OSAResults;
import com.cx.restclient.sast.dto.SASTResults;

/**
 * Created by Galn on 11/03/2018.
 */
public abstract class SummaryUtils {


    public static String generateJellySummary(SASTResults sastResults, OSAResults osaResults){
        String summaryStr = generateSummary(sastResults, osaResults);
        return convertToJelly(summaryStr);
    }

    public static String generateSummary(SASTResults sastResults, OSAResults osaResults){


        return "";
    }

    private static String convertToJelly(String summaryStr){
        return "";
    }
}
