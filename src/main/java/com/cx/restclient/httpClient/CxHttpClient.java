package com.cx.restclient.httpClient;

import com.cx.restclient.dto.TokenLoginResponse;
import com.cx.restclient.exception.CxClientException;
import com.cx.restclient.exception.CxTokenExpiredException;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.slf4j.Logger;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
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

    private Logger logi;
    private HttpClient apacheClient;
    private TokenLoginResponse token;
    private String rootUri;
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

    public CxHttpClient(String hostname, String username, String password, String origin, boolean disableSSLValidation, Logger logi) throws MalformedURLException {
        this.logi = logi;
        this.username = username;
        this.password = password;
        this.rootUri = new URL(new URL(hostname), "CxRestAPI/").toString();
        this.cxOrigin = origin;
        //create httpclient
        HttpClientBuilder builder = HttpClientBuilder.create().addInterceptorFirst(requestFilter);
        if (disableSSLValidation) {
            builder = disableCertificateValidation(builder, logi);
        }
        apacheClient = builder.build();
    }

    public void login() throws IOException, CxClientException {
        UrlEncodedFormEntity requestEntity = generateUrlEncodedFormEntity();
        HttpPost post = new HttpPost(rootUri + AUTHENTICATION);
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
    public <T> T getRequest(String relPath, String contentType, Class<T> responseType, int expectStatus, String failedMsg, boolean isCollection) throws IOException, CxClientException {
        HttpGet get = new HttpGet(rootUri + relPath);
        return request(get, contentType, null, responseType, expectStatus, "get " + failedMsg, isCollection, true);
    }

    //POST REQUEST
    public <T> T postRequest(String relPath, String contentType, HttpEntity entity, Class<T> responseType, int expectStatus, String failedMsg) throws IOException, CxClientException {
        HttpPost post = new HttpPost(rootUri + relPath);
        return request(post, contentType, entity, responseType, expectStatus, failedMsg, false, true);
    }

    //PUT REQUEST
    public <T> T putRequest(String relPath, String contentType, HttpEntity entity, Class<T> responseType, int expectStatus, String failedMsg) throws IOException, CxClientException {
        HttpPut put = new HttpPut(rootUri + relPath);
        return request(put, contentType, entity, responseType, expectStatus, failedMsg, false, true);
    }

    //PATCH REQUEST
    public void patchRequest(String relPath, String contentType, HttpEntity entity, int expectStatus, String failedMsg) throws IOException, CxClientException {
        HttpPatch patch = new HttpPatch(rootUri + relPath);
        request(patch, contentType, entity, null, expectStatus, failedMsg, false, true);
    }

    private <T> T request(HttpRequestBase httpMethod, String contentType, HttpEntity entity, Class<T> responseType, int expectStatus, String failedMsg, boolean isCollection, boolean retry) throws IOException, CxClientException {
        if (contentType != null) {
            httpMethod.addHeader("Content-type", contentType);
        }
        if (entity != null && httpMethod instanceof HttpEntityEnclosingRequestBase) { //Entity for Post methods
            ((HttpEntityEnclosingRequestBase) httpMethod).setEntity(entity);
        }
        HttpResponse response = null;

        try {
            response = apacheClient.execute(httpMethod);

            if (response.getStatusLine().getStatusCode() == 401) { //Token expired
                throw new CxTokenExpiredException(extractResponseBody(response));
            }
            validateResponse(response, expectStatus, "Failed to " + failedMsg);

            //extract response as object and return the link
            return convertToObject(response, responseType, isCollection);
        } catch (CxTokenExpiredException ex) {
            if (retry) {
                logi.warn("Access token expired, requesting a new token");
                login();
                return request(httpMethod, contentType, entity, responseType, expectStatus, failedMsg, isCollection, false);
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

    private HttpClientBuilder disableCertificateValidation(HttpClientBuilder builder, Logger logi) {
        try {
            SSLContext disabledSSLContext = SSLContexts.custom().loadTrustMaterial(new TrustStrategy() {
                public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    return true;
                }
            }).build();
            builder.setSslcontext(disabledSSLContext);
            builder.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE);
        } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
            logi.warn("Failed to disable certificate verification: " + e.getMessage());
        }

        return builder;
    }

}
