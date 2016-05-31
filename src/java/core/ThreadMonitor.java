/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mokok
 */
public class ThreadMonitor implements Runnable {

	private final ThreadPoolExecutor executor;
	private final int seconds;
	private boolean run = true;

	/**
	 *
	 * @param executor from the #ThreadManager
	 * @param delay gap in seconds between 2 calls for pool's informations
	 */
	public ThreadMonitor(ThreadPoolExecutor executor, int delay) {
		this.executor = executor;
		this.seconds = delay;
	}

	/**
	 * call for stoping the monitor
	 */
	public void stop() {
		this.run = false;
	}

	/**
	 *
	 * @return true if monitor is running, false otherwise
	 */
	public boolean isRunning() {
		return this.run;
	}

	@Override
	public void run() {
		while (run) {
			System.out.println(
					String.format("[monitor] [%d/%d] Active: %d, Completed: %d, Task: %d, isShutdown: %s, isTerminated: %s",
							this.executor.getPoolSize(),
							this.executor.getCorePoolSize(),
							this.executor.getActiveCount(),
							this.executor.getCompletedTaskCount(),
							this.executor.getTaskCount(),
							this.executor.isShutdown(),
							this.executor.isTerminated()
					));
			try {
				Thread.sleep(seconds * 1000L);
			} catch (InterruptedException ex) {
				Logger.getLogger(ThreadMonitor.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
}
