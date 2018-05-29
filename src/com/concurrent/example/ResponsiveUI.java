package com.concurrent.example;

import java.io.IOException;

/**
 * P671
 * 创建有响应的用户界面
 */
class UnresponsiveUI {
    private volatile double d = 1;
    public UnresponsiveUI() throws IOException {
        while (d > 0) {
            d = d + (Math.PI + Math.E) / d ;
        }
        System.in.read();
    }
}

public class ResponsiveUI extends Thread {
    private static volatile double d = 1;
    public ResponsiveUI() {
        setDaemon(true);
        start();
    }

    @Override
    public void run() {
        while (true) {
            d = d + (Math.PI + Math.E) / d;
        }
    }

    public static void main(String[] args) throws IOException {
        //new UnresponsiveUI();
        new ResponsiveUI();
        System.in.read();
        System.out.println(d);
    }
}
