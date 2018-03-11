package com.cx.restclient.osa;

import com.cx.restclient.httpClient.exception.CxClientException;
import com.cx.restclient.osa.dto.CreateOSAScanResponse;
import com.cx.restclient.osa.dto.OSAResults;
import com.cx.restclient.osa.exception.CxOSAException;

import java.io.IOException;

/**
 * Created by Galn on 07/02/2018.
 */
public interface ICxOSAClient {

    public CreateOSAScanResponse createOSAScan() throws IOException, InterruptedException, CxClientException;

    public OSAResults getOSAResults(String scanId) throws CxClientException, IOException, CxOSAException, InterruptedException;
}
