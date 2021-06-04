package com.zhangjunchao.virtual.mqtt.listener;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MyMqttCallbackExtended implements MqttCallbackExtended {

    McListener mcListener;

    String login_reply_topic = String.format("/ext/session/%s/%s/combine/login_reply", mcListener.getParentDevice().getProductId(), mcListener.getParentDevice().getDeviceId());
    String property_set_topic = String.format("/sys/%s/%s/thing/service/property/set", mcListener.getParentDevice().getProductId(), mcListener.getParentDevice().getDeviceId());

    //订阅topic定义
    int[] Qos = new int[]{0, 0};
    String[] topics = new String[]{login_reply_topic, property_set_topic};

    public MyMqttCallbackExtended(McListener mcListener) {
        this.mcListener = mcListener;
    }

    public void connectComplete(boolean reconnect, String serverURI) {
        //连接成功，需要上传客户端所有的订阅关系
        try {
            mcListener.getClient_sub().subscribe(topics, Qos);
        } catch (Exception e) {
            System.err.println("=======重连MQTT HOST 失败: {}, case: {}=========" + serverURI + e.toString());
        }
    }

    public void connectionLost(Throwable cause) {
        // 连接丢失后，一般在这里面进行重连
        System.err.println("=======连接断开，可以做重连==============");
        // reConnect();
    }

    public void deliveryComplete(IMqttDeliveryToken token) {
        System.err.println("=======交付完成: {}==============" + token.isComplete());
    }

    public void messageArrived(String topic, MqttMessage message) {
        //due arrived message...
        System.out.println("=======收到消息topic: {}===Qos: {}" + topic + message.getQos());
        System.out.println("=======message: {}" + message.toString());
    }
}
