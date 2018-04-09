package com.cx.restclient.httpClient.utils;

import com.cx.restclient.httpClient.exception.CxClientException;
import com.cx.restclient.httpClient.exception.CxTokenExpiredException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by Galn on 06/02/2018.
 */
public abstract class ClientUtils {
    public static <T> T convertToObject(HttpResponse response, Class<T> responseType, boolean isCollection) throws IOException, CxClientException {
        //TODO errorHandling
        //  if (responseType != null) {
        if (response.getEntity() == null || response.getEntity().getContentLength() == 0 || responseType == null) {
            return null;
        }
        if (responseType.equals(byte[].class)) { //convert to byte[]
            return (T) IOUtils.toByteArray(response.getEntity().getContent());
        }
        if (isCollection) { //convert to List<T>
            return convertToCollectionObject(response, TypeFactory.defaultInstance().constructCollectionType(List.class, responseType));
        }

        return convertToStrObject(response, responseType); //convert to T
        // }
        // return (T)""; //In cases where is no content in the response TODO
    }


    public static <T> T convertToStrObject(HttpResponse response, Class<T> valueType) throws CxClientException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try {
            if (response.getEntity() == null) {
                return null;
            }
            json = IOUtils.toString(response.getEntity().getContent(), Charset.defaultCharset());
            return mapper.readValue(json, valueType);

        } catch (IOException e) {
            // log.debug("Failed to parse json response: [" + json + "]", e);
            throw new CxClientException("Failed to parse json response: " + e.getMessage());
        }
    }

    public static String convertToJson(Object o) throws CxClientException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try {
            return mapper.writeValueAsString(o);
        } catch (Exception e) {
            // log.debug("Failed convert object to json: [" + json + "]", e);
            throw new CxClientException("Failed convert object to json: " + e.getMessage());
        }
    }


    public static <T> T convertToCollectionObject(HttpResponse response, JavaType javaType) throws CxClientException {
        String json = "";
        ObjectMapper mapper = new ObjectMapper();
        try {
            json = IOUtils.toString(response.getEntity().getContent(), Charset.defaultCharset());
            return mapper.readValue(json, javaType);
        } catch (IOException e) {
            // log.debug("Failed to parse json response: [" + json + "]", e);
            throw new CxClientException("Failed to parse json response: " + e.getMessage());
        }
    }


    public static void validateResponse(HttpResponse response, int status, String message) throws CxClientException, IOException, CxTokenExpiredException {
        if (response.getStatusLine().getStatusCode() != status) {
            String responseBody = extractResponseBody(response);
            responseBody = responseBody.replace("{", "").replace("}", "").replace(System.getProperty("line.separator"), " ").replace("  ", "");
            throw new CxClientException(message + ". " + "status code: " + response.getStatusLine() + ". error:" + responseBody);
        }
    }

    public static String extractResponseBody(HttpResponse response) {
        try {
            String str = IOUtils.toString(response.getEntity().getContent(), Charset.defaultCharset());
            return str;
        } catch (Exception e) {
            return "";
        }
    }
}
