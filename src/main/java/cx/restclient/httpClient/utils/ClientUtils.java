package cx.restclient.httpClient.utils;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cx.restclient.httpClient.exception.CxClientException;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by Galn on 06/02/2018.
 */
public abstract class ClientUtils {

    public static <T> T convertToObject(HttpResponse response, Class<T> valueType) throws CxClientException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try {
            if (response.getEntity() == null){
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



    public static void validateResponse(HttpResponse response, int status, String message) throws CxClientException, IOException {
        if (response.getStatusLine().getStatusCode() != status) {
            String responseBody = extractResponseBody(response);
            responseBody = responseBody.replace("{", "").replace("}", "").replace(System.getProperty("line.separator"), " ").replace("  ", "");
            throw new CxClientException(message + ": " + "status code: " + response.getStatusLine() + ". error:" + responseBody);
        }
    }

    private static String extractResponseBody(HttpResponse response) {
        try {
            return IOUtils.toString(response.getEntity().getContent(), Charset.defaultCharset());
        } catch (Exception e) {
            return "";
        }
    }
}
