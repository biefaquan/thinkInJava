package com.concurrent.example;

import java.util.concurrent.atomic.LongAdder;

public class BasicThreads {
	public static void main(String[] args) {
		Thread t = new Thread(new Liftoff());
		t.start();
		System.out.println("Waiting for LiftOff");
	}
}
