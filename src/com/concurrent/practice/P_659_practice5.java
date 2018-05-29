package com.concurrent.practice;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.util.Fibonacci;

/**
 * P656 练习2
 * @author bfq
 *
 */
public class P_659_practice5 {
	public static void main(String[] args) {
		ExecutorService exec = Executors.newCachedThreadPool();
		ArrayList<Future<String>> result = new ArrayList<Future<String>>();
		for(int i = 0; i< 3; i++) {
			result.add(exec.submit(new Thread5(5)));
		}
		for (Future<String> future : result) {
			try {
				System.out.println("Future value:" + future.get() + " end!!");
			} catch (InterruptedException e) {
				System.out.println(e);
				return;
			} catch (ExecutionException e) {
				System.out.println(e); 
			} finally {
				exec.shutdown();
			}
		}
	}
}

class Thread5 implements Callable<String>{
	private int n = 1;
	
	public Thread5() {}
	
	public Thread5(int n) {
		this.n = n;
	}
	
	@Override
	public String call() {
		Fibonacci gen = new Fibonacci();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < n; i++) {
			sb.append(gen.next() + "\t");
		}
		Thread.yield();
		return sb.toString();
	}
}
