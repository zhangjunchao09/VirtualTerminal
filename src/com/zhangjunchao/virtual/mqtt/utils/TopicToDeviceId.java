package com.zhangjunchao.virtual.mqtt.utils;

public class TopicToDeviceId {

    public static String propertySetTopicToDeviceId(String topic) {

        ///sys/{productId}/{deviceId}/thing/service/property/set
        String[] strings = topic.split("/");
        return strings[3];
    }

    public static void main(String[] args) {
        String deviceId = propertySetTopicToDeviceId("/sys/{productId}/{deviceId}/thing/service/property/set");
        System.out.println(deviceId);
    }
}
