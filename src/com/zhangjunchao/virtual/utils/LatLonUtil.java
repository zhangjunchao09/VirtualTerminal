package com.zhangjunchao.virtual.utils;

public class LatLonUtil {

    public static void main(String[] args) {
//        SpringApplication.run(ZvosDatacompareApplication.class, args);
        //D40B0309092C1622011872055E58065900
        getLon("D472055E58", false);
        //114.099147
        parseLonOrLat(114.099147, false);
    }

    public static double getLon(String hex, boolean is157C0) {
        byte[] bytes = hexString2Bytes(hex);
        byte position0 = bytes[0];
        byte position10 = bytes[1];
        byte position11 = bytes[2];
        byte position12 = bytes[3];
        byte position13 = bytes[4];
        ;
        int multiple = 100;
        if (is157C0) {
            multiple = 256;
        }
        int lonFlag = (position0 & 0xFF) >> 7 == 1 ? 1 : -1;
        double ret = (position10 & 0xFF) +
                ((position11 & 0xFF) + ((position12 & 0xFF) * multiple + (position13 & 0xFF)) / 10000.0D) /
                        60.0D;
        double lon = ret * lonFlag;
        return lon;
    }

    public static String parseLonOrLat(double lon, boolean is157C0) {
        int multiple = 100;
        if (is157C0) {
            multiple = 256;
        }
        byte position10 = (byte) lon;
        double fen = (lon - position10) * 60;
        byte position11 = (byte) fen;
        short position1213 = (short) ((fen - position11) * 10000);
        byte position12 = (byte) (position1213 / multiple);
        byte position13 = (byte) (position1213 - position12 * multiple);
        String result = Byte2HexString(position10) + Byte2HexString(position11) + Byte2HexString(position12) + Byte2HexString(position13);
        return result;
    }

    public static byte[] hexString2Bytes(String src) {
        byte[] ret = new byte[src.length() / 2];
        byte[] tmp = src.getBytes();
        for (int i = 0; i < tmp.length / 2; i++) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }
        return ret;
    }

    public static byte uniteBytes(byte src0, byte src1) {
        byte _b0 = Byte.decode("0x" + new String(new byte[]{src0})).byteValue();
        _b0 = (byte) (_b0 << 4);
        byte _b1 = Byte.decode("0x" + new String(new byte[]{src1})).byteValue();
        byte ret = (byte) (_b0 ^ _b1);
        return ret;
    }

    public static String Byte2HexString(byte b) {
        String hex = Integer.toHexString(b & 0xFF);
        if (hex.length() == 1) {
            hex = '0' + hex;
        }
        return hex.toUpperCase();
    }

    public static String Int2HexString(Integer b) {
        String hex = Integer.toHexString(b);
        if (hex.length() == 1) {
            hex = '0' + hex;
        }
        return hex.toUpperCase();
    }

}
