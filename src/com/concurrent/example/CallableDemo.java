package com.concurrent.example;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class ClassWithResult implements Callable<String> {
	private int id;
	public ClassWithResult(int id) {
		this.id = id;
	}
	
	@Override
	public String call() throws Exception {
		return "result of TaskWithResult " + id;
	}
}

public class CallableDemo{
	public static void main(String[] args) {
		ExecutorService exec = Executors.newCachedThreadPool();
		ArrayList<Future<String>> result = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			result.add(exec.submit(new ClassWithResult(i)));
		}
		for (Future<String> future : result) {
			try {
				System.out.println(future.get());
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
