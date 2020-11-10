package com.zhangjunchao.virtual.server;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Date;

public class ServerReceiveMessThread extends Thread implements Closeable {

    Socket socket = null;

    public ServerReceiveMessThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        InputStream in = null;
        try {
            in = socket.getInputStream();
            int batch = 1024;
            byte[] buf = new byte[batch];
            int len = 0;
            int count = 1;
            byte[] data = new byte[batch];
            while (true) {
                len = in.read(buf);
                if (len == batch) {
                    if (count > 1) {
                        byte[] t = data;
                        int length = (count -1) * batch + len;
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
                    count ++;
                } else {
                    if (count > 1) {
                        byte[] t = data;
                        int length = (count -1) * batch + len;
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
                    count ++;
                    System.out.println(new Date() + "  server receive：  " + new String(data, 0, data.length, "UTF-8"));
                    count = 1;
                    data = new byte[1];
                }
                sleep(1000);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void close() throws IOException {
        socket.close();
    }
}
