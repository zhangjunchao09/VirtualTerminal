package com.zhangjunchao.virtual;

public class TerminalSendGpsThread extends Thread {
    String terminalId;
    SendMess sendMess;

    public TerminalSendGpsThread(String terminalId, SendMess sendMess) {
        this.terminalId = terminalId;
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

}
