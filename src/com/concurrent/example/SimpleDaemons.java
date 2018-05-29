package com.concurrent.example;

import java.util.concurrent.TimeUnit;
/**
 * P662
 * @author bfq
 *
 */
public class SimpleDaemons implements Runnable {

	@Override
	public void run() {
		try {
			while(true) {
				TimeUnit.MILLISECONDS.sleep(100);
				System.out.println(Thread.currentThread() + " " + this);
			}
		} catch (InterruptedException e) {
			System.out.println("sleep() interrupted");
		}
	}
	
	public static void main(String[] args) throws Exception {
		for (int i = 0; i < 10; i++) {
			Thread t = new Thread(new SimpleDaemons());
			t.setDaemon(true);
			t.start();
		}
		System.out.println("All daemons started");
		TimeUnit.MILLISECONDS.sleep(175);
	}

}
