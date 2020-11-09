package com.zhangjunchao.virtual;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;

public class TerminalSendGpsThread extends Thread implements Closeable {
    OutputStream os;
    String terminalId;
    SendMess sendMess;

    public TerminalSendGpsThread(String terminalId, OutputStream os,SendMess sendMess) {
        this.terminalId = terminalId;
        this.os = os;
        this.sendMess = sendMess;
    }

    @Override
    public void run() {
        while (true) {
            try {
                sleep(VirtualTerminal12.time_interval);
                sendMess.sendMessage(Protocol12.getLatLonStr(terminalId, VirtualTerminal12.lat, VirtualTerminal12.lon));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void close() throws IOException {
        os.close();
    }
}
