package com.zhangjunchao.virtual.mqtt;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zhangjunchao.virtual.mqtt.listener.McListener;
import com.zhangjunchao.virtual.mqtt.model.DeviceInfo;
import com.zhangjunchao.virtual.mqtt.model.SendJsonInfo;
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

        try {
            DeviceInfo parentDevice = new DeviceInfo();
            parentDevice.setDeviceId(deviceId);
            parentDevice.setProductId(productId);
            parentDevice.setSecret(secret);

            McListener mcListener = new McListener(host, parentDevice);

            // 父设备上线　订阅登陆回复topic
            mcListener.initMQTTListener();

            DeviceInfo childDevice = new DeviceInfo();
            childDevice.setDeviceId(childDeviceId);
            childDevice.setProductId(childProductId);
            childDevice.setSecret(childSecret);

            //子设备上线 订阅子设备topic
            mcListener.childDeviceLogin(childDevice);

            Scanner scanner = new Scanner(System.in);

            while (true) {
                String position = scanner.nextLine();  // -sc json
                try {
                    String[] argss = position.trim().split("\\|");
                    String opt = argss[0];
                    switch (opt) {
                        case "-sc":
                            String topicSC = childDevice.getPropertyPostTopic();
                            HashMap mapSC = gson.fromJson(argss[1], HashMap.class);
                            SendJsonInfo sendJsonInfoSC = new SendJsonInfo();
                            sendJsonInfoSC.setParams(mapSC);
                            MqttMessage content = new MqttMessage(gson.toJson(sendJsonInfoSC).getBytes());
                            content.setQos(0);
                            mcListener.publish(topicSC, content);
                            break;
                        case "-sp":
                            String topicSP = parentDevice.getPropertyPostTopic();
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
            System.err.println("=======发布主题消息失败：topic: {}=========");
        }
    }


}
