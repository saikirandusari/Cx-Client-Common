package cx.restclient.osa.utils;

/**
 * Created by Galn on 06/02/2018.
 */
public class CxOSAParam {

    public static final String OSA_SCAN_PROJECT = "osa/scans";
    public static final String OSA_SCAN_STATUS = "osa/scans/{scanId}";
    public static final String OSA_SCAN_SUMMARY = "osa/reports";
    public static final String OSA_SCAN_LIBRARIES = "/osa/libraries";
    public static final String OSA_SCAN_VULNERABILITIES = "/osa/vulnerabilities";
    public static final String SCAN_ID_QUERY_PARAM = "?scanId=";
    public static final String ITEM_PER_PAGE_QUERY_PARAM = "&itemsPerPage=";
    public static final long MAX_ITEMS = 1000000;
    public static int waitForScanToFinishRetry = 5;
}
