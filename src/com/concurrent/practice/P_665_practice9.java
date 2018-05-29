package com.concurrent.practice;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * 通过
 */
public class P_665_practice9 implements Runnable {
	private int countDown = 5;
	private volatile double d;
	private int priority;
	public P_665_practice9(int priority) {
		this.priority = priority;
	}
	
	@Override
	public String toString() {
		return Thread.currentThread() + ": " + countDown;
	}
	
	@Override
	public void run() {
		if(priority > 1) {
			Thread.currentThread().setPriority(priority);
		}
		while(true) {
			for (int i = 0; i < 100000; i++) {
				d += (Math.PI + Math.E) / (double)i;
				if(i % 1000 == 0) {
					Thread.yield();
				}
			}
			System.out.println(this);
			if(--countDown == 0) {
				return;
			}
		}
	}
	
	public static void main(String[] args) {
		ExecutorService exec = Executors.newCachedThreadPool((r)-> {
				Thread t = new Thread(r);
				t.setPriority(3);
				return  t;
		});
		for (int i = 0; i < 5; i++) {
			exec.execute(new P_665_practice9(Thread.MIN_PRIORITY));
		}
		exec.execute(new P_665_practice9(Thread.MAX_PRIORITY));
		exec.shutdown();
	}
}
