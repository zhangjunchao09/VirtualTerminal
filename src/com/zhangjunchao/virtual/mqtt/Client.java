package com.zhangjunchao.virtual.mqtt;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.HashMap;
import java.util.Scanner;

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

        try {
            McListener mcListener = new McListener(host, deviceId, productId, secret);
            mcListener.initMQTTListener();
            mcListener.publish(login_topic, msg_pub);

            String child_property_set_topic = String.format("/sys/%s/%s/thing/service/property/set", childProductId, childDeviceId);
            mcListener.subscribe(child_property_set_topic, 0);
            Scanner scanner = new Scanner(System.in);

            while (true) {
                String position = scanner.nextLine();  // -sc json
                try {
                    String[] argss = position.trim().split("\\|");
                    String opt = argss[0];
                    switch (opt) {
                        case "-sc":
                            String topicSC = String.format("/sys/%s/%s/thing/event/property/post", childProductId, childDeviceId);
                            HashMap mapSC = gson.fromJson(argss[1], HashMap.class);
                            SendJsonInfo sendJsonInfoSC = new SendJsonInfo();
                            sendJsonInfoSC.setParams(mapSC);
                            MqttMessage content = new MqttMessage(gson.toJson(sendJsonInfoSC).getBytes());
                            content.setQos(0);
                            mcListener.publish(topicSC, content);
                            break;
                        case "-sp":
                            String topicSP = String.format("/sys/%s/%s/thing/service/property/post", productId, deviceId);
                            HashMap mapSP = gson.fromJson(argss[1], HashMap.class);
                            SendJsonInfo sendJsonInfoSP = new SendJsonInfo();
                            sendJsonInfoSP.setParams(mapSP);
                            MqttMessage contentSP = new MqttMessage(gson.toJson(sendJsonInfoSP).getBytes());
                            contentSP.setQos(0);
                            mcListener.publish(topicSP, contentSP);
                            break;
                        default:

                    }
                } catch (Exception e) {
                    System.out.println(e);
                }

            }
        } catch (Exception e) {
            System.err.println("=======发布主题消息失败：topic: {}=========" + login_topic);
        }
    }
}
