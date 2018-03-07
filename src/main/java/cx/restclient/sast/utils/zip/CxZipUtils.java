
package cx.restclient.sast.utils.zip;


import cx.restclient.dto.CxScanConfiguration;
import cx.restclient.httpClient.exception.CxClientException;
import cx.restclient.sast.exception.CxSASTException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static cx.restclient.sast.utils.CxSASTParam.TEMP_FILE_NAME_TO_ZIP;



/**
 * CxZipUtils generates the patterns used for zipping the workspace folder
 */


public abstract class CxZipUtils {

    public static File zipWorkspaceFolder( CxScanConfiguration config, long maxZipBytes, Logger log) throws IOException, InterruptedException {
        final String combinedFilterPattern = generatePattern(config.getFolderExclusions(), config.getFilterPattern(), log);
        CxZip cxZip = new CxZip(TEMP_FILE_NAME_TO_ZIP, maxZipBytes, log);

        return cxZip.zipWorkspaceFolder(new File(config.getSourceDir()), new File(config.getTempDir()), combinedFilterPattern);

    }

/*  TODO  public static byte[] getBytesFromZippedSources(File zipTempFile, Logger log) throws CxClientException, CxSASTException {
        log.info("Converting zipped sources to byte array");
        byte[] zipFileByte;
        InputStream fileStream = null;
        try {
            fileStream = new FileInputStream(zipTempFile);
            zipFileByte = IOUtils.toByteArray(fileStream);
        } catch (Exception e) {
            throw new CxSASTException("Fail to set zipped file into project: " + e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(fileStream);
        }
        return zipFileByte;
    }*/

    private static String generatePattern(String folderExclusions, String filterPattern, Logger log) throws IOException, InterruptedException {

        String excludeFoldersPattern = processExcludeFolders(folderExclusions, log);

        if (StringUtils.isEmpty(filterPattern) && StringUtils.isEmpty(excludeFoldersPattern)) {
            return "";
        } else if (!StringUtils.isEmpty(filterPattern) && StringUtils.isEmpty(excludeFoldersPattern)) {
            return filterPattern;
        } else if (StringUtils.isEmpty(filterPattern) && !StringUtils.isEmpty(excludeFoldersPattern)) {
            return excludeFoldersPattern;
        } else {
            return filterPattern + "," + excludeFoldersPattern;
        }
    }


    private static String processExcludeFolders(String folderExclusions, Logger log) {
        if (StringUtils.isEmpty(folderExclusions)) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        String[] patterns = StringUtils.split(folderExclusions, ",\n");
        for (String p : patterns) {
            p = p.trim();
            if (p.length() > 0) {
                result.append("!**//*");
                result.append(p);
                result.append("*//**, ");
            }
        }
        log.info("Exclude folders converted to: '" + result.toString() + "'");
        return result.toString();
    }
}

