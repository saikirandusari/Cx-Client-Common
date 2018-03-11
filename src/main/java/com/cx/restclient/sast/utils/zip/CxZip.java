package com.cx.restclient.sast.utils.zip;


import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.slf4j.Logger;


public class CxZip {
    private long maxZipSizeInBytes = 209715200;
    private int numOfZippedFiles = 0;

    private String tempFileName;
    private Logger log;

    public CxZip(String tempFileName, long maxZipSizeInBytes, Logger log) {
        this.tempFileName = tempFileName;
        this.log = log;
        this.maxZipSizeInBytes = maxZipSizeInBytes;
    }

    public File zipWorkspaceFolder(File baseDir, File tempDir, String filterPattern)
            throws InterruptedException, IOException {
        log.info("Zipping workspace: '" + baseDir + "'");

        ZipListener zipListener = new ZipListener() {
            public void updateProgress(String fileName, long size) {
                numOfZippedFiles++;
                log.info("Zipping (" + FileUtils.byteCountToDisplaySize(size) + "): " + fileName);
            }
        };

        File tempFile = File.createTempFile(tempFileName, ".bin", tempDir);
        OutputStream fileOutputStream = new FileOutputStream(tempFile);

        try {
            new Zipper(log).zip(baseDir, filterPattern, fileOutputStream, maxZipSizeInBytes, zipListener);
        } catch (Zipper.MaxZipSizeReached e) {
            tempFile.delete();
            throw new IOException("Reached maximum upload size limit of " + FileUtils.byteCountToDisplaySize(maxZipSizeInBytes));
        } catch (Zipper.NoFilesToZip e) {
            throw new IOException("No files to zip");
        }

        log.info("Zipping complete with " + numOfZippedFiles + " files, total compressed size: " +
                FileUtils.byteCountToDisplaySize(tempFile.length()));
        log.info("Temporary file with zipped sources was created at: '" + tempFile.getAbsolutePath() + "'");

        return tempFile;
    }

    public CxZip setMaxZipSizeInBytes(long maxZipSizeInBytes) {
        this.maxZipSizeInBytes = maxZipSizeInBytes;
        return this;
    }

    public CxZip setTempFileName(String tempFileName) {
        this.tempFileName = tempFileName;
        return this;
    }

    public String getTempFileName() {
        return tempFileName;
    }

}
