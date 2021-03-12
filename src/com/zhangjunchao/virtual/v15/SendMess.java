package com.zhangjunchao.virtual.v15;

import com.zhangjunchao.virtual.utils.DataTransUtils;

import java.io.IOException;
import java.util.Date;

public class SendMess {

    public void sendMessage(String msg) {
        try {
            if (VirtualTerminal15.os != null) {
                VirtualTerminal15.os.write(DataTransUtils.strToBytes(msg));
                VirtualTerminal15.os.flush();
                System.out.println(new Date() + "  send：  " + msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
