package cx.restclient;

import com.fasterxml.jackson.databind.type.TypeFactory;
import cx.restclient.dto.CxScanConfiguration;
import cx.restclient.httpClient.CxHttpClient;
import cx.restclient.httpClient.exception.CxClientException;
import cx.restclient.osa.CxOSAClient;
import cx.restclient.sast.CxSASTClient;

import cx.restclient.sast.dto.EngineConfig;
import cx.restclient.sast.dto.Query;
import cx.restclient.sast.dto.Team;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.List;

import static cx.restclient.common.CxPARAM.CXPRESETS;
import static cx.restclient.common.CxPARAM.CXTEAMS;
import static cx.restclient.sast.utils.CxSASTParam.CONTENT_TYPE_APPLICATION_JSON_V1;
import static cx.restclient.sast.utils.CxSASTParam.SAST_ENGINE_CONFIG;

/**
 * Created by Galn on 05/02/2018.
 */
public class CxRestClient {
    private CxHttpClient httpClient;
    private Logger log;
    private CxScanConfiguration config;

    CxRestClient(CxScanConfiguration config, Logger log) {

        this.config = config;
        this.log = log;
        httpClient = new CxHttpClient(config.getUrl(), config.getUsername(), config.getPassword(), config.getCxOrigin());
    }

    public CxSASTClient getSASTClient() {
        return new CxSASTClient(httpClient, log, config);
    }

    public CxOSAClient getOSAClient() {
        return new CxOSAClient(httpClient, log, config);
    }

    public void close() {
        httpClient.close();
    }

    public void login(){
  //      checkServerConnectivity();
        //perform login to server
        log.info("Logging into the Checkmarx service.");
        //loginToServer();

    }

    private List<Team> getTeamList() throws IOException, CxClientException {
        return httpClient.getRequest(CXTEAMS, CONTENT_TYPE_APPLICATION_JSON_V1,  TypeFactory.defaultInstance().constructCollectionType(List.class, Team.class), 200, " Preset list");
    }

    //Config Public Methods

    public List<Query> getPresetList() throws IOException, CxClientException {
        return httpClient.getRequest(CXPRESETS, CONTENT_TYPE_APPLICATION_JSON_V1, TypeFactory.defaultInstance().constructCollectionType(List.class, Query.class), 200, " Preset list");
    }


    public List<EngineConfig> GetConfigurationSetList() throws IOException, CxClientException {
        return httpClient.getRequest(SAST_ENGINE_CONFIG, CONTENT_TYPE_APPLICATION_JSON_V1, TypeFactory.defaultInstance().constructCollectionType(List.class, EngineConfig.class), 200, " EngineConfiguration");
    }

}

