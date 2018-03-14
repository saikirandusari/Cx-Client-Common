package com.cx.restclient;

import com.cx.restclient.httpClient.exception.CxClientException;
import com.cx.restclient.osa.CxOSAClient;
import com.cx.restclient.osa.dto.OSAResults;
import com.cx.restclient.sast.CxSASTClient;
import com.cx.restclient.sast.dto.CxNameObj;
import com.cx.restclient.sast.dto.Query;
import com.cx.restclient.sast.dto.SASTResults;
import com.cx.restclient.sast.dto.Team;

import java.io.IOException;
import java.util.List;

/**
 * Created by Galn on 11/03/2018.
 */
public interface ICxRestClient {

    void login() throws IOException, CxClientException;

    List<Team> getTeamList() throws IOException, CxClientException;

    List<Query> getPresetList() throws IOException, CxClientException;

    List<CxNameObj> GetConfigurationSetList() throws IOException, CxClientException;

    CxSASTClient getSASTClient() throws IOException, CxClientException;

    CxOSAClient getOSAClient() throws IOException, CxClientException;

    String generateHTMLSummary(SASTResults sastResults, OSAResults osaResults);

    void close();
}
