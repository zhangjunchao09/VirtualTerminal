package com.zhangjunchao.virtual.mqtt.listener;

import com.google.gson.JsonObject;
import com.zhangjunchao.virtual.mqtt.model.DeviceInfo;
import com.zhangjunchao.virtual.mqtt.model.DeviceProperty;
import com.zhangjunchao.virtual.mqtt.model.SendJsonInfo;
import com.zhangjunchao.virtual.mqtt.utils.DateUtil;
import com.zhangjunchao.virtual.mqtt.utils.TopicToDeviceId;
import com.zhangjunchao.virtual.utils.GsonUtils;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.HashMap;

public class MyMqttCallbackExtended implements MqttCallbackExtended {

    McListener mcListener;

    public MyMqttCallbackExtended(McListener mcListener) {
        this.mcListener = mcListener;
    }

    public void connectComplete(boolean reconnect, String serverURI) {
        //连接成功，需要上传客户端所有的订阅关系
        try {
            String login_reply_topic = mcListener.getParentDevice().getLoginReplySubscribeTopic();
            String property_set_topic = mcListener.getParentDevice().getPropertySubscribeTopic();
            String[] topics = new String[]{login_reply_topic, property_set_topic};
            //订阅topic定义
            int[] Qos = new int[]{0, 0};
            mcListener.getClient_sub().subscribe(topics, Qos);
        } catch (Exception e) {
            System.err.println("=======重连MQTT HOST 失败: {}, case: {}=========" + serverURI);
            System.err.println(e);
        }
    }

    public void connectionLost(Throwable cause) {
        // 连接丢失后，一般在这里面进行重连
        System.err.println("=======连接断开，可以做重连==============");
        mcListener.reConnect();
    }

    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println("=======交付完成: {}==============" + token.isComplete());
    }

    public void messageArrived(String topic, MqttMessage message) {
        System.out.println("=======收到消息topic: {}===Qos: {}" + topic + message.getQos());
        System.out.println("=======message: {}" + message.toString());

        if (topic.contains("thing/service/property/set")) {
            String deviceId = TopicToDeviceId.propertySetTopicToDeviceId(topic);
            DeviceInfo deviceInfo;
            if (deviceId.equals(mcListener.getParentDevice().getDeviceId())) {
                deviceInfo = mcListener.getParentDevice();
            } else {
                deviceInfo = mcListener.getChildDevices().get(deviceId);
            }
            if (deviceInfo != null) {
                try {
                    String receiveJson = message.toString();
                    JsonObject receive = GsonUtils.fromJson(receiveJson, JsonObject.class);
                    long id = receive.get("id").getAsLong();
                    SendJsonInfo res = new SendJsonInfo();
                    res.setId(id);
                    mcListener.publish(deviceInfo.getPropertyReplySubscribeTopic(), res);

                    JsonObject jsonObject = (JsonObject) receive.get("params");


                    DeviceProperty deviceProperty = new DeviceProperty();
                    for (String key : jsonObject.keySet()) {
                        if (key.equals("time")) {
                            deviceProperty.setTime(jsonObject.get(key).getAsString());
                        } else {
                            deviceProperty.setKey(key);
                            deviceProperty.setValue(jsonObject.get(key).getAsString());
                        }
                    }
                    deviceInfo.setProperty(deviceProperty);
                    HashMap<String, DeviceProperty> params = new HashMap<>();

                    if (deviceProperty.getKey().equals("onoff")) {
                        if (deviceProperty.getValue().equals("on")) {

                            String time = DateUtil.getCurrentTimeStr();

                            DeviceProperty devicePropertyOpen = new DeviceProperty();
                            devicePropertyOpen.setValue("1");
                            devicePropertyOpen.setTime(time);
                            params.put("full-open", devicePropertyOpen);

                            DeviceProperty devicePropertyClose = new DeviceProperty();
                            devicePropertyClose.setValue("0");
                            devicePropertyClose.setTime(time);
                            params.put("full-close", devicePropertyClose);
                        }
                        if (deviceProperty.getValue().equals("off")) {

                            String time = DateUtil.getCurrentTimeStr();

                            DeviceProperty devicePropertyOpen = new DeviceProperty();
                            devicePropertyOpen.setValue("0");
                            devicePropertyOpen.setTime(time);
                            params.put("full-open", devicePropertyOpen);

                            DeviceProperty devicePropertyClose = new DeviceProperty();
                            devicePropertyClose.setValue("1");
                            devicePropertyClose.setTime(time);
                            params.put("full-close", devicePropertyClose);
                        }
                    } else {
                        params.put(deviceProperty.getKey(), deviceProperty);
                    }
                    SendJsonInfo<DeviceProperty> postProperty = new SendJsonInfo<>();
                    postProperty.setParams(params);

                    mcListener.publish(deviceInfo.getPropertyPostTopic(), postProperty);

                } catch (MqttException e) {
                    System.err.println(e);
                } catch (Exception e) {
                    System.err.println(e);
                }
            }
        }
    }

}
