package com.zhangjunchao.virtual;

import com.zhangjunchao.virtual.utils.DataTransUtils;

import java.io.IOException;
import java.util.Date;

public class SendMess {

    public void sendMessage(String msg) {
        try {
            if (VirtualTerminal12.os != null) {
                VirtualTerminal12.os.write(DataTransUtils.strToBytes(msg));
                VirtualTerminal12.os.flush();
                System.out.println(new Date() + "  send：  " + msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
