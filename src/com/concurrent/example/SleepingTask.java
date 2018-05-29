package com.concurrent.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SleepingTask extends Liftoff {
	@Override
	public void run() {
		while(countDown-- > 0) {
			System.out.print(status());
			try {
				TimeUnit.MICROSECONDS.sleep(100);
			} catch (InterruptedException e) {
				System.out.println("Interrupted");
			}
		}
	}
	
	public static void main(String[] args) {
		ExecutorService exec = Executors.newCachedThreadPool();
		for (int i = 0; i < 5; i++) {
			exec.execute(new SleepingTask());
		}
		exec.shutdown();
	}
}
