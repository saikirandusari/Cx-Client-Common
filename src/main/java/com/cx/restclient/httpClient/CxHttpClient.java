package com.cx.restclient.httpClient;

import com.cx.restclient.dto.LoginRequest;
import com.cx.restclient.httpClient.exception.CxClientException;
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

import static com.cx.restclient.common.CxPARAM.*;
import static com.cx.restclient.httpClient.utils.ClientUtils.*;
import static com.cx.restclient.httpClient.utils.PARAM.CONTENT_TYPE_APPLICATION_JSON;
import static com.cx.restclient.httpClient.utils.PARAM.CONTENT_TYPE_APPLICATION_JSON_V1;

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
            httpRequest.addHeader(ORIGIN_HEADER, cxOrigin);

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

        StringEntity requestEntity = new StringEntity(convertToJson(new LoginRequest(username, password)));
        postRequest(AUTHENTICATION, CONTENT_TYPE_APPLICATION_JSON_V1, requestEntity, null, 200, "authenticate");
    }

    //GET REQUEST

    public <T> T getRequest(String relPath, String contentType, Class<T> responseType, Integer expectStatus, String failedMsg, boolean isCollection) throws IOException, CxClientException {
        String resolvedPath = rootPath + relPath;
        HttpGet getRequest = new HttpGet(resolvedPath);
        getRequest.setHeader("Accept", CONTENT_TYPE_APPLICATION_JSON);
        getRequest.addHeader("Content-type", contentType);
        HttpResponse response = null; //TODO
        try {
            response = apacheClient.execute(getRequest);
            if (expectStatus != null) {
                validateResponse(response, expectStatus, "Failed to get " + failedMsg);
            } else if (response.getStatusLine().getStatusCode() == 404) {
                return null;
            }

            return convertToObject(response, responseType, isCollection);

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
        if (contentType != null) {
            post.addHeader("Content-type", contentType);
        }
        post.addHeader("Accept", CONTENT_TYPE_APPLICATION_JSON);
        HttpResponse response = null;

        try {
            //send scan request
            response = apacheClient.execute(post);
            //verify scan request
            validateResponse(response, expectStatus, "Failed to " + failedMsg);
            //extract response as object and return the link
            return convertToObject(response, responseType, false);
        } finally {
            post.releaseConnection();
            HttpClientUtils.closeQuietly(response);
        }
    }

    //PATCH REQUEST
    public void patchRequest(String relPath, String contentType, HttpEntity entity, int expectStatus, String failedMsg) throws CxClientException, IOException {
        String resolvedPath = rootPath + relPath;
        HttpPatch patch = new HttpPatch(resolvedPath);
        patch.setEntity(entity);
        patch.addHeader("Content-type", contentType);
        HttpResponse response = null;

        try {
            response = apacheClient.execute(patch);
            validateResponse(response, expectStatus, "Failed to " + failedMsg);
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
