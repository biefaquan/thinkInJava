package com.concurrent.example;

/**
 * P682
 * 产生序列数字
 */
public class SerialNumberGenerator {
    private static volatile int serialNumber = 0;
    public static int nextSerialNumber() {
        return serialNumber ++; //Not thread-safe
    }
}
