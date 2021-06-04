package com.zhangjunchao.virtual.mqtt.listener;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zhangjunchao.virtual.mqtt.model.ChildDeviceLoginInfo;
import com.zhangjunchao.virtual.mqtt.model.DeviceInfo;
import com.zhangjunchao.virtual.mqtt.model.LoginInfo;
import com.zhangjunchao.virtual.mqtt.model.Params;
import com.zhangjunchao.virtual.mqtt.utils.Signature;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.List;

public class McListener {

    private static final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    private String host;

    private DeviceInfo parentDevice;

    private List<DeviceInfo> childDevices;

    private MqttClient client_sub;
    private MqttConnectOptions options_sub;

    public McListener(String host, DeviceInfo parentDevice) {
        this.host = host;
        this.parentDevice = parentDevice;
    }

    public void initMQTTListener() {
        try {

            String deviceId = parentDevice.getDeviceId();
            String productId = parentDevice.getProductId();
            String secret = parentDevice.getSecret();

            String login_reply_topic = String.format("/ext/session/%s/%s/combine/login_reply", productId, deviceId);
            String property_set_topic = String.format("/sys/%s/%s/thing/service/property/set", productId, deviceId);
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
            int[] Qos = new int[]{0, 0};
            String[] topics = new String[]{login_reply_topic, property_set_topic};

            // 设置回调
            client_sub.setCallback(new MqttCallbackExtended() {
                public void connectComplete(boolean reconnect, String serverURI) {
                    //连接成功，需要上传客户端所有的订阅关系
                    try {
                        client_sub.subscribe(topics, Qos);
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

    public void childDeviceLogin(DeviceInfo childDevice) {
        String login_topic = String.format("/ext/session/%s/%s/combine/login", parentDevice.getProductId(), parentDevice.getDeviceId());

        LoginInfo loginInfoChildren = Signature.mqttInfo(childDevice.getDeviceId(), childDevice.getProductId(), childDevice.getSecret());
        Params params = new Params();
        params.setDeviceId(childDevice.getDeviceId());
        params.setProductId(childDevice.getProductId());
        params.setClientId(loginInfoChildren.getClientId());
        params.setSign(loginInfoChildren.getPassword());
        params.setTimestamp(loginInfoChildren.getTimestamp());

        ChildDeviceLoginInfo childDeviceLoginInfo = new ChildDeviceLoginInfo();
        childDeviceLoginInfo.setParams(params);
        String loginJSON = gson.toJson(childDeviceLoginInfo);
        MqttMessage msg_pub = new MqttMessage(loginJSON.getBytes());
        msg_pub.setQos(0);
        try {
            publish(login_topic, msg_pub);

            String child_property_set_topic = String.format("/sys/%s/%s/thing/service/property/set", childDevice.getProductId(), childDevice.getDeviceId());
            subscribe(child_property_set_topic, 0);

        } catch (MqttException e) {
            System.err.println("=======发布主题消息失败：topic: {}=========" + login_topic);
        }
    }

    public void subscribe(String topic, int qos) throws MqttException {
        this.client_sub.subscribe(topic, qos);
    }

    public void publish(String topic, MqttMessage message) throws MqttException {
        this.client_sub.publish(topic, message.getPayload(), 1, true);
    }

    public MqttClient getClient_sub() {
        return client_sub;
    }

    public DeviceInfo getParentDevice() {
        return parentDevice;
    }

    public void setParentDevice(DeviceInfo parentDevice) {
        this.parentDevice = parentDevice;
    }

    public List<DeviceInfo> getChildDevices() {
        return childDevices;
    }

    public void setChildDevices(List<DeviceInfo> childDevices) {
        this.childDevices = childDevices;
    }
}
