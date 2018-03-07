import cx.restclient.CxRestClient;
import cx.restclient.dto.CxScanConfiguration;
import cx.restclient.httpClient.exception.CxClientException;
import cx.restclient.osa.CxOSAClient;
import cx.restclient.osa.dto.CreateOSAScanResponse;
import cx.restclient.osa.dto.OSAResults;
import cx.restclient.osa.exception.CxOSAException;
import cx.restclient.sast.CxSASTClient;
import cx.restclient.sast.dto.CxLinkObj;
import cx.restclient.sast.dto.SASTResults;
import cx.restclient.sast.exception.CxSASTException;
import org.slf4j.Logger;
import org.slf4j.Marker;

import java.io.IOException;


/**
 * Created by Galn on 04/03/2018.
 */
public class testi {
    private static String DEFAULT_FILTER_PATTERNS = "!**/_cvs/**/*, !**/.svn/**/*,   !**/.hg/**/*,   !**/.git/**/*,  !**/.bzr/**/*, !**/bin/**/*," +
            "!**/obj/**/*,  !**/backup/**/*, !**/.idea/**/*, !**/*.DS_Store, !**/*.ipr,     !**/*.iws,   " +
            "!**/*.bak,     !**/*.tmp,       !**/*.aac,      !**/*.aif,      !**/*.iff,     !**/*.m3u,   !**/*.mid,   !**/*.mp3,  " +
            "!**/*.mpa,     !**/*.ra,        !**/*.wav,      !**/*.wma,      !**/*.3g2,     !**/*.3gp,   !**/*.asf,   !**/*.asx,  " +
            "!**/*.avi,     !**/*.flv,       !**/*.mov,      !**/*.mp4,      !**/*.mpg,     !**/*.rm,    !**/*.swf,   !**/*.vob,  " +
            "!**/*.wmv,     !**/*.bmp,       !**/*.gif,      !**/*.jpg,      !**/*.png,     !**/*.psd,   !**/*.tif,   !**/*.swf,  " +
            "!**/*.jar,     !**/*.zip,       !**/*.rar,      !**/*.exe,      !**/*.dll,     !**/*.pdb,   !**/*.7z,    !**/*.gz,   " +
            "!**/*.tar.gz,  !**/*.tar,       !**/*.gz,       !**/*.ahtm,     !**/*.ahtml,   !**/*.fhtml, !**/*.hdm,   " +
            "!**/*.hdml,    !**/*.hsql,      !**/*.ht,       !**/*.hta,      !**/*.htc,     !**/*.htd,   !**/*.war,   !**/*.ear,  " +
            "!**/*.htmls,   !**/*.ihtml,     !**/*.mht,      !**/*.mhtm,     !**/*.mhtml,   !**/*.ssi,   !**/*.stm,   " +
            "!**/*.stml,    !**/*.ttml,      !**/*.txn,      !**/*.xhtm,     !**/*.xhtml,   !**/*.class, !**/*.iml,   !Checkmarx/Reports/*.*";

    private static String DEFAULT_OSA_ARCHIVE_INCLUDE_PATTERNS = "*.zip, *.tgz, *.war, *.ear";


    public static void main(String[] args) throws IOException, CxClientException, CxSASTException, InterruptedException, CxOSAException {
        Logger logi = new Logger() {
            public String getName() {
                return "Bamboo";
            }

            public boolean isTraceEnabled() {
                return false;
            }

            public void trace(String s) {

            }

            public void trace(String s, Object o) {

            }

            public void trace(String s, Object o, Object o1) {

            }

            public void trace(String s, Object... objects) {

            }

            public void trace(String s, Throwable throwable) {

            }

            public boolean isTraceEnabled(Marker marker) {
                return false;
            }

            public void trace(Marker marker, String s) {

            }

            public void trace(Marker marker, String s, Object o) {

            }

            public void trace(Marker marker, String s, Object o, Object o1) {

            }

            public void trace(Marker marker, String s, Object... objects) {

            }

            public void trace(Marker marker, String s, Throwable throwable) {

            }

            public boolean isDebugEnabled() {
                return false;
            }

            public void debug(String s) {

            }

            public void debug(String s, Object o) {

            }

            public void debug(String s, Object o, Object o1) {

            }

            public void debug(String s, Object... objects) {

            }

            public void debug(String s, Throwable throwable) {

            }

            public boolean isDebugEnabled(Marker marker) {
                return false;
            }

            public void debug(Marker marker, String s) {

            }

            public void debug(Marker marker, String s, Object o) {

            }

            public void debug(Marker marker, String s, Object o, Object o1) {

            }

            public void debug(Marker marker, String s, Object... objects) {

            }

            public void debug(Marker marker, String s, Throwable throwable) {

            }

            public boolean isInfoEnabled() {
                return false;
            }

            public void info(String s) {

            }

            public void info(String s, Object o) {

            }

            public void info(String s, Object o, Object o1) {

            }

            public void info(String s, Object... objects) {

            }

            public void info(String s, Throwable throwable) {

            }

            public boolean isInfoEnabled(Marker marker) {
                return false;
            }

            public void info(Marker marker, String s) {

            }

            public void info(Marker marker, String s, Object o) {

            }

            public void info(Marker marker, String s, Object o, Object o1) {

            }

            public void info(Marker marker, String s, Object... objects) {

            }

            public void info(Marker marker, String s, Throwable throwable) {

            }

            public boolean isWarnEnabled() {
                return false;
            }

            public void warn(String s) {

            }

            public void warn(String s, Object o) {

            }

            public void warn(String s, Object... objects) {

            }

            public void warn(String s, Object o, Object o1) {

            }

            public void warn(String s, Throwable throwable) {

            }

            public boolean isWarnEnabled(Marker marker) {
                return false;
            }

            public void warn(Marker marker, String s) {

            }

            public void warn(Marker marker, String s, Object o) {

            }

            public void warn(Marker marker, String s, Object o, Object o1) {

            }

            public void warn(Marker marker, String s, Object... objects) {

            }

            public void warn(Marker marker, String s, Throwable throwable) {

            }

            public boolean isErrorEnabled() {
                return false;
            }

            public void error(String s) {

            }

            public void error(String s, Object o) {

            }

            public void error(String s, Object o, Object o1) {

            }

            public void error(String s, Object... objects) {

            }

            public void error(String s, Throwable throwable) {

            }

            public boolean isErrorEnabled(Marker marker) {
                return false;
            }

            public void error(Marker marker, String s) {

            }

            public void error(Marker marker, String s, Object o) {

            }

            public void error(Marker marker, String s, Object o, Object o1) {

            }

            public void error(Marker marker, String s, Object... objects) {

            }

            public void error(Marker marker, String s, Throwable throwable) {

            }
        };

        CxScanConfiguration config = setConfigi();


        CxRestClient client = new CxRestClient(config, logi);
        client.login();

        CxSASTClient sastClient = client.getSASTClient();
        CxOSAClient osaClient = client.getOSAClient();

        CxLinkObj createScanResponse = sastClient.createSASTScan();
        CreateOSAScanResponse createOSAScanResponse = osaClient.createOSAScan();

        SASTResults sastResults = sastClient.getSASTResults(createScanResponse);
        OSAResults osaResults = osaClient.getOSAResults(createOSAScanResponse.getScanId());

    }


    private static CxScanConfiguration setConfigi() {
        CxScanConfiguration config = new CxScanConfiguration();
        config.setSASTEnabled(true);
        config.setCxOrigin("Bamboo");
        config.setSourceDir("C:\\Users\\galm\\Desktop\\restiDir\\sourciDir");
        config.setTempDir("C:\\Users\\galm\\Desktop\\restiDir\\TempDir");
        config.setReportsDir("C:\\Users\\galm\\Desktop\\restiDir\\reportsDir");
        config.setUsername("admin@cx");
        config.setPassword("Cx123456!");
        config.setUrl("http://localhost");
        config.setProjectName("TESTPROJECT");
        config.setPresetName("Default");//TODO why name and id?
        config.setPresetId(7);
        config.setFullTeamPath("CxServer");//TODO why name and id?
        config.setTeamId("00000000-1111-1111-b111-989c9070eb11");
        config.setFolderExclusions("");
        config.setFilterPattern(DEFAULT_FILTER_PATTERNS);
        config.setScanTimeoutInMinutes(null);//todo check
        config.setScanComment("This is First test of the Common client");
        config.setIncremental(false);
        config.setSynchronous(true);
        config.setThresholdsEnabled(false);
        //config.setHighThreshold();
        //config.setMediumThreshold();
        //config.setLowThreshold();
        config.setGeneratePDFReport(true);
        config.setOsaEnabled(true);
        config.setOsaFilterPattern("");//TODO
        config.setOsaArchiveIncludePatterns(DEFAULT_OSA_ARCHIVE_INCLUDE_PATTERNS);
        config.setOsaInstallBeforeScan(false);
        config.setOsaThresholdsEnabled(false);
        //config.setOsaHighThreshold();
        //config.setOsaMediumThreshold();
        //config.setOsaLowThreshold();
        config.setDenyProject(false);
        config.setPublic(true);
        config.setForceScan(false);
        //config.setZipFile();
        config.setEngineConfigurationId(null);//TODO what should be the value?
        //config.setOsaDependenciesJson();

        return config;
    }

}
