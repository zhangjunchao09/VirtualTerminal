package com.zhangjunchao.virtual.mqtt.model;

public class ChildDeviceLoginInfo {
    private String id = "123";
    private String version = "1.0";
    private LoginParams params;

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

    public LoginParams getParams() {
        return params;
    }

    public void setParams(LoginParams params) {
        this.params = params;
    }
}

