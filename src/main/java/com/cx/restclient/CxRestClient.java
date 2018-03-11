package com.cx.restclient;

import com.cx.restclient.httpClient.CxHttpClient;
import com.cx.restclient.common.summary.SummaryUtils;
import com.cx.restclient.dto.CxScanConfiguration;
import com.cx.restclient.httpClient.exception.CxClientException;
import com.cx.restclient.osa.CxOSAClient;
import com.cx.restclient.osa.dto.OSAResults;
import com.cx.restclient.sast.CxSASTClient;

import com.cx.restclient.sast.dto.*;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.List;

import static com.cx.restclient.common.CxPARAM.CXPRESETS;
import static com.cx.restclient.common.CxPARAM.CXTEAMS;
import static com.cx.restclient.sast.utils.CxSASTParam.CONTENT_TYPE_APPLICATION_JSON_V1;
import static com.cx.restclient.sast.utils.CxSASTParam.SAST_ENGINE_CONFIG;
import static com.cx.restclient.sast.utils.CxSASTParam.SAST_GET_PROJECT;
import static com.cx.restclient.sast.utils.PrintUtils.printBuildFailure;

/**
 * Created by Galn on 05/02/2018.
 */
public class CxRestClient {
    private CxHttpClient httpClient;
    private Logger log;
    private CxScanConfiguration config;

    public CxRestClient(CxScanConfiguration config, Logger log) {

        this.config = config;
        this.log = log;
        httpClient = new CxHttpClient(config.getUrl(), config.getUsername(), config.getPassword(), config.getCxOrigin());
    }

    public CxSASTClient getSASTClient() throws IOException, CxClientException {
        resolveProject();
        return new CxSASTClient(httpClient, log, config);
    }

    private void resolveProject() throws IOException, CxClientException {
        Project project = null;
        if (config.getProject() == null) {
            List<Project> projects = getProjectByName(config.getProjectName(), config.getTeamId());
            if (projects == null) { // Project is new
                if (config.isDenyProject()) {
                    {
                        denyProject();
                    }
                }
            } else {
                project = projects.get(0);
            }
            config.setProject(project);
        }
    }

    public CxOSAClient getOSAClient() throws IOException, CxClientException {
        resolveProject();


        return new CxOSAClient(httpClient, log, config);
    }

    public void close() {
        httpClient.close();
    }

    public void login() throws IOException, CxClientException {
        //      checkServerConnectivity();
        //perform login to server
        log.info("Logging into the Checkmarx service.");
        //loginToServer();
        httpClient.login();

    }

    public String generateHTMLSummary(SASTResults sastResults, OSAResults osaResults){
        if (config.getCxOrigin().equals("Jenkins")){
            return SummaryUtils.generateJellySummary(sastResults, osaResults);
        }
        return SummaryUtils.generateSummary(sastResults, osaResults);
    }

    private List<Project> getProjectByName(String projectName, String teamId) throws IOException, CxClientException {
        String projectNamePath = SAST_GET_PROJECT.replace("{name}", projectName).replace("{teamId}", teamId);
        return (List<Project>) httpClient.getRequest(projectNamePath, CONTENT_TYPE_APPLICATION_JSON_V1, Project.class, null, "", true);
    }

    // private List<Project> getAllProjects() throws IOException, CxClientException {
    // String projectNamePath = SAST_GET_PROJECT.replace("{name}", projectName).replace("{teamId}", teamId);
    //  return httpClient.getRequest(projectNamePath, CONTENT_TYPE_APPLICATION_JSON_V1, TypeFactory.defaultInstance().constructCollectionType(List.class,Project.class), 200, " Project by projectId and teamId");
    //  }

    private void denyProject() { //TODO
        StringBuilder str = new StringBuilder("Creation of the new project [" + config.getProjectName() + "] is not authorized. Please use an existing project.");
        str.append("\nYou can enable the creation of new projects by disabling the Deny new Checkmarx projects creation checkbox in the Checkmarx plugin global settings.\n");
        printBuildFailure(str, null, null, log);
        //return taskResultBuilder.failed().build();
        //return null;//TODO
        //throw exception
    }



    //Config Public Methods

    private List<Team> getTeamList() throws IOException, CxClientException {
        return (List<Team>) httpClient.getRequest(CXTEAMS, CONTENT_TYPE_APPLICATION_JSON_V1, Team.class, 200, " Preset list", true);
    }

    public List<Query> getPresetList() throws IOException, CxClientException {
        return (List<Query>) httpClient.getRequest(CXPRESETS, CONTENT_TYPE_APPLICATION_JSON_V1, Query.class, 200, " Preset list", true);
    }


    public List<CxNameObj> GetConfigurationSetList() throws IOException, CxClientException {
        return (List<CxNameObj>) httpClient.getRequest(SAST_ENGINE_CONFIG, CONTENT_TYPE_APPLICATION_JSON_V1, CxNameObj.class, 200, " EngineConfiguration", true);
    }

}

