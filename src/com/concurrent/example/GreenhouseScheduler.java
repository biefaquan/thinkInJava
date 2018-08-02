package com.concurrent.example;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * P730
 * 使用SchedulerExecutor的温室控制室
 *
 */
public class GreenhouseScheduler {
    private volatile boolean light = false;
    private volatile boolean water = false;
    private String thermostat = "Day";
    public synchronized String getThermostat () {
        return thermostat;
    }
    public synchronized void setThermostat (String value) {
        thermostat = value;
    }
    ScheduledThreadPoolExecutor scheduler = new ScheduledThreadPoolExecutor(10);
    public void schedule(Runnable event, long delay) {
        scheduler.schedule(event, delay, TimeUnit.MILLISECONDS);
    }

    class LightOn implements Runnable {
        @Override
        public void run() {
            //Put hardware control code here to
            //physically turn on the light
            System.out.println("Turning on lights");
            light = true;
        }
    }

    class LightOff implements Runnable {
        @Override
        public void run() {
            //Put hardware control code here to
            //physically turn off the light
            System.out.println("Turning off lights");
            light = false;
        }
    }

    class WaterOn implements Runnable {
        @Override
        public void run() {
            //Put hardware control code here to
            System.out.println("Turning greenhouse water on");
            water = true;
        }
    }

    class WatertOff implements Runnable {
        @Override
        public void run() {
            //Put hardware control code here to
            System.out.println("Turning greenhouse water off");
            water = false;
        }
    }
}
