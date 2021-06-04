package com.zhangjunchao.virtual.mqtt;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import static java.lang.Thread.sleep;

public class Client {

    private static String host = "tcp://10.67.1.120:1883";
    private static String deviceId = "20210603001";
    private static String productId = "SA66EhKg";
    private static String secret = "LSS9x6M1ctVcha36";

    private static String childDeviceId = "20210603009";
    private static String childProductId = "AFIeiEN7";
    private static String childSecret = "kdBR57zfjmoNnFz6";

    private static final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();


    public static void main(String[] args) {
        LoginInfo loginInfoChildren = Signature.mqttInfo(childDeviceId, childProductId, childSecret);
        Params params = new Params();
        params.setDeviceId(childDeviceId);
        params.setProductId(childProductId);
        params.setClientId(loginInfoChildren.getClientId());
        params.setSign(loginInfoChildren.getPassword());
        params.setTimestamp(loginInfoChildren.getTimestamp());

        ChildDeviceLoginInfo childDeviceLoginInfo = new ChildDeviceLoginInfo();
        childDeviceLoginInfo.setParams(params);
        String loginJSON = gson.toJson(childDeviceLoginInfo);

        String login_topic = String.format("/ext/session/%s/%s/combine/login", productId, deviceId);

        MqttMessage msg_pub = new MqttMessage(loginJSON.getBytes());
        msg_pub.setQos(0);

        System.out.println("=======准备发送Topic：{}========" + login_topic);
        System.out.println("=======准备发送Message：{}========" + loginJSON);
        try {
            McListener mcListener = new McListener(host, deviceId, productId, secret);
            mcListener.initMQTTListener();
            mcListener.publish(login_topic, msg_pub);

            while (true) {
                sleep(1000);
            }
        } catch (Exception e) {
            System.err.println("=======发布主题消息失败：topic: {}=========" + login_topic);
        }
    }
}
