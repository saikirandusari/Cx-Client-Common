package cx.restclient.sast.utils;

import com.checkmarx.components.zipper.ZipListener;
import com.checkmarx.components.zipper.Zipper;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.slf4j.Logger;

/**
 * CxZip encapsulates the workspace folder zipping
 */

public class CxZip {
    private long maxZipSizeInBytes = 209715200;
    private int numOfZippedFiles = 0;

    private String tempFileName;
    private Logger logger;

    public CxZip(String tempFileName, Logger logger) {
        this.tempFileName = tempFileName;
        this.logger = logger;
    }

    public File zipWorkspaceFolder(File baseDir, File tempDir, String filterPattern)
            throws InterruptedException, IOException {
        logger.info("Zipping workspace: '" + baseDir + "'");

        ZipListener zipListener = new ZipListener() {
                public void updateProgress(String fileName, long size) {
                    numOfZippedFiles++;
                    logger.info("Zipping (" + FileUtils.byteCountToDisplaySize(size) + "): " + fileName);
                }
            };

        File tempFile = File.createTempFile(tempFileName, ".bin", tempDir);
        OutputStream fileOutputStream = new FileOutputStream(tempFile);

        try {
            new Zipper().zip(baseDir, filterPattern, fileOutputStream, maxZipSizeInBytes, zipListener);
        } catch (Zipper.MaxZipSizeReached e) {
            tempFile.delete();
            throw new IOException("Reached maximum upload size limit of " + FileUtils.byteCountToDisplaySize(maxZipSizeInBytes));
        } catch (Zipper.NoFilesToZip e) {
            throw new IOException("No files to zip");
        }

        logger.info("Zipping complete with " + numOfZippedFiles + " files, total compressed size: " +
                FileUtils.byteCountToDisplaySize(tempFile.length()));
        logger.info("Temporary file with zipped sources was created at: '" + tempFile.getAbsolutePath() + "'");

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
