package com.zhangjunchao.virtual.v15;

public class TerminalSendGpsThread extends Thread {
    String terminalId;

    public TerminalSendGpsThread(String terminalId) {
        this.terminalId = terminalId;
    }

    @Override
    public void run() {
        while (true) {
            try {
                sleep(VirtualTerminal15.time_interval);
                VirtualTerminal15.sendMess.sendMessage(Protocol15.getLatLonStr(terminalId, VirtualTerminal15.lat, VirtualTerminal15.lon));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
