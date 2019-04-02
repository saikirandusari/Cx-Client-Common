package com.cx.restclient.dto;

/**
 * Created by Galn on 4/2/2019.
 */
public class CxProxy {
    private Boolean useProxy;
    private String proxyHost;
    private Integer proxyPort;
    private String scheme;
    private String proxyUser;
    private String proxyPass;


    public CxProxy(Boolean useProxy, String proxyHost, Integer proxyPort, String scheme, String proxyUser, String proxyPass) {
        this.useProxy = useProxy;
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
        this.scheme = scheme;
        this.proxyUser = proxyUser;
        this.proxyPass = proxyPass;
    }

    public CxProxy() {
    }

    public Boolean getUseProxy() {
        return useProxy;
    }

    public void setUseProxy(Boolean useProxy) {
        this.useProxy = useProxy;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public Integer getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(Integer proxyPort) {
        this.proxyPort = proxyPort;
    }

    public void setProxyPort(String proxyPort) {
        try {
            this.proxyPort = Integer.parseInt(proxyPort);
        }catch (Exception ex){}
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getProxyUser() {
        return proxyUser;
    }

    public void setProxyUser(String proxyUser) {
        this.proxyUser = proxyUser;
    }

    public String getProxyPass() {
        return proxyPass;
    }

    public void setProxyPass(String proxyPass) {
        this.proxyPass = proxyPass;
    }
}
