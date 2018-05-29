package com.concurrent.practice;
/**
 * P656 练习1
 * @author bfq
 *
 */
public class P_656_practice1 {
	public static void main(String[] args) {
		for(int i = 0; i< 3; i++) {
			new Thread(new Thread1()).start();
		}
	}
}

class Thread1 implements Runnable{
	private static int count = 0;
	
	@Override
	public void run() {
		System.out.println(count++);
		Thread.yield();
	}
}
