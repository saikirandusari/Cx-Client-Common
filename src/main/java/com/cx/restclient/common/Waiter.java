package com.cx.restclient.common;

import com.cx.restclient.dto.BaseStatus;
import com.cx.restclient.dto.Status;
import com.cx.restclient.exception.CxClientException;
import com.cx.restclient.exception.CxTokenExpiredException;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.Date;

/**
 * Created by Galn on 13/02/2018.
 */
public abstract class Waiter<T> {

    private int retry = 5;
    private String scanType;
    private int sleepIntervalSec;

    public Waiter(String scanType, int interval) {
        this.scanType = scanType;
        this.sleepIntervalSec = interval;
    }

    private long startTimeSec; //In miliSeconds!!!!!!!!!!! Or minutes!!! or not!!

    protected Status status = null;

    public T waitForTaskToFinish(String taskId, Integer scanTimeoutSec, Logger log) throws CxClientException, InterruptedException {
        startTimeSec = System.currentTimeMillis() / 1000;
        long elapsedTimeSec = 0L;
        status = Status.IN_PROGRESS;
        T obj = null;

        while (status.equals(Status.IN_PROGRESS) && (scanTimeoutSec <= 0 || elapsedTimeSec < scanTimeoutSec)) {
            Thread.sleep(sleepIntervalSec * 1000);
            try {
                obj = getStatus(taskId);
                status = ((BaseStatus) obj).getBaseStatus();
            } catch (Exception e) {
                log.debug("Failed to get status from " + scanType + ". retrying (" + (retry - 1) + " tries left). Error message: " + e.getMessage());
                if (retry < 0) { //TODO <= maybe
                    throw new CxClientException("Failed to get status from " + scanType + ". Error message: " + e.getMessage());
                }
                retry--;
            }
            elapsedTimeSec = (new Date()).getTime() / 1000 - startTimeSec;
            if (obj != null) { //TODO // FIXME: 21/03/2018
                printProgress(obj);
            }
        }
        if (scanTimeoutSec > elapsedTimeSec && !status.equals(Status.SUCCEEDED)) {
            throw new CxClientException("Scan has reached the time limit. (" + scanTimeoutSec / 60 + " minutes).");
        } //TODO Timeout QA
        return resolveStatus(obj);

    }

    public abstract T getStatus(String id) throws CxClientException, IOException, CxTokenExpiredException;

    public abstract void printProgress(T status);

    public abstract T resolveStatus(T status) throws CxClientException;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public long getStartTimeSec() {
        return startTimeSec;
    }
}
