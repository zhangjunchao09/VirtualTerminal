package com.zhangjunchao.virtual.mqtt.model;

public class DeviceInfo {
    String deviceId;
    String productId;
    String secret;

    public String getPropertyPostTopic() {
        return String.format("/sys/%s/%s/thing/event/property/post", productId, deviceId);
    }

    public String getPropertySubscribeTopic() {
        return String.format("/sys/%s/%s/thing/service/property/set", productId, deviceId);
    }

    public String getLoginTopic() {
        return String.format("/ext/session/%s/%s/combine/login", productId, deviceId);
    }

    public String getLoginReplySubscribeTopic() {
        return String.format("/ext/session/%s/%s/combine/login_reply", productId, deviceId);
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
