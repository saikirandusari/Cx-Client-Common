package com.cx.restclient.common;

import com.cx.restclient.httpClient.exception.CxClientException;
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

    public T waitForScanToFinish(String id, long scanTimeoutInMin, Logger log) throws CxClientException, InterruptedException {
        sleepInterval = 10;
        startTime = System.currentTimeMillis();
        long elapsedTime = 0L;
        long timeout = scanTimeoutInMin * 60000;
        status = Status.IN_PROGRESS;
        T obj = null;


            Thread.sleep(sleepInterval);  //Get status every 20 sec
            while (status.equals(Status.IN_PROGRESS) && (scanTimeoutInMin <= 0 || elapsedTime < timeout)){
                try {
                    obj = getStatus(id);
                    status = ((BaseStatus) obj).getBaseStatus();
                } catch (Exception e) {
                    log.debug("Failed to get status from " + scanType + ". retrying (" + (retry - 1) + " tries left). Error message: " + e.getMessage());
                    if (retry < 0) { //TODO <= maybe
                        throw new CxClientException("Failed to get status from SAST scan. Error message: " + e.getMessage());
                    }
                    retry--;
                }
                elapsedTime = (new Date()).getTime() - startTime;
                printProgress(obj);
            }

            return resolveStatus(obj);
        //TODO timeout- check Osa and SAST
    }

    public abstract T getStatus(String id) throws CxClientException, IOException;

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
