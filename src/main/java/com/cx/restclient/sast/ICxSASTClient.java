package com.cx.restclient.sast;

import com.cx.restclient.httpClient.exception.CxClientException;
import com.cx.restclient.sast.dto.CxLinkObj;
import com.cx.restclient.sast.dto.SASTResults;
import com.cx.restclient.sast.exception.CxSASTException;

import java.io.IOException;

/**
 * Created by Galn on 05/02/2018.
 */
public interface ICxSASTClient {
    CxLinkObj createSASTScan() throws CxSASTException, IOException, InterruptedException;

    SASTResults getSASTResults(CxLinkObj createScanResponse);

    void cancelSASTScan(String scanId) throws IOException, CxClientException;
}
