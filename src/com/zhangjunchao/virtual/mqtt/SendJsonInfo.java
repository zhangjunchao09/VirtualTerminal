package com.zhangjunchao.virtual.mqtt;

import java.util.HashMap;

public class SendJsonInfo {
    private String id = "123";
    private String version = "1.0";
    private HashMap params;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public HashMap getParams() {
        return params;
    }

    public void setParams(HashMap params) {
        this.params = params;
    }
}

