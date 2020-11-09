package com.zhangjunchao.virtual.utils;

public class DataTransUtils {

    public static byte[] strToBytes(String msg) {
        byte[] bytes = new byte[msg.length() / 2];
        for (int n = 0; n < msg.length(); ) {
            String t = msg.substring(n, n + 2);
            byte b = (byte) Integer.parseInt(t, 16);
            bytes[n / 2] = b;
            n = n + 2;
        }
        xorSum(bytes);
        return bytes;
    }

    /**
     * 计算异或和
     *
     * @return
     */
    public static void xorSum(byte[] bytes) {
        int n = bytes.length;
        byte result = bytes[2];
        for (int i = 3; i < n - 2; i++) {
            result ^= bytes[i];
        }
        bytes[n - 2] = result;
    }

    /**
     * int 转2字节 十六进制字符串
     * @param n
     * @return
     */
    public static String intToHexString(int n) {
        return String.format("%02x", n);
    }

    /**
     * byte转十六进制字符串
     * @param bytes
     * @return
     */
    public static String bytesToHexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            int n = bytes[i];
            sb.append(String.format("%02x", n).toUpperCase());
        }
        return sb.toString();
    }
}
