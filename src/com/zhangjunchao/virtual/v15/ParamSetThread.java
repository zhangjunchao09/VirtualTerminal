package com.zhangjunchao.virtual.v15;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.Closeable;
import java.io.IOException;
import java.util.Scanner;

public class ParamSetThread extends Thread implements Closeable {
    private Scanner scanner = new Scanner(System.in);

    private static Options options = new Options();

    static {
        options.addOption("x", "lat", true, "lat default 25.626133");
        options.addOption("y", "lon", true, "lon default 122.075813");
        options.addOption("t", "time_interval", true, "time_interval default 10000");
    }

    String terminalId;

    public ParamSetThread(String terminalId) {
        this.terminalId = terminalId;
    }

    @Override
    public void run() {
        while (true) {
            String position = scanner.nextLine();  //105.499054,32.911882
            try {
                String[] args = position.trim().split("\\s+");
                setOptions(args);
            } catch (Exception e) {
                System.out.println(e);
            }

        }
    }

    public void setOptions(String[] args) {
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption('x')) {
                VirtualTerminal15.lat = Double.parseDouble(cmd.getOptionValue('x'));
            }
            if (cmd.hasOption('y')) {
                VirtualTerminal15.lon = Double.parseDouble(cmd.getOptionValue('y'));
            }
            if (cmd.hasOption('t')) {
                VirtualTerminal15.time_interval = Integer.parseInt(cmd.getOptionValue('t'));
                VirtualTerminal15.terminalSendGpsThread.stop();
                VirtualTerminal15.terminalSendGpsThread = new TerminalSendGpsThread(terminalId);
                VirtualTerminal15.terminalSendGpsThread.start();
            }

        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void close() throws IOException {
        scanner.close();
    }
}
