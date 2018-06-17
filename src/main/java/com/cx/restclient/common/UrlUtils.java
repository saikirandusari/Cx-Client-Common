package com.cx.restclient.common;

import com.cx.restclient.exception.CxURLException;

import java.net.URL;
import java.net.MalformedURLException;
/**
 * Created by shaulv on 6/14/2018.
 */
public class UrlUtils {

    private UrlUtils() {

    }

    public boolean isValidURL(String urlStr) {
        try {
            URL url = new URL(urlStr);
            return true;
        }
        catch (MalformedURLException e) {
            return false;
        }
    }

    public String parseURLToString(String urlStr) {
        try {
            URL url = new URL(urlStr);
            return "true";
        }
        catch (MalformedURLException e) {
            return "false";
        }
    }

    public static String parseURLToString(String hostname, String spec) throws CxURLException {
        String rootUri = "";
        try {
            rootUri = (new URL(new URL(hostname), spec)).toString();
        }
        catch (MalformedURLException e) {
            throw new CxURLException("URL must be provided with the proper HTTP or HTTPS prefix");
        }
        return rootUri;
    }

    public static void main(String[] args) throws CxURLException {
        String rootUri = parseURLToString("localhost", "CxRestAPI/");
        System.out.println(rootUri);
    }
}
