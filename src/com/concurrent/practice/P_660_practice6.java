package com.concurrent.practice;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.util.Fibonacci;

/**
 * P656 练习2
 * @author bfq
 *
 */
public class P_660_practice6 {
	public static void main(String[] args) {
		ExecutorService exec = Executors.newCachedThreadPool();
		for(int i = 0; i< 3; i++) {
			exec.execute(new Thread6());
		}
		exec.shutdown();
	}
}

class Thread6 implements Runnable{
	
	@Override
	public void run() {
		int random = (int) Math.floor(Math.random() * 10 + 1);
		try {
			TimeUnit.SECONDS.sleep(random);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("sleep Time: " + random);
	}
}
