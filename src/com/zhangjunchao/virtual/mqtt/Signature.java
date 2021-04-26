package com.zhangjunchao.virtual.mqtt;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

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

    public static void mqttInfo(String devcieId, String productId, String secret, String time) {

        String clientId = String.format("%s&%s", devcieId, productId);
        String username = String.format("%s{timestamp=%s,signmethod=hmacsha1}", devcieId, time);
        String content = String.format("deviceId=%s&productId=%s&timestamp=%s", devcieId, productId, time);
        String password = encrypt(secret, content);

        System.out.println(clientId);
        System.out.println(username);
        System.out.println(content);
        System.out.println(password.toLowerCase());
        System.out.println("===========================");
    }

    public static void main(String[] args) {
        mqttInfo("zvos", "test", "ABCD1234", "1571047235");

        mqttInfo("lgctestes", "NcZ62Ge5", "3ObGhRF3Xua2VHkyRcsVeTdQ6WjMIEkK", "1571047235");
    }
}
