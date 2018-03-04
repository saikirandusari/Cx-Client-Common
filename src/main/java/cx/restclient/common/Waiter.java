package cx.restclient.common;

import cx.restclient.httpClient.exception.CxClientException;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.Date;

/**
 * Created by Galn on 13/02/2018.
 */
public abstract class Waiter<T> {

    private int retry = 5;
    private String scanType;
    private int sleepInterval;

    public Waiter(String scanType, int interval) {
        this.scanType = scanType;
        this.sleepInterval = interval;
    }

    private long startTime;
    protected Status status = null;

    public T waitForScanToFinish(long id, long scanTimeoutInMin, Logger log) throws CxClientException, InterruptedException {
        startTime = System.currentTimeMillis();
        long elapsedTime = 0L;
        long timeout = scanTimeoutInMin * 60000;
        boolean exit = false;
        T  obj = null;
        while (elapsedTime < timeout) {

            Thread.sleep(sleepInterval);  //Get status every 20 sec
            while (!exit) {
                try {
                    obj = getStatus(id);
                    status = ((BaseStatus)obj).getStatus();
                    exit = true;
                } catch (Exception e) {
                    log.debug("Failed to get status from " + scanType + ". retrying (" + (retry - 1) + " tries left). Error message: " + e.getMessage());
                    if (retry < 0) { //TODO <= maybe
                        throw new CxClientException("Failed to get status from SAST scan. Error message: " + e.getMessage());
                    }
                    retry--;
                }
            }
            if ((!Status.IN_PROGRESS.equals(status))) {
                obj = resolveStatus(obj);
                return obj;
            }
            elapsedTime = (new Date()).getTime() - startTime;
            printProgress(obj);
        }
        //TODO timeout- check Osa and SAST
        return null;
    }

    public abstract T getStatus(long id) throws CxClientException, IOException;

    public abstract void printProgress(T status);

    public abstract T resolveStatus(T status) throws CxClientException;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public long getStartTime() {
        return startTime;
    }
}
