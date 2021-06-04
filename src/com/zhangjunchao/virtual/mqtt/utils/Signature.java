package com.zhangjunchao.virtual.mqtt.utils;

import com.zhangjunchao.virtual.mqtt.model.LoginInfo;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class Signature {

    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    private static final char[] HEX_CHAR_TABLE = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * Hmac加密 返回hex格式的结果
     *
     * @param secret
     * @param content
     * @return
     */
    private static String encrypt(String secret, String content) {
        try {
            byte[] data = secret.getBytes(Charset.forName("UTF-8"));
            SecretKey secretKey = new SecretKeySpec(data, HMAC_SHA1_ALGORITHM);
            // 生成一个指定 Mac 算法 的 Mac 对象
            Mac mac = Mac.getInstance(secretKey.getAlgorithm());
            // 用给定密钥初始化 Mac 对象
            mac.init(secretKey);
            byte[] text = content.getBytes(Charset.forName("UTF-8"));
            // 完成 Mac 操作
            byte[] bytes = mac.doFinal(text);
            return bytes2Hex(bytes);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String bytes2Hex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(HEX_CHAR_TABLE[(b & 0xf0) >> 4]);
            sb.append(HEX_CHAR_TABLE[b & 0x0f]);
        }
        return sb.toString();
    }

    public static LoginInfo mqttInfo(String devcieId, String productId, String secret) {

        long time = new Date().getTime();
        String clientId = String.format("%s&%s", devcieId, productId);
        String userName = String.format("%s{timestamp=%s,signmethod=hmacsha1}", devcieId, time);
        String content = String.format("deviceId=%s&productId=%s&timestamp=%s", devcieId, productId, time);
        String password = encrypt(secret, content);

        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setClientId(clientId);
        loginInfo.setUserName(userName);
        loginInfo.setPassword(password.toLowerCase());
        loginInfo.setTimestamp(time + "");
        return loginInfo;
    }

    public static void main(String[] args) {

        LoginInfo loginInfoParent = mqttInfo("20210603001", "SA66EhKg", "LSS9x6M1ctVcha36");

        System.out.println(loginInfoParent.getClientId());
        System.out.println(loginInfoParent.getUserName());
        System.out.println(loginInfoParent.getPassword());

        System.out.println("===========================");

        LoginInfo loginInfoChildren = mqttInfo("LJF20210519ZS002", "3sXd1Ojd", "5F1Vb9LqN1hIg0nt");

        System.out.println(loginInfoChildren.getClientId());
        System.out.println(loginInfoChildren.getUserName());
        System.out.println(loginInfoChildren.getPassword());
    }
}
