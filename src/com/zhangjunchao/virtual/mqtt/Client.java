package com.zhangjunchao.virtual.mqtt;

import com.google.gson.reflect.TypeToken;
import com.zhangjunchao.virtual.mqtt.listener.McListener;
import com.zhangjunchao.virtual.mqtt.model.DeviceInfo;
import com.zhangjunchao.virtual.mqtt.thread.PropertySendJob;
import com.zhangjunchao.virtual.utils.GsonUtils;

import java.util.Map;
import java.util.Scanner;

public class Client {

    private static String host = "tcp://10.67.1.120:1883";
    private static String deviceId = "20210603001";
    private static String productId = "SA66EhKg";
    private static String secret = "LSS9x6M1ctVcha36";

    private static String childDeviceId = "20210603009";
    private static String childProductId = "AFIeiEN7";
    private static String childSecret = "kdBR57zfjmoNnFz6";

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

            new PropertySendJob(mcListener).start();

            Scanner scanner = new Scanner(System.in);

            while (true) {
                String position = scanner.nextLine();
                try {
                    String[] argss = position.trim().split("\\s+");
                    String opt = argss[0];
                    switch (opt) {
                        case "-sp":
                            String deviceId = argss[1];
                            Map<String, Object> map = GsonUtils.mapFromJson(argss[2], new TypeToken<Map<String, Object>>() {
                            });
                            mcListener.postProperty(deviceId, map);
                            break;
                        case "-ac":
                            DeviceInfo deviceInfo = new DeviceInfo();
                            deviceInfo.setDeviceId(argss[1]);
                            deviceInfo.setProductId(argss[2]);
                            deviceInfo.setSecret(argss[3]);
                            mcListener.childDeviceLogin(deviceInfo);
                            break;
                        default:

                    }
                } catch (Exception e) {
                    System.out.println(e);
                }

            }
        } catch (Exception e) {
            System.err.println("=======发布主题消息失败：topic: {}=========");
            System.err.println(e);
        }
    }


}
