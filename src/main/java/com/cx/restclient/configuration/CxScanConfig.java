package com.cx.restclient.configuration;


import java.io.File;
import java.util.Map;

/**
 * Created by galn on 21/12/2016.
 */
public class CxScanConfig {

    private Boolean sastEnabled;//TODO
    private Boolean osaEnabled;//TODO

    private String cxOrigin;
    private String sourceDir;
    private String reportsDir;
    private String username;
    private String password;
    private String url;
    private String projectName;
    private String teamPath;
    private String teamId;
    private Boolean denyProject = false;
    private Boolean isPublic = true;//TODO what are the default values
    private Boolean forceScan = false;
    private String presetName;
    private Integer presetId;
    private String sastFolderExclusions;
    private String sastFilterPattern;
    private Integer sastScanTimeoutInMinutes;
    private String scanComment;
    private Boolean isIncremental = false;
    private Boolean isSynchronous = false;
    private Boolean sastThresholdsEnabled = false;
    private Integer sastHighThreshold;
    private Integer sastMediumThreshold;
    private Integer sastLowThreshold;
    private Boolean generatePDFReport = false;
    private File zipFile;
    private Integer engineConfigurationId; //TODO what are the default values

    private String osaFilterPattern;
    private String osaArchiveIncludePatterns;
    private Boolean osaRunInstall = false;
    private Boolean osaThresholdsEnabled = false;
    private Integer osaHighThreshold;
    private Integer osaMediumThreshold;
    private Integer osaLowThreshold;
    private Map<String, String> osaFsaConfig; //for MAVEN
    private String osaDependenciesJson;

    public Boolean getSastEnabled() {
        return sastEnabled;
    }

    public void setSastEnabled(Boolean sastEnabled) {
        this.sastEnabled = sastEnabled;
    }

    public Boolean getOsaEnabled() {
        return osaEnabled;
    }

    public void setOsaEnabled(Boolean osaEnabled) {
        this.osaEnabled = osaEnabled;
    }

    public String getCxOrigin() {
        return cxOrigin;
    }

    public void setCxOrigin(String cxOrigin) {
        this.cxOrigin = cxOrigin;
    }

    public String getSourceDir() {
        return sourceDir;
    }

    public void setSourceDir(String sourceDir) {
        this.sourceDir = sourceDir;
    }

    public String getReportsDir() {
        return reportsDir;
    }

    public void setReportsDir(String reportsDir) {
        this.reportsDir = reportsDir;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getTeamPath() {
        return teamPath;
    }

    public void setTeamPath(String teamPath) {
        this.teamPath = teamPath;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public Boolean getDenyProject() {
        return denyProject;
    }

    public void setDenyProject(Boolean denyProject) {
        this.denyProject = denyProject;
    }

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    public Boolean getForceScan() {
        return forceScan;
    }

    public void setForceScan(Boolean forceScan) {
        this.forceScan = forceScan;
    }

    public String getPresetName() {
        return presetName;
    }

    public void setPresetName(String presetName) {
        this.presetName = presetName;
    }

    public Integer getPresetId() {
        return presetId;
    }

    public void setPresetId(Integer presetId) {
        this.presetId = presetId;
    }

    public String getSastFolderExclusions() {
        return sastFolderExclusions;
    }

    public void setSastFolderExclusions(String sastFolderExclusions) {
        this.sastFolderExclusions = sastFolderExclusions;
    }

    public String getSastFilterPattern() {
        return sastFilterPattern;
    }

    public void setSastFilterPattern(String sastFilterPattern) {
        this.sastFilterPattern = sastFilterPattern;
    }

    public Integer getSastScanTimeoutInMinutes() {
        return sastScanTimeoutInMinutes;
    }

    public void setSastScanTimeoutInMinutes(Integer sastScanTimeoutInMinutes) {
        this.sastScanTimeoutInMinutes = sastScanTimeoutInMinutes;
    }

    public String getScanComment() {
        return scanComment;
    }

    public void setScanComment(String scanComment) {
        this.scanComment = scanComment;
    }

    public Boolean getIncremental() {
        return isIncremental;
    }

    public void setIncremental(Boolean incremental) {
        isIncremental = incremental;
    }

    public Boolean getSynchronous() {
        return isSynchronous;
    }

    public void setSynchronous(Boolean synchronous) {
        isSynchronous = synchronous;
    }

    public Boolean getSastThresholdsEnabled() {
        return sastThresholdsEnabled;
    }

    public void setSastThresholdsEnabled(Boolean sastThresholdsEnabled) {
        this.sastThresholdsEnabled = sastThresholdsEnabled;
    }

    public Integer getSastHighThreshold() {
        return sastHighThreshold;
    }

    public void setSastHighThreshold(Integer sastHighThreshold) {
        this.sastHighThreshold = sastHighThreshold;
    }

    public Integer getSastMediumThreshold() {
        return sastMediumThreshold;
    }

    public void setSastMediumThreshold(Integer sastMediumThreshold) {
        this.sastMediumThreshold = sastMediumThreshold;
    }

    public Integer getSastLowThreshold() {
        return sastLowThreshold;
    }

    public void setSastLowThreshold(Integer sastLowThreshold) {
        this.sastLowThreshold = sastLowThreshold;
    }

    public Boolean getGeneratePDFReport() {
        return generatePDFReport;
    }

    public void setGeneratePDFReport(Boolean generatePDFReport) {
        this.generatePDFReport = generatePDFReport;
    }

    public File getZipFile() {
        return zipFile;
    }

    public void setZipFile(File zipFile) {
        this.zipFile = zipFile;
    }

    public Integer getEngineConfigurationId() {
        return engineConfigurationId;
    }

    public void setEngineConfigurationId(Integer engineConfigurationId) {
        this.engineConfigurationId = engineConfigurationId;
    }

    public String getOsaFilterPattern() {
        return osaFilterPattern;
    }

    public void setOsaFilterPattern(String osaFilterPattern) {
        this.osaFilterPattern = osaFilterPattern;
    }

    public String getOsaArchiveIncludePatterns() {
        return osaArchiveIncludePatterns;
    }

    public void setOsaArchiveIncludePatterns(String osaArchiveIncludePatterns) {
        this.osaArchiveIncludePatterns = osaArchiveIncludePatterns;
    }

    public Boolean getOsaRunInstall() {
        return osaRunInstall;
    }

    public void setOsaRunInstall(Boolean osaRunInstall) {
        this.osaRunInstall = osaRunInstall;
    }

    public Boolean getOsaThresholdsEnabled() {
        return osaThresholdsEnabled;
    }

    public void setOsaThresholdsEnabled(Boolean osaThresholdsEnabled) {
        this.osaThresholdsEnabled = osaThresholdsEnabled;
    }

    public Integer getOsaHighThreshold() {
        return osaHighThreshold;
    }

    public void setOsaHighThreshold(Integer osaHighThreshold) {
        this.osaHighThreshold = osaHighThreshold;
    }

    public Integer getOsaMediumThreshold() {
        return osaMediumThreshold;
    }

    public void setOsaMediumThreshold(Integer osaMediumThreshold) {
        this.osaMediumThreshold = osaMediumThreshold;
    }

    public Integer getOsaLowThreshold() {
        return osaLowThreshold;
    }

    public void setOsaLowThreshold(Integer osaLowThreshold) {
        this.osaLowThreshold = osaLowThreshold;
    }

    public Map<String, String> getOsaFsaConfig() {
        return osaFsaConfig;
    }

    public void setOsaFsaConfig(Map<String, String> osaFsaConfig) {
        this.osaFsaConfig = osaFsaConfig;
    }

    public String getOsaDependenciesJson() {
        return osaDependenciesJson;
    }

    public boolean isSASTThresholdEffectivelyEnabled() {
        return getSastEnabled() && getSastThresholdsEnabled()&& (getSastHighThreshold() != null || getSastMediumThreshold() != null || getSastLowThreshold() != null);
    }

    public boolean isOSAThresholdEffectivelyEnabled() {
        return getOsaEnabled() && getOsaThresholdsEnabled() && (getOsaHighThreshold() != null || getOsaMediumThreshold() != null || getOsaLowThreshold() != null);
    }    public void setOsaDependenciesJson(String osaDependenciesJson) {
        this.osaDependenciesJson = osaDependenciesJson;
    }


}
