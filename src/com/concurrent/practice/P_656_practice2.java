package com.concurrent.practice;

import com.util.Fibonacci;

/**
 * P656 练习2
 * @author bfq
 *
 */
public class P_656_practice2 {
	public static void main(String[] args) {
		for(int i = 0; i< 3; i++) {
			new Thread(new Thread2(10)).start();
		}
	}
}

class Thread2 implements Runnable{
	private int n = 1;
	
	public Thread2() {}
	
	public Thread2(int n) {
		this.n = n;
	}
	
	@Override
	public void run() {
		Fibonacci gen = new Fibonacci();
		for (int i = 0; i < n; i++) {
			System.out.print(gen.next() + "\t");
		}
		Thread.yield();
	}
}
