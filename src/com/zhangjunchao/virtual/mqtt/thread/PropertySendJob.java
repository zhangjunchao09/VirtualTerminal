package com.zhangjunchao.virtual.mqtt.thread;

import com.zhangjunchao.virtual.mqtt.listener.McListener;
import com.zhangjunchao.virtual.mqtt.model.DeviceInfo;
import com.zhangjunchao.virtual.mqtt.model.DeviceProperty;
import com.zhangjunchao.virtual.mqtt.model.SendJsonInfo;
import com.zhangjunchao.virtual.mqtt.utils.DateUtil;

import java.util.HashMap;

public class PropertySendJob extends Thread {

    private static long TIME = 10000;

    private McListener mcListener;

    public PropertySendJob(McListener mcListener) {
        this.mcListener = mcListener;
    }

    @Override
    public void run() {

        while (true) {
            try {
                DeviceInfo parentDevice = mcListener.getParentDevice();
                sendProperties(parentDevice);
                HashMap<String, DeviceInfo> childDevices = mcListener.getChildDevices();
                childDevices.forEach((s, deviceInfo) -> sendProperties(deviceInfo));

                Thread.sleep(TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    void sendProperties(DeviceInfo deviceInfo) {
        try {
            HashMap<String, DeviceProperty> properties = deviceInfo.getProperties();
            if (properties.size() > 0) {
                String propertyPostTopic = deviceInfo.getPropertyPostTopic();
                String time = DateUtil.getCurrentTimeStr();

                properties.forEach((s, deviceProperty) -> deviceProperty.setTime(time));
                SendJsonInfo<DeviceProperty> postProperty = new SendJsonInfo<>();
                postProperty.setParams(deviceInfo.getProperties());
                mcListener.publish(propertyPostTopic, postProperty);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
