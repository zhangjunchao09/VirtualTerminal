package com.zhangjunchao.virtual;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {

    public static void main(String[] args) {
        ServerSocket socket = null;
        try {
            socket = new ServerSocket(2603);
            while (true) {
                ServerReceiveMessThread receiveMessThread = new ServerReceiveMessThread(socket.accept());
                receiveMessThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
