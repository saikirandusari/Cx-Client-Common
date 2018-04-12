
import com.cx.restclient.CxShragaClient;
import com.cx.restclient.configuration.CxScanConfig;
import com.cx.restclient.dto.ThresholdResult;
import com.cx.restclient.osa.dto.OSAResults;
import com.cx.restclient.sast.dto.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;


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


    public static void main(String[] args) throws Exception {
        SASTResults sastResults = null;
        OSAResults osaResults = null;
        Logger logi = LoggerFactory.getLogger("testush");

        CxScanConfig config = setConfigi();

        CxShragaClient shraga = new CxShragaClient(config, logi);
        shraga.init();

        // List<Team> teamList = client.getTeamList();
        //List<Query> presetList = client.getPresetList();
        //List<CxNameObj> configList = client.GetConfigurationSetList();

        shraga.createSASTScan();
        // shraga.cancelSASTScan(sastScanId);
        shraga.createOSAScan();


        //jenkins OSAUtils.generateOSAScanConfiguration(sasasa)
        //TODjenkins client.updateOSAJsonDependencies("fsafsfsfsf");


      //  sastResults = shraga.getSASTResults();
        sastResults = shraga.getLastSASTResults();
        osaResults = shraga.getOSAResults();

        ThresholdResult thresholdResult = shraga.getThresholdResult();
        shraga.generateHTMLSummary();
        //  shraga.generateHTMLSummary(sastResults, osaResults);
        shraga.close();

    }


    private static CxScanConfig setConfigi() {
        CxScanConfig config = new CxScanConfig();
        config.setSastEnabled(true);
        config.setCxOrigin("Bamboo");
        // config.setSourceDir("C:\\Users\\galn\\Desktop\\restiDir\\srcDir");
        config.setSourceDir("C:\\Users\\galn\\Desktop\\restiDir\\srcDir\\SAST\\Folder1\\Folder2\\Folder3");
        config.setReportsDir(new File("C:\\Users\\galm\\Desktop\\restiDir\\reportsDir"));
        config.setUsername("admin@cx");
        config.setPassword("Cx123456!");
        //config.setUrl("http://10.31.3.123");
        config.setUrl("http://10.31.0.152");
        //config.setUrl("http://galn-laptop");
        config.setProjectName("SanityTesmt842");
        config.setPresetName("Default");
        // config.setPresetId(7);
        config.setTeamPath("\\CxServer");
        //  config.setTeamId("00000000-1111-1111-b111-989c9070eb11");
        config.setSastFolderExclusions("");
        config.setSastFilterPattern(DEFAULT_FILTER_PATTERNS);
        config.setSastScanTimeoutInMinutes(null);//todo check
        config.setScanComment("This is First test of the Common client :) !!!!!!");
        config.setIncremental(false);
        config.setSynchronous(true);
        config.setSastThresholdsEnabled(true);
        config.setSastHighThreshold(1);
        config.setSastMediumThreshold(2);
        config.setSastLowThreshold(2);
        config.setGeneratePDFReport(true);
        config.setOsaEnabled(true);
        config.setOsaFilterPattern("");//TODO
        config.setOsaArchiveIncludePatterns(DEFAULT_OSA_ARCHIVE_INCLUDE_PATTERNS);
        config.setOsaRunInstall(false);
        config.setOsaThresholdsEnabled(true);
        config.setOsaHighThreshold(1);
        config.setOsaMediumThreshold(2);
        config.setOsaLowThreshold(7);
        config.setDenyProject(false);
        config.setPublic(true);
        //config.setZipFile();
        config.setEngineConfigurationId(null);//TODO what should be the value?
        //config.setOsaDependenciesJson();

        return config;
    }


    public static String generateHtml(SASTResults sastResults, OSAResults osaResults) throws ParserConfigurationException, IOException, SAXException {
        //-------------------------- sast vars --------------------------------------
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        InputSource is = new InputSource();


        String path = "C:\\cxdev\\CxPlugins\\Cx-Client-Common\\src\\main\\resources\\fullRpoertTemplate";
        // String fullHtml = FileUtils.readFileToString(new File(path));

        StringBuilder contentBuilder = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new FileReader(path));
            String str;
            while ((str = in.readLine()) != null) {
                contentBuilder.append(str);
            }
            in.close();
        } catch (IOException e) {
        }
        String fullHtml = contentBuilder.toString();
        is.setCharacterStream(new StringReader(fullHtml));
        Document report = db.parse(is);

        report.getElementById("sast-title-links");


        boolean isSastFullReady = (sastResults.getScanStart() != null && sastResults.getScanTime() != null
                && sastResults.getFilesScanned() != null && sastResults.getLinesOfCodeScanned() != null);


        String thresholdExceededStr = "<div class=\"threshold-exceeded\">" +
                "<div class=\"threshold-exceeded-icon\">" +
                "<svg xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" width=\"12px\" height=\"12px\" viewBox=\"0 0 12 12\" version=\"1.1\"><defs/><g id=\"Page-1\" stroke=\"none\" stroke-width=\"1\" fill=\"none\" fill-rule=\"evenodd\"><g id=\"Icons\" transform=\"translate(-52.000000, -241.000000)\"><g id=\"threshhold-icon\" transform=\"translate(52.000000, 241.000000)\"><g><path d=\"M8.0904685,3 L7.0904685,3 L7.0904685,5 L8.0904685,5 L8.0904685,11 L3.0904685,11 L3.0904685,0 L8.0904685,0 L8.0904685,3 Z M3.0904685,3 L3.0904685,5 L5.0904685,5 L5.0904685,3 L3.0904685,3 Z M5.0904685,3 L5.0904685,5 L7.0904685,5 L7.0904685,3 L5.0904685,3 Z\" id=\"Combined-Shape\" fill=\"#FFFFFF\"/><path d=\"M10.5904685,11.5 L0.590468498,11.5\" id=\"Line\" stroke=\"#FFFFFF\" stroke-linecap=\"square\"/></g></g></g></g></svg>" +
                "</div>" +
                "<div class=\"threshold-exceeded-text\">" +
                "Threshold Exceeded" +
                "</div>" +
                "</div>";

        is.setCharacterStream(new StringReader(thresholdExceededStr));
        Document thresholdExceededHtml = db.parse(is);


        String thresholdComplianceStr =
                "<div class=\"threshold-compliance\">" +
                        "<div class=\"threshold-compliance-icon\">" +
                        "<svg xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:svgjs=\"http://svgjs.com/svgjs\" id=\"SvgjsSvg1050\" version=\"1.1\" width=\"13.99264158479491\" height=\"13\" viewBox=\"0 0 13.99264158479491 13\"><title>Icon</title><desc>Created with Avocode.</desc><defs id=\"SvgjsDefs1051\"><clipPath id=\"SvgjsClipPath1056\"><path id=\"SvgjsPath1055\" d=\"M1035.00736 793.9841L1035.00736 784.01589L1046.9926400000002 784.01589L1046.9926400000002 793.9841ZM1038.67 790.72L1036.68 788.72L1036 789.4L1038.67 792.0699999999999L1045.21 785.67L1044.54 785Z \" fill=\"#ffffff\"/></clipPath></defs><path id=\"SvgjsPath1052\" d=\"M1033 789.5C1033 785.91015 1035.91015 783 1039.5 783C1043.08985 783 1046 785.91015 1046 789.5C1046 793.08985 1043.08985 796 1039.5 796C1035.91015 796 1033 793.08985 1033 789.5Z \" fill=\"#21bf3f\" fill-opacity=\"1\" transform=\"matrix(1,0,0,1,-1033,-783)\"/><path id=\"SvgjsPath1053\" d=\"M1038.67 790.72L1036.68 788.72L1036 789.4L1038.67 792.0699999999999L1045.21 785.67L1044.54 785Z \" fill=\"#ffffff\" fill-opacity=\"1\" transform=\"matrix(1,0,0,1,-1033,-783)\"/><path id=\"SvgjsPath1054\" d=\"M1038.67 790.72L1036.68 788.72L1036 789.4L1038.67 792.0699999999999L1045.21 785.67L1044.54 785Z \" fill-opacity=\"0\" fill=\"#ffffff\" stroke-dasharray=\"0\" stroke-linejoin=\"miter\" stroke-linecap=\"butt\" stroke-opacity=\"1\" stroke=\"#ffffff\" stroke-miterlimit=\"50\" stroke-width=\"1.4\" clip-path=\"url(&quot;#SvgjsClipPath1056&quot;)\" transform=\"matrix(1,0,0,1,-1033,-783)\"/></svg>" +
                        "</div>" +
                        "<div class=\"threshold-compliance-text\">" +
                        "Threshold Compliant" +
                        "</div>" +
                        "</div>";
        is.setCharacterStream(new StringReader(thresholdComplianceStr));
        Document thresholdComplianceHtml = db.parse(is);

        String sastErrorMsg =
                "<div class=\"sast-error-container\">" +
                        "<svg xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" width=\"48px\" height=\"37px\" viewBox=\"0 0 48 37\" version=\"1.1\"> <!-- Generator: Sketch 45.2 (43514) - http:/*www.bohemiancoding.com/sketch -->*/ <title>scan_failed</title> <desc>Created with Sketch.</desc> <defs/> <g id=\"Page-1\" stroke=\"none\" stroke-width=\"1\" fill=\"none\" fill-rule=\"evenodd\"> <g id=\"Jenkins-OSA-empty\" transform=\"translate(-936.000000, -335.000000)\"> <g id=\"Group-2\" transform=\"translate(683.000000, 179.000000)\"> <g id=\"Group\" transform=\"translate(81.000000, 155.202941)\"> <g id=\"Group-3\" transform=\"translate(171.000000, 0.000000)\"> <g transform=\"translate(2.000606, 1.714748)\"> <path d=\"M24.3114123,0.619831205 L44.98533,32.7440758 L44.98533,32.7440758 C45.2842135,33.208497 45.1500186,33.8272778 44.6855974,34.1261613 C44.5242092,34.2300245 44.336342,34.2852522 44.1444209,34.2852522 L1.8977196,34.2852522 L1.8977196,34.2852522 C1.34543485,34.2852522 0.897719599,33.837537 0.897719599,33.2852522 C0.897719599,33.0866799 0.956838199,32.8926033 1.0675421,32.7277531 L22.6403257,0.60350849 L22.6403257,0.60350849 C22.948224,0.145014117 23.569508,0.0229318695 24.0280024,0.330830157 C24.1411852,0.406837189 24.2376306,0.50518529 24.3114123,0.619831205 Z\" id=\"Triangle\" stroke=\"#373050\" stroke-width=\"2\"/> <rect id=\"Rectangle-6\" fill=\"#373050\" x=\"21.9993936\" y=\"11.2852522\" width=\"3\" height=\"13\" rx=\"1.5\"/> <path d=\"M23.4993936,26.2852522 L23.4993936,26.2852522 L23.4993936,26.2852522 C24.3278207,26.2852522 24.9993936,26.9568251 24.9993936,27.7852522 L24.9993936,27.7852522 L24.9993936,27.7852522 C24.9993936,28.6136794 24.3278207,29.2852522 23.4993936,29.2852522 L23.4993936,29.2852522 L23.4993936,29.2852522 C22.6709664,29.2852522 21.9993936,28.6136794 21.9993936,27.7852522 L21.9993936,27.7852522 L21.9993936,27.7852522 C21.9993936,26.9568251 22.6709664,26.2852522 23.4993936,26.2852522 Z\" id=\"Rectangle-6-Copy\" fill=\"#373050\"/> </g> </g> </g> </g> </g> </g> </svg>" +
                        "<div class=\"sast-error-text\">Scan failed</div>" +
                        "</div>";

        is.setCharacterStream(new StringReader(sastErrorMsg));
        Document sastErrorMsgHtml = db.parse(is);

        return "";

    }
}
