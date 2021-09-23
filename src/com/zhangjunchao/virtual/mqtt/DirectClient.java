package com.zhangjunchao.virtual.mqtt;

import com.google.gson.reflect.TypeToken;
import com.zhangjunchao.virtual.mqtt.listener.McListener;
import com.zhangjunchao.virtual.mqtt.model.DeviceInfo;
import com.zhangjunchao.virtual.mqtt.thread.PropertySendJob;
import com.zhangjunchao.virtual.utils.GsonUtils;

import java.util.Map;
import java.util.Scanner;

public class DirectClient {

    private static String host = "tcp://10.39.52.191:1883";
    private static String deviceId = "1111111114444";
    private static String productId = "xZQ6vOjr";
    private static String secret = "S7K82dZMHBfZficg";


    public static void main(String[] args) {

        try {
            DeviceInfo parentDevice = new DeviceInfo();
            parentDevice.setDeviceId(deviceId);
            parentDevice.setProductId(productId);
            parentDevice.setSecret(secret);

            McListener mcListener = new McListener(host, parentDevice);

            mcListener.initMQTTListener(2);


            new PropertySendJob(mcListener).start();

            Scanner scanner = new Scanner(System.in);

            while (true) {
                String position = scanner.nextLine();
                try {
                    String[] argss = position.trim().split("\\s+");
                    String opt = argss[0];
                    String deviceId;
                    switch (opt) {
                        case "-df":
                            deviceId = argss[1];
                            mcListener.postDeviceInform(deviceId, argss[2]);
                            break;
                        case "-dp":
                            deviceId = argss[1];
                            mcListener.postDeviceUpgradeProgress(deviceId, argss[2]);
                            break;
                        case "-dr":
                            deviceId = argss[1];
                            mcListener.postDeviceUpgradeResult(deviceId, argss[2]);
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
