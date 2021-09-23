package com.zhangjunchao.virtual.mqtt.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DeviceInfo {
    String deviceId;
    String productId;
    String secret;

    HashMap<String, DeviceProperty> properties = new HashMap<>();

    /**
     * 上报固件版本号topic
     *
     * @return
     */
    public String getOtaInformTopic() {
        return String.format("/ota/%s/%s/device/inform", productId, deviceId);
    }

    /**
     * 接收ota升级topic
     *
     * @return
     */
    public String getOtaUpgradeTopic() {
        return String.format("/ota/%s/%s/device/upgrade", productId, deviceId);
    }

    /**
     * 上报ota下载进度topic
     *
     * @return
     */
    public String getOtaUpgradeProgressTopic() {
        return String.format("/ota/%s/%s/device/upgrade/progress", productId, deviceId);
    }

    /**
     * 上报ota升级结果topic
     *
     * @return
     */
    public String getOtaUpgradeResultTopic() {
        return String.format("/ota/%s/%s/device/upgrade/result", productId, deviceId);
    }

    /**
     * 上报ota升级结果平台回复topic
     *
     * @return
     */
    public String getOtaUpgradeResultReplyTopic() {
        return String.format("/ota/%s/%s/device/upgrade/result_reply", productId, deviceId);
    }

    /**
     * 上报属性topic
     *
     * @return
     */
    public String getPropertyPostTopic() {
        return String.format("/sys/%s/%s/thing/event/property/post", productId, deviceId);
    }

    /**
     * 设置属性topic
     *
     * @return
     */
    public String getPropertySubscribeTopic() {
        return String.format("/sys/%s/%s/thing/service/property/set", productId, deviceId);
    }

    /**
     * 属性设置回复topic
     *
     * @return
     */
    public String getPropertyReplySubscribeTopic() {
        return String.format("/sys/%s/%s/thing/service/property/set_reply", productId, deviceId);
    }

    /**
     * 登陆子设备topic
     *
     * @return
     */
    public String getLoginTopic() {
        return String.format("/ext/session/%s/%s/combine/login", productId, deviceId);
    }

    /**
     * 子设备登陆回复topic
     *
     * @return
     */
    public String getLoginReplySubscribeTopic() {
        return String.format("/ext/session/%s/%s/combine/login_reply", productId, deviceId);
    }

    public void setProperty(DeviceProperty deviceProperty) {
        properties.put(deviceProperty.getKey(), deviceProperty);
    }

    public DeviceProperty getProperty(String key) {
        return properties.get(key);
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

    public HashMap<String, DeviceProperty> getProperties() {
        return properties;
    }

    public void setProperties(HashMap<String, DeviceProperty> properties) {
        this.properties = properties;
    }

    public List<String> pLoginSubTopic() {
        List<String> topics = new ArrayList<>();
        topics.add(getLoginReplySubscribeTopic());
        topics.add(getPropertySubscribeTopic());
        topics.add(getOtaUpgradeResultReplyTopic());
        topics.add(getOtaUpgradeTopic());

        return topics;
    }

    public List<String> cLoginSubTopic() {
        List<String> topics = new ArrayList<>();
        topics.add(getPropertySubscribeTopic());

        return topics;
    }

    public List<String> dLoginSubTopic() {
        List<String> topics = new ArrayList<>();
        topics.add(getPropertySubscribeTopic());
        topics.add(getOtaUpgradeResultReplyTopic());
        topics.add(getOtaUpgradeTopic());

        return topics;
    }
}
