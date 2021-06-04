package com.zhangjunchao.virtual.mqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class McListener {

    private String host;
    private String deviceId;
    private String productId;
    private String secret;

    private MqttClient client_sub;
    private MqttConnectOptions options_sub;

    public McListener(String host, String deviceId, String productId, String secret) {
        this.host = host;
        this.deviceId = deviceId;
        this.productId = productId;
        this.secret = secret;
    }

    public void initMQTTListener() {
        try {
            String login_reply_topic = String.format("/ext/session/%s/%s/combine/login_reply", productId, deviceId);
            LoginInfo loginInfoParent = Signature.mqttInfo(deviceId, productId, secret);

            // HOST_MQ为主机名，clientid即连接MQTT的客户端ID，一般以唯一标识符表示，MemoryPersistence设置clientid的保存形式，
            client_sub = new MqttClient(host, loginInfoParent.getClientId(), new MemoryPersistence());
            // MQTT的连接设置
            options_sub = new MqttConnectOptions();
            // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，设置为true表示每次连接到服务器都以新的身份连接
            options_sub.setCleanSession(false);
            // 设置连接的用户名
            options_sub.setUserName(loginInfoParent.getUserName());
            // 设置连接的密码
            options_sub.setPassword(loginInfoParent.getPassword().toCharArray());
            // 设置会话心跳时间 单位为秒 服务器会每隔90秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
            options_sub.setKeepAliveInterval(90);
            //订阅topic定义
            int[] Qos = new int[]{0};
            String[] topics = new String[]{login_reply_topic};

            // 设置回调
            client_sub.setCallback(new MqttCallbackExtended() {
                public void connectComplete(boolean reconnect, String serverURI) {
                    //连接成功，需要上传客户端所有的订阅关系
                    try {
                        client_sub.subscribe(topics, Qos);
                        System.out.println("=======连接MQTT HOST 成功,重发Topics 1{}=2{}=3{}=======" + login_reply_topic);
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
            });
            //连接mqtt服务器broker
            client_sub.connect(options_sub);
            //订阅消息
            client_sub.subscribe(topics, Qos);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.err.println("init listener MQTT err info: {}" + e.toString());
            System.exit(-1);
        }
    }

    /**
     * mqtt重连
     */
    public void reConnect() {
        try {
            if (null != client_sub && !(client_sub.isConnected())) {
                client_sub.reconnect();
                System.err.println("=======尝试重新连接==============");
            }
        } catch (MqttException e) {
            System.err.println("=======重新连接失败:{}==============" + e.toString());
        }
    }

    public void publish(String topic, MqttMessage message) throws MqttException {
        this.client_sub.publish(topic, message.getPayload(), 1, true);
    }

}
