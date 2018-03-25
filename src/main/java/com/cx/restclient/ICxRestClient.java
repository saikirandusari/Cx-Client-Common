package com.cx.restclient;

import com.cx.restclient.httpClient.exception.CxClientException;
import com.cx.restclient.httpClient.exception.CxTokenExpiredException;
import com.cx.restclient.osa.CxOSAClient;
import com.cx.restclient.osa.dto.OSAResults;
import com.cx.restclient.sast.CxSASTClient;
import com.cx.restclient.sast.dto.CxNameObj;
import com.cx.restclient.sast.dto.Query;
import com.cx.restclient.sast.dto.SASTResults;
import com.cx.restclient.dto.Team;

import java.io.IOException;
import java.util.List;

/**
 * Created by Galn on 11/03/2018.
 */
public interface ICxRestClient {

    void login() throws IOException, CxClientException, CxTokenExpiredException;

    List<Team> getTeamList() throws IOException, CxClientException, CxTokenExpiredException;

    List<Query> getPresetList() throws IOException, CxClientException, CxTokenExpiredException;

    List<CxNameObj> GetConfigurationSetList() throws IOException, CxClientException, CxTokenExpiredException;

    CxSASTClient getSASTClient() throws IOException, CxClientException, CxTokenExpiredException;

    CxOSAClient getOSAClient() throws IOException, CxClientException, CxTokenExpiredException;

    String generateHTMLSummary(SASTResults sastResults, OSAResults osaResults);

    void close();
}
