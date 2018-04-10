package com.cx.restclient.httpClient;

import com.cx.restclient.dto.TokenLoginResponse;
import com.cx.restclient.exception.CxClientException;
import com.cx.restclient.exception.CxTokenExpiredException;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.cx.restclient.common.CxPARAM.AUTHENTICATION;
import static com.cx.restclient.common.CxPARAM.ORIGIN_HEADER;
import static com.cx.restclient.httpClient.utils.ContentType.CONTENT_TYPE_APPLICATION_JSON;
import static com.cx.restclient.httpClient.utils.HttpClientHelper.*;

/**
 * Created by Galn on 05/02/2018.
 */
public class CxHttpClient {

    private Logger log;
    private HttpClient apacheClient;
    private TokenLoginResponse token;
    private String basePathParam = "CxRestAPI";
    private String rootPath;
    private final String username;
    private final String password;
    private String cxOrigin;


    private final HttpRequestInterceptor requestFilter = new HttpRequestInterceptor() {
        public void process(HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {
            httpRequest.addHeader(ORIGIN_HEADER, cxOrigin);
            httpRequest.addHeader(HttpHeaders.ACCEPT, CONTENT_TYPE_APPLICATION_JSON);
            if (token != null) {
                httpRequest.addHeader(HttpHeaders.AUTHORIZATION, token.getToken_type() + " " + token.getAccess_token());
            }
        }
    };


    public CxHttpClient(URL hostname, String username, String password, String origin) throws URISyntaxException, MalformedURLException {
        this.username = username;
        this.password = password;
        this.rootPath = new URL(hostname, basePathParam).toString();
        this.cxOrigin = origin;
        //create httpclient
        apacheClient = HttpClientBuilder.create().addInterceptorFirst(requestFilter).build();
    }

    public void setLogger(Logger log) {
        this.log = log;
    }

    public void login() throws CxClientException, IOException, CxTokenExpiredException {
        UrlEncodedFormEntity requestEntity = generateUrlEncodedFormEntity();
        HttpPost post = new HttpPost(rootPath + AUTHENTICATION);
        token = request(post, ContentType.APPLICATION_FORM_URLENCODED.toString(), requestEntity, TokenLoginResponse.class, 200, "authenticate", false, false);
    }

    private UrlEncodedFormEntity generateUrlEncodedFormEntity() throws UnsupportedEncodingException {
        List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
        parameters.add(new BasicNameValuePair("username", username));
        parameters.add(new BasicNameValuePair("password", password));
        parameters.add(new BasicNameValuePair("grant_type", "password"));
        parameters.add(new BasicNameValuePair("scope", "sast_rest_api"));
        parameters.add(new BasicNameValuePair("client_id", "resource_owner_client"));
        parameters.add(new BasicNameValuePair("client_secret", "014DF517-39D1-4453-B7B3-9930C563627C"));

        return new UrlEncodedFormEntity(parameters, "utf-8");
    }

    //GET REQUEST
    public <T> T getRequest(String relPath, String contentType, Class<T> responseType, Integer expectStatus, String failedMsg, boolean isCollection) throws IOException, CxClientException, CxTokenExpiredException {
        HttpGet get = new HttpGet(rootPath + relPath);
        return request(get, contentType, null, responseType, expectStatus, "get " + failedMsg, isCollection, true);
    }

    //POST REQUEST
    public <T> T postRequest(String relPath, String contentType, HttpEntity entity, Class<T> responseType, int expectStatus, String failedMsg) throws CxClientException, IOException, CxTokenExpiredException {
        HttpPost post = new HttpPost(rootPath + relPath);
        return request(post, contentType, entity, responseType, expectStatus, failedMsg, false, true);
    }

    //PATCH REQUEST
    public void patchRequest(String relPath, String contentType, HttpEntity entity, int expectStatus, String failedMsg) throws CxClientException, IOException, CxTokenExpiredException {
        HttpPatch patch = new HttpPatch(rootPath + relPath);
        request(patch, contentType, entity, null, expectStatus, failedMsg, false, true);
    }


    private <T> T request(HttpRequestBase httpMethod, String contentType, HttpEntity entity, Class<T> responseType, Integer expectStatus, String failedMsg, boolean isCollection, boolean retry) throws IOException, CxClientException, CxTokenExpiredException {
        if (contentType != null) {
            httpMethod.addHeader("Content-type", contentType);
        }
        if (entity != null && httpMethod instanceof HttpEntityEnclosingRequestBase) {
            ((HttpEntityEnclosingRequestBase) httpMethod).setEntity(entity);
        }
        HttpResponse response = null;

        try {
            response = apacheClient.execute(httpMethod);
            if (response.getStatusLine().getStatusCode() == 401) { //Token expired
                throw new CxTokenExpiredException(extractResponseBody(response));
            }

            if (expectStatus != null) {
                validateResponse(response, expectStatus, "Failed to " + failedMsg);
            }
            //extract response as object and return the link
            return convertToObject(response, responseType, isCollection);
        } catch (CxTokenExpiredException ex) {
            if (retry) {
                // log.warn("token expired");//TODO
                login();
                request(httpMethod, contentType, entity, responseType, expectStatus, failedMsg, isCollection, false);
            }
            throw ex;
        } finally {
            httpMethod.releaseConnection();
            HttpClientUtils.closeQuietly(response);
        }
    }

    public void close() {
        HttpClientUtils.closeQuietly(apacheClient);
    }
}
