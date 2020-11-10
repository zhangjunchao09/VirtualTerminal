package com.zhangjunchao.virtual;

public class TerminalSendGpsThread extends Thread {
    String terminalId;

    public TerminalSendGpsThread(String terminalId) {
        this.terminalId = terminalId;
    }

    @Override
    public void run() {
        while (true) {
            try {
                sleep(VirtualTerminal12.time_interval);
                VirtualTerminal12.sendMess.sendMessage(Protocol12.getLatLonStr(terminalId, VirtualTerminal12.lat, VirtualTerminal12.lon));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
