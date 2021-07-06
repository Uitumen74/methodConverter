package com.mobicom.httpmethodconverter.models;

/**
 *
 * @author uitumen.t
 */
public class DataSendRequest {

    private String url;
    private String method;
    private String contentType;

    public DataSendRequest(String url, String method, String contentType) {
        this.url = url;
        this.method = method;
        this.contentType = contentType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
