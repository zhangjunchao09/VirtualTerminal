package com.zhangjunchao.virtual.v15;

import com.zhangjunchao.virtual.utils.DataTransUtils;
import com.zhangjunchao.virtual.utils.ParamsUtils;

import java.io.IOException;
import java.util.Date;

public class ReceiveMessThread extends Thread {

    @Override
    public void run() {
        try {
            int batch = 1024;
            byte[] buf = new byte[batch];
            int len = 0;
            int count = 1;
            byte[] data = new byte[batch];
            while (true) {
                if (VirtualTerminal15.in != null) {
                    len = VirtualTerminal15.in.read(buf);
                    if (len != -1) {
                        if (len == batch) {
                            if (count > 1) {
                                byte[] t = data;
                                int length = (count - 1) * batch + len;
                                data = new byte[length];
                                for (int i = 0; i < t.length; i++) {
                                    data[i] = t[i];
                                }
                                for (int i = t.length; i < length; i++) {
                                    data[i] = buf[i];
                                }
                            } else {
                                data = new byte[len];
                                for (int i = 0; i < len; i++) {
                                    data[i] = buf[i];
                                }
                            }
                            count++;
                        } else {
                            if (count > 1) {
                                byte[] t = data;
                                int length = (count - 1) * batch + len;
                                data = new byte[length];
                                for (int i = 0; i < t.length; i++) {
                                    data[i] = t[i];
                                }
                                for (int i = t.length; i < length; i++) {
                                    data[i] = buf[i];
                                }
                            } else {
                                data = new byte[len];
                                for (int i = 0; i < len; i++) {
                                    data[i] = buf[i];
                                }
                            }
                            String receive = DataTransUtils.bytesToHexString(data);
                            handlReceive(receive);
                            count = 1;
                            data = new byte[1];
                        }
                    }
                }
                sleep(1000);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    public void handlReceive(String receive) {
        System.out.println(new Date() + "  client receive：  " + receive);
        int cmdId = Integer.parseInt(receive.substring(4, 6), 16);
        String ser = receive.substring(6, 14);
        String ter = receive.substring(14, 27);
        int dataLen = Integer.parseInt(receive.substring(30, 34), 16);
        String data = receive.substring(34, (receive.length() - 4));

        if (cmdId == 0x12) {
            String key = data.substring(0, 2);
            String opt = data.substring(2, 4);
            if (opt.equals("01")) {
                String value = data.substring(4, 6);
                ParamsUtils.params.put(key, value);
                VirtualTerminal15.sendMess.sendMessage(Protocol15.paramSetResponse(ter, ser));
            }
            if (opt.equals("00")) {
                String value = ParamsUtils.params.get(key);
                VirtualTerminal15.sendMess.sendMessage(Protocol15.paramQueryResponse(ter, ser));
            }
        }
        if (cmdId == 0x11) {
            VirtualTerminal15.sendMess.sendMessage(Protocol15.locateQueryResponse(ter, ser, VirtualTerminal15.lat, VirtualTerminal15.lon));
        }
        if (cmdId == 0x13) {
            VirtualTerminal15.sendMess.sendMessage(Protocol15.limitSpeedResponse(ter, ser));
        }
    }
}
