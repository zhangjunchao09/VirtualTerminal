package com.zhangjunchao.virtual.mqtt.model;

public class ChildDeviceLoginInfo {
    private String id = "123";
    private String version = "1.0";
    private Params params;

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

    public Params getParams() {
        return params;
    }

    public void setParams(Params params) {
        this.params = params;
    }
}

