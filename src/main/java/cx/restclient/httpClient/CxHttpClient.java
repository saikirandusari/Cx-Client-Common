package cx.restclient.httpClient;

import cx.restclient.dto.LoginRequest;
import cx.restclient.httpClient.exception.CxClientException;
import org.apache.http.*;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;

import java.io.IOException;

import static cx.restclient.common.CxPARAM.AUTHENTICATION;
import static cx.restclient.common.CxPARAM.CSRF_TOKEN_HEADER;
import static cx.restclient.httpClient.utils.ClientUtils.*;
import static cx.restclient.sast.utils.CxSASTParam.SAST_CREATE_SCAN;

/**
 * Created by Galn on 05/02/2018.
 */
public class CxHttpClient {

    private Logger log;
    private HttpClient apacheClient;
    private CookieStore cookieStore;
    private String cookies;
    private String csrfToken;
    private String rootPath = "{hostName}/CxRestAPI/";
    private final String username;
    private final String password;
    private String cxOrigin; //TODO good as defualt??


    private final HttpRequestInterceptor requestFilter = new HttpRequestInterceptor() {
        public void process(HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {
            if (csrfToken != null) {
                httpRequest.addHeader(CSRF_TOKEN_HEADER, csrfToken);
            }
            if (cookies != null) {
                httpRequest.addHeader("cookie", cookies);
            }
        }
    };

    private final HttpResponseInterceptor responseFilter = new HttpResponseInterceptor() {

        public void process(HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {

            for (org.apache.http.cookie.Cookie c : cookieStore.getCookies()) {
                if (CSRF_TOKEN_HEADER.equals(c.getName())) {
                    csrfToken = c.getValue();
                }
            }
            Header[] setCookies = httpResponse.getHeaders("Set-Cookie");

            StringBuilder sb = new StringBuilder();
            for (Header h : setCookies) {
                sb.append(h.getValue()).append(";");
            }

            cookies = (cookies == null ? "" : cookies) + sb.toString();

        }
    };


    public CxHttpClient(String hostname, String username, String password, String origin) {
        this.username = username;
        this.password = password;
        this.rootPath = rootPath.replace("{hostName}", hostname);
        this.cxOrigin = origin;
        //create httpclient
        cookieStore = new BasicCookieStore();
        apacheClient = HttpClientBuilder.create().addInterceptorFirst(requestFilter).addInterceptorLast(responseFilter).setDefaultCookieStore(cookieStore).build();
    }

    public void setLogger(Logger log) {
        this.log = log;
    }

    public void login() throws CxClientException, IOException {
        cookies = null;
        csrfToken = null;
        HttpResponse loginResponse = null;
        //create login request
        HttpPost loginPost = new HttpPost(rootPath + AUTHENTICATION);
        StringEntity requestEntity = new StringEntity(convertToJson(new LoginRequest(username, password)));
        loginPost.setEntity(requestEntity);
        loginPost.addHeader("Content-type", "application/json;v=1");
        try {
            //send login request
            loginResponse = apacheClient.execute(loginPost);

            //validate login response
            validateResponse(loginResponse, 200, "Failed to authenticate");
        } finally {
            loginPost.releaseConnection();
            HttpClientUtils.closeQuietly(loginResponse);
        }
    }

    //GET REQUEST
    public <T> T getRequest(String relPath, String contentType, Object responseType, int expectStatus, String failedMsg) throws IOException, CxClientException {
        String resolvedPath = rootPath + relPath;
        HttpGet getRequest = new HttpGet(resolvedPath);
        getRequest.addHeader("Content-type", contentType);
        HttpResponse response = null; //TODO
        try {
            response = apacheClient.execute(getRequest);
            validateResponse(response, expectStatus, "Failed to get " + failedMsg);
            return (T) convertToObject(response, responseType.getClass());
        } finally {
            getRequest.releaseConnection();
            HttpClientUtils.closeQuietly(response);
        }
    }

    public <T> T getRequestNoValid(String relPath, String contentType, Object responseType) throws IOException, CxClientException {
        String resolvedPath = rootPath + relPath;
        HttpGet getRequest = new HttpGet(resolvedPath);
        getRequest.addHeader("Content-type", contentType);
        HttpResponse response = null; //TODO
        try {
            response = apacheClient.execute(getRequest);
            return (T) convertToObject(response, responseType.getClass());
        } finally {
            getRequest.releaseConnection();
            HttpClientUtils.closeQuietly(response);
        }
    }

    //POST REQUEST
    public <T> T postRequest(String relPath, String contentType, HttpEntity entity, Class<T> responseType, int expectStatus, String failedMsg) throws CxClientException, IOException {
        String resolvedPath = rootPath + relPath;
        HttpPost post = new HttpPost(resolvedPath);
        post.setEntity(entity);
        post.addHeader("Content-type", contentType);
        if (resolvedPath.equals(SAST_CREATE_SCAN)) { //TODO If create scan- need to add roigion
            post.addHeader("CxOrigin", cxOrigin);
        }
        HttpResponse response = null;

        try {
            //send scan request
            response = apacheClient.execute(post);
            //verify scan request
            validateResponse(response, expectStatus, "Failed to " + failedMsg);
            //extract response as object and return the link
            return convertToObject(response, responseType);
        } finally {
            post.releaseConnection();
            HttpClientUtils.closeQuietly(response);
        }
    }

    //POST REQUEST
    public void patchRequest(String relPath, String contentType, HttpEntity entity, int expectStatus, String failedMsg) throws CxClientException, IOException {
        String resolvedPath = rootPath + relPath;
        HttpPatch patch = new HttpPatch(resolvedPath);
        patch.setEntity(entity);
        patch.addHeader("Content-type", contentType);
        HttpResponse response = null;

        try {
            response = apacheClient.execute(patch);
            if (response.getStatusLine().getStatusCode() != expectStatus) {
                validateResponse(response, expectStatus, "Failed to " + failedMsg);
            }

        } catch (Exception ex) {
            log.warn("Failed to " + failedMsg + ": " + ex.getMessage());
        } finally {
            patch.releaseConnection();
            HttpClientUtils.closeQuietly(response);
        }
    }

    public void close() {
        HttpClientUtils.closeQuietly(apacheClient);
    }
}
