package com.concurrent.example;

/**
 * P666
 * 构造器放start()来启动线程
 */
public class SimpleThread extends  Thread {
    private int countDown = 5;
    private static int theadCount = 0;
    public SimpleThread() {
        super(Integer.toString(++theadCount));
        start();
    }
    public String toString(){
        return "#" + getName() + "(" + countDown + "), ";
    }

    @Override
    public void run() {
        while(true) {
            System.out.println(this);
            if(--countDown == 0) {
                return;
            }
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i< 5; i++) {
            new SimpleThread();
        }
    }
}
