package com.cx.restclient.sast.utils;

import org.slf4j.Logger;

import java.io.File;

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
}
