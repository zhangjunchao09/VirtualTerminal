package com.zhangjunchao.virtual.mqtt.model;

import java.util.HashMap;

public class SendJsonInfo<T> {
    private long id = 123;
    private String version = "1.0";
    private HashMap<String, T> params = new HashMap();

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

