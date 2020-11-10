package com.zhangjunchao.virtual;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class VirtualTerminal12 implements Closeable {
    private static String terminalId = "157D520070002";
    private static String ip = "10.39.52.67";
    private static int port = 2603;
    public static double lat = 25.626133;
    public static double lon = 122.075813;
    public static int time_interval = 10000;
    public static Socket socket = new Socket();
    public static OutputStream os;
    public static InputStream in;

    private static Options options = new Options();

    static {
        options.addRequiredOption("t", "terminalNo", true, "terminalNo default 157D520070002");
        options.addRequiredOption("h", "host", true, "gateway ip 10.39.52.67");
        options.addRequiredOption("p", "port", true, "gateway port, default 2603");

        options.addOption("x", "lat", true, "lat default 25.626133");
        options.addOption("y", "lon", true, "lon default 122.075813");
    }

    public static void main(String[] args) {
        setOptions(args);
        try {
            socket.connect(new InetSocketAddress(ip, port), 2000);
            os = socket.getOutputStream();
            in = socket.getInputStream();
            SendMess sendMess = new SendMess();

            new ReceiveMessThread(sendMess).start();

            sendMess.sendMessage(Protocol12.getLoginStr(terminalId));

            TerminalSendGpsThread terminalSendGpsThread = new TerminalSendGpsThread(terminalId, sendMess);
            terminalSendGpsThread.start();

            new ParamSetThread().start();
            while (terminalSendGpsThread.isAlive()) {

            }
            System.out.println("退出");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void setOptions(String[] args) {
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            terminalId = cmd.getOptionValue('t');
            ip = cmd.getOptionValue('h');
            port = Integer.parseInt(cmd.getOptionValue('p'));
            if (cmd.hasOption('x')) {
                lat = Double.parseDouble(cmd.getOptionValue('x'));
            }
            if (cmd.hasOption('y')) {
                lon = Double.parseDouble(cmd.getOptionValue('y'));
            }

        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void close() throws IOException {
        in.close();
        os.close();
        socket.close();
    }
}
