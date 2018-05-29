package com.concurrent.example;

import java.util.concurrent.ThreadFactory;

/**
 * P663
 * ThreadFactory定制由Executor创建的线程的属性（后台、优先级、名称）
 */
public class DaemonThreadFactory implements ThreadFactory {

	@Override
	public Thread newThread(Runnable r) {
		Thread t = new Thread(r);
		t.setDaemon(true);
		return  t;
	}

}
