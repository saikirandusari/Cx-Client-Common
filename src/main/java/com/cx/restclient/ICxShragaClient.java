package com.cx.restclient;

import com.cx.restclient.dto.Team;
import com.cx.restclient.httpClient.exception.CxClientException;
import com.cx.restclient.httpClient.exception.CxTokenExpiredException;
import com.cx.restclient.osa.CxOSAClient;
import com.cx.restclient.osa.dto.OSAResults;
import com.cx.restclient.sast.CxSASTClient;
import com.cx.restclient.sast.dto.CxNameObj;
import com.cx.restclient.sast.dto.Preset;
import com.cx.restclient.sast.dto.SASTResults;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Galn on 11/03/2018.
 */
public interface ICxShragaClient {

    void init() throws CxClientException, IOException, CxTokenExpiredException;



    List<Team> getTeamList() throws IOException, CxClientException, CxTokenExpiredException;

    List<Preset> getPresetList() throws IOException, CxClientException, CxTokenExpiredException;

    List<CxNameObj> getConfigurationSetList() throws IOException, CxClientException, CxTokenExpiredException;

    File zipWorkspace() throws IOException, InterruptedException;



    CxSASTClient getSASTClient() throws IOException, CxClientException, CxTokenExpiredException;

    CxOSAClient getOSAClient() throws IOException, CxClientException, CxTokenExpiredException;

    String generateHTMLSummary(SASTResults sastResults, OSAResults osaResults);

    void close();

    void login() throws IOException, CxClientException, CxTokenExpiredException;
}
