package com.zhangjunchao.virtual.v15;

import com.zhangjunchao.virtual.utils.LatLonUtil;

public class Protocol15 {

    private static String type = "4E4AA114";

    /**
     * 15 登陆
     *
     * @param terminalId
     * @return
     */
    public static String getLoginStr(String terminalId) {
        String start = "5A4C";
        String cmd = "19";
        String dataLen = "0022";
        String dataBlock = "FFFFFFFFFFFFFFFF000000001E223130363438393931363030313622222222222222";
        String serialNumber = "00000000";
        String end = "F80D";
        return start + cmd + serialNumber + terminalId + "000" + type + dataLen + dataBlock + end;
    }

    public static String getLatLonStr(String terminalId, double lat, double lon) {
        String start = "5A4C";
        String cmd = "18";
        String dataBlock = "D4" + "0B0309092C" + LatLonUtil.parseLonOrLat(lat, false) + LatLonUtil.parseLonOrLat(lon, false) + "065900";
        String dataLen = String.format("%04x", dataBlock.length() / 2);
        String serialNumber = "00000000";
        String end = "760D";
        return start + cmd + serialNumber + terminalId + "000" + type + dataLen + dataBlock + end;
    }

    public static String locateQueryResponse(String terminalId, String serialNumber, double lat, double lon) {
        String start = "5A4C";
        String cmd = "17";
        String dataBlock = "D4" + "0B0309092C" + LatLonUtil.parseLonOrLat(lat, false) + LatLonUtil.parseLonOrLat(lon, false) + "065900";
        String dataLen = String.format("%04x", dataBlock.length() / 2);
        String end = "760D";
        return start + cmd + serialNumber + terminalId + "000" + type + dataLen + dataBlock + end;
    }

    public static String paramSetResponse(String terminalId, String ser) {
        String start = "5A4C";
        String cmd = "18";
        String dataBlock = "1200";
        String dataLen = String.format("%04x", dataBlock.length() / 2);
        String end = "760D";
        return start + cmd + ser + terminalId + "000" + type + dataLen + dataBlock + end;
    }

    public static String paramQueryResponse(String terminalId, String ser) {
        String start = "5A4C";
        String cmd = "18";
        String dataBlock = "1200";
        String dataLen = String.format("%04x", dataBlock.length() / 2);
        String end = "760D";
        return start + cmd + ser + terminalId + "000" + type + dataLen + dataBlock + end;
    }

    public static String limitSpeedResponse(String terminalId, String serialNumber) {
        String start = "5A4C";
        String cmd = "13";
        String dataBlock = "13014D0100FFFFFFFFFFFFFFFFFFFB0D";
        String dataLen = String.format("%04x", dataBlock.length() / 2);
        String end = "760D";
        return start + cmd + serialNumber + terminalId + "000" + type + dataLen + dataBlock + end;
    }

    public static void setType(String type) {
        Protocol15.type = type;
    }
}
