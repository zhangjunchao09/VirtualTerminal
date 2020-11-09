package com.zhangjunchao.virtual;

import com.zhangjunchao.virtual.utils.DataTransUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

public class SendMess {
    OutputStream os = null;

    public SendMess(OutputStream os) {
        this.os = os;
    }

    public void sendMessage(String msg) {
        try {
            os.write(DataTransUtils.strToBytes(msg));
            os.flush();
            System.out.println(new Date() + "  send：  " + msg);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
