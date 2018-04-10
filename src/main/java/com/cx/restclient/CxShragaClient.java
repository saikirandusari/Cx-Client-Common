package com.cx.restclient;

import com.cx.restclient.common.summary.SummaryUtils;
import com.cx.restclient.configuration.CxScanConfig;
import com.cx.restclient.dto.Team;
import com.cx.restclient.dto.ThresholdResult;
import com.cx.restclient.exception.CxClientException;
import com.cx.restclient.exception.CxSASTException;
import com.cx.restclient.exception.CxTokenExpiredException;
import com.cx.restclient.httpClient.CxHttpClient;
import com.cx.restclient.osa.dto.OSAResults;
import com.cx.restclient.sast.dto.*;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import static com.cx.restclient.common.CxPARAM.*;
import static com.cx.restclient.httpClient.utils.HttpClientHelper.convertToJson;
import static com.cx.restclient.httpClient.utils.ContentType.CONTENT_TYPE_APPLICATION_JSON_V1;
import static com.cx.restclient.sast.utils.SASTParam.SAST_ENGINE_CONFIG;
import static com.cx.restclient.sast.utils.SASTParam.SAST_GET_PROJECT;

/**
 * Created by Galn on 05/02/2018.
 */
public class CxShragaClient /*implements ICxShragaClient*/ {
    private CxHttpClient httpClient;
    private Logger log;
    private CxScanConfig config;
    private Integer projectId;

    private CxSASTClient sastClient;
    private CxOSAClient osaClient;
    private SASTResults sastResults;
    private OSAResults osaResults;


    public CxShragaClient(CxScanConfig config, Logger log) throws URISyntaxException, MalformedURLException {
        this.config = config;
        this.log = log;
        this.httpClient = new CxHttpClient(new URL(config.getUrl()), config.getUsername(), config.getPassword(), config.getCxOrigin());
        sastClient = new CxSASTClient(httpClient, log, config);
        osaClient = new CxOSAClient(httpClient, log, config);
    }

    //API Scans methods
    public void init() throws CxClientException, IOException, CxTokenExpiredException {
        login();
        if (config.getSastEnabled()) {
            resolvePreset();
        }
        resolveTeam();
        resolveProject();
    }

    public long createSASTScan() throws CxSASTException, InterruptedException, IOException {
        long sastScanId = sastClient.createSASTScan(projectId);
        sastResults.setScanId(sastScanId);
        sastResults.setSastProjectLink(config.getUrl(), projectId);

        return sastScanId;
    }

    public String createOSAScan() throws IOException, InterruptedException, CxClientException, CxTokenExpiredException {
        String osaScanId = osaClient.createOSAScan(projectId);
        osaResults.setOsaScanId(osaScanId);
        osaResults.setOsaProjectSummaryLink(config.getUrl(), projectId);

        return osaScanId;
    }

    public void cancelSASTScan() throws IOException, CxClientException, CxTokenExpiredException {
        sastClient.cancelSASTScan(sastResults.getScanId());
    }

    public SASTResults getSASTResults() throws Exception {
        sastResults = sastClient.getSASTResults(sastResults.getScanId(), projectId);
        return sastResults;
    }

    public OSAResults getOSAResults() throws Exception {
        osaResults = osaClient.getOSAResults(osaResults.getOsaScanId());
        return osaResults;
    }

    public ThresholdResult getThresholdResult() {
        StringBuilder res = new StringBuilder("");
        boolean isFail = isThresholdExceeded(sastResults, osaResults, res, config);
        return new ThresholdResult(isFail, res.toString());
    }

    public String generateHTMLSummary(SASTResults sastResults, OSAResults osaResults) {
        if (config.getCxOrigin().equals("Jenkins")) {
            return SummaryUtils.generateJellySummary(sastResults, osaResults, log);
        }
        return SummaryUtils.generateSummary(sastResults, osaResults, config, log);
    }

    // public resolveBuild(){
    //  if (config.getSastEnabled()){
    //assert if expected exception is thrown  OR when vulnerabilities under threshold
    //   StringBuilder res = new StringBuilder("");
    //    if (configUtil.assertVulnerabilities(scanResults, osaSummaryResults, res, config) || sastWaitException != null || osaException != null) {
    //     printUtils.printBuildFailure(res, sastWaitException, osaException, loggerAdapter, log);
    //     return taskResultBuilder.failed().build();
    //  }

    // }
    // }
//
    public void close() {
        httpClient.close();
    }


    //HELP config  Methods
    public void login() throws IOException, CxClientException, CxTokenExpiredException {

        // perform login to server
        log.info("Logging into the Checkmarx service.");
        //loginToServer();
        httpClient.login();
    }

    public String getTeamIdByName(String teamName) throws CxClientException, IOException, CxTokenExpiredException {
        List<Team> allTeams = getTeamList();
        for (Team team : allTeams) {
            if ((team.getFullName()).equalsIgnoreCase(teamName)) { //TODO caseSenesitive- checkkk and REMOVE The WA "\"
                return team.getId();
            }
        }
        throw new CxClientException("Could not resolve team ID from teamName: " + teamName); //TODO
    }

    public int getPresetIdByName(String presetName) throws CxClientException, IOException, CxTokenExpiredException {
        List<Preset> allPresets = getPresetList();
        for (Preset preset : allPresets) {
            if (preset.getName().equalsIgnoreCase(presetName)) { //TODO caseSenesitive- checkkk
                return preset.getId();
            }
        }

        throw new CxClientException("Could not resolve preset ID from preset Name: " + presetName); //TODO
    }

    public List<Team> getTeamList() throws IOException, CxClientException, CxTokenExpiredException {
        return (List<Team>) httpClient.getRequest(CXTEAMS, CONTENT_TYPE_APPLICATION_JSON_V1, Team.class, 200, "team list", true);
    }

    public List<Preset> getPresetList() throws IOException, CxClientException, CxTokenExpiredException {
        return (List<Preset>) httpClient.getRequest(CXPRESETS, CONTENT_TYPE_APPLICATION_JSON_V1, Preset.class, 200, "preset list", true);
    }

    public List<CxNameObj> GetConfigurationSetList() throws IOException, CxClientException, CxTokenExpiredException {
        return (List<CxNameObj>) httpClient.getRequest(SAST_ENGINE_CONFIG, CONTENT_TYPE_APPLICATION_JSON_V1, CxNameObj.class, 200, "engine configurations", true);
    }

   /* public File zipItJenkins() throws IOException, InterruptedException {
        return CxZipUtils.zipWorkspaceFolder(config, MAX_ZIP_SIZE_BYTES, log);
    }*/

    //Private methods
    private void resolveTeam() throws CxClientException, IOException, CxTokenExpiredException {
        if (config.getTeamId() == null) {
            config.setTeamId(getTeamIdByName(config.getTeamPath()));
        }
    }

    private void resolvePreset() throws CxClientException, IOException, CxTokenExpiredException {
        if (config.getPresetId() == null) {
            config.setPresetId(getPresetIdByName(config.getPresetName()));
        }
    }

    private void resolveProject() throws IOException, CxClientException, CxTokenExpiredException {
        List<Project> projects = getProjectByName(config.getProjectName(), config.getTeamId());
        if (projects == null) { // Project is new
            if (config.getDenyProject()) {
                String errMsg = "Creation of the new project [" + config.getProjectName() + "] is not authorized. " +
                        "Please use an existing project. \nYou can enable the creation of new projects by disabling" + "" +
                        " the Deny new Checkmarx projects creation checkbox in the Checkmarx plugin global settings.\n";
                throw new CxClientException(errMsg);
            }
            //Create newProject
            CreateProjectRequest request = new CreateProjectRequest(config.getProjectName(), config.getTeamId(), config.getPublic());
            projectId = createNewProject(request).getId();

        } else {
            projectId = projects.get(0).getId();
        }
    }

    private List<Project> getProjectByName(String projectName, String teamId) throws IOException, CxClientException, CxTokenExpiredException {
        String projectNamePath = SAST_GET_PROJECT.replace("{name}", projectName).replace("{teamId}", teamId);
        return (List<Project>) httpClient.getRequest(projectNamePath, CONTENT_TYPE_APPLICATION_JSON_V1, Project.class, 200, "project by name: " + projectName, true);
    }

    private Project createNewProject(CreateProjectRequest request) throws CxClientException, IOException, CxTokenExpiredException {
        String json = convertToJson(request);
        StringEntity entity = new StringEntity(json);
        return httpClient.postRequest(CREATE_PROJECT, CONTENT_TYPE_APPLICATION_JSON_V1, entity, Project.class, 201, "create new project: " + request.getName());
    }

    public void updateSASTZipFile(File zipFile) {
        config.setZipFile(zipFile);
    } //TODO j need it??

    public void updateOSAJsonDependencies(String osaDependenciesJson) {
        config.setOsaDependenciesJson(osaDependenciesJson);
    } //TODO j need it?>
    // private List<Project> getAllProjects() throws IOException, CxClientException {
    // String projectNamePath = SAST_GET_PROJECT.replace("{name}", projectName).replace("{teamId}", teamId);
    //  return httpClient.getRequest(projectNamePath, CONTENT_TYPE_APPLICATION_JSON_V1, TypeFactory.defaultInstance().constructCollectionType(List.class,Project.class), 200, " Project by projectId and teamId");
    //  }

    //Util function
    private boolean isThresholdExceeded(SASTResults sastResults, OSAResults osaResults, StringBuilder res, CxScanConfig config) {

        boolean thresholdExceeded = false;
        if (config.isSASTThresholdEffectivelyEnabled() && sastResults != null) {
            thresholdExceeded = isSeverityExceeded(sastResults.getSastHighResults(), config.getSastHighThreshold(), res, "high", "CxSAST ");
            thresholdExceeded |= isSeverityExceeded(sastResults.getSastMediumResults(), config.getSastMediumThreshold(), res, "medium", "CxSAST ");
            thresholdExceeded |= isSeverityExceeded(sastResults.getSastLowResults(), config.getSastLowThreshold(), res, "low", "CxSAST ");
        }
        if (config.isOSAThresholdEffectivelyEnabled() && osaResults != null) {
            thresholdExceeded |= isSeverityExceeded(osaResults.getResults().getTotalHighVulnerabilities(), config.getOsaHighThreshold(), res, "high", "CxOSA ");
            thresholdExceeded |= isSeverityExceeded(osaResults.getResults().getTotalMediumVulnerabilities(), config.getOsaMediumThreshold(), res, "medium", "CxOSA ");
            thresholdExceeded |= isSeverityExceeded(osaResults.getResults().getTotalLowVulnerabilities(), config.getOsaLowThreshold(), res, "low", "CxOSA ");
        }
        return thresholdExceeded;
    }

    private boolean isSeverityExceeded(int result, Integer threshold, StringBuilder res, String severity, String severityType) {
        boolean fail = false;
        if (threshold != null && result > threshold) {
            res.append(severityType).append(severity).append(" severity results are above threshold. Results: ").append(result).append(". Threshold: ").append(threshold).append("\n");
            fail = true;
        }
        return fail;
    }
}

