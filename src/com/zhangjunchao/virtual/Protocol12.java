package com.zhangjunchao.virtual;

import com.zhangjunchao.virtual.utils.LatLonUtil;

public class Protocol12 {

    /**
     * 12 登陆
     * @param terminalId
     * @return
     */
    public static String getLoginStr(String terminalId) {
        String start = "5A4C";
        String cmd = "19";
        String dataLen = "0019";
        String dataBlock = "31323334353637380A0A012C22313339323635363131323722";
        String serialNumber = "00000000";
        String end = "F80D";
        return start + cmd + serialNumber + terminalId + "000" + dataLen + dataBlock + end;
    }

    public static String getLatLonStr(String terminalId, double lat, double lon) {
        String start = "5A4C";
        String cmd = "18";
        String dataBlock = "D4" + "0B0309092C" + LatLonUtil.parseLonOrLat(lat, false) + LatLonUtil.parseLonOrLat(lon, false) + "065900";
        String dataLen = String.format("%04x", dataBlock.length() / 2);
        String serialNumber = "00000000";
        String end = "760D";
        return start + cmd + serialNumber + terminalId + "000" + dataLen + dataBlock + end;
    }
}