package com.zhangjunchao.virtual.mqtt.listener;

import com.zhangjunchao.virtual.mqtt.model.ChildDeviceLoginInfo;
import com.zhangjunchao.virtual.mqtt.model.DeviceInfo;
import com.zhangjunchao.virtual.mqtt.model.LoginParams;
import com.zhangjunchao.virtual.mqtt.model.SignInfo;
import com.zhangjunchao.virtual.mqtt.utils.Signature;
import com.zhangjunchao.virtual.utils.GsonUtils;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.HashMap;

public class McListener {

    private String host;

    private DeviceInfo parentDevice;

    private HashMap<String, DeviceInfo> childDevices = new HashMap<>();

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

            String login_reply_topic = parentDevice.getLoginReplySubscribeTopic();
            String property_set_topic = parentDevice.getPropertySubscribeTopic();

            SignInfo signInfoParent = Signature.mqttInfo(deviceId, productId, secret);

            // HOST_MQ为主机名，clientid即连接MQTT的客户端ID，一般以唯一标识符表示，MemoryPersistence设置clientid的保存形式，
            client_sub = new MqttClient(host, signInfoParent.getClientId(), new MemoryPersistence());
            // MQTT的连接设置
            options_sub = new MqttConnectOptions();
            // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，设置为true表示每次连接到服务器都以新的身份连接
            options_sub.setCleanSession(false);
            // 设置连接的用户名
            options_sub.setUserName(signInfoParent.getUserName());
            // 设置连接的密码
            options_sub.setPassword(signInfoParent.getPassword().toCharArray());
            // 设置会话心跳时间 单位为秒 服务器会每隔90秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
            options_sub.setKeepAliveInterval(90);
            //订阅topic定义
            int[] Qos = new int[]{0, 0};
            String[] topics = new String[]{login_reply_topic, property_set_topic};

            // 设置回调
            client_sub.setCallback(new MyMqttCallbackExtended(this));
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

        String login_topic = parentDevice.getLoginTopic();

        SignInfo signInfoChildren = Signature.mqttInfo(childDevice.getDeviceId(), childDevice.getProductId(), childDevice.getSecret());
        LoginParams params = new LoginParams();
        params.setDeviceId(childDevice.getDeviceId());
        params.setProductId(childDevice.getProductId());
        params.setClientId(signInfoChildren.getClientId());
        params.setSign(signInfoChildren.getPassword());
        params.setTimestamp(signInfoChildren.getTimestamp());

        ChildDeviceLoginInfo childDeviceLoginInfo = new ChildDeviceLoginInfo();
        childDeviceLoginInfo.setParams(params);
        String loginJSON = GsonUtils.toJson(childDeviceLoginInfo, false);
        MqttMessage msg_pub = new MqttMessage(loginJSON.getBytes());
        msg_pub.setQos(0);
        try {
            publish(login_topic, msg_pub);
            childDevices.put(childDevice.getDeviceId(), childDevice);

            subscribe(childDevice.getPropertySubscribeTopic(), 0);

        } catch (MqttException e) {
            System.err.println("=======登陆子设备/订阅子设备属性设置topic异常" + e.toString());
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

    public HashMap<String, DeviceInfo> getChildDevices() {
        return childDevices;
    }

    public void setChildDevices(HashMap<String, DeviceInfo> childDevices) {
        this.childDevices = childDevices;
    }
}
