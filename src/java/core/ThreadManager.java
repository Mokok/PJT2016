/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import core.worker.CoreTask;
import core.worker.SplitTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mokok
 */
public class ThreadManager implements Runnable {

	private final int poolSize;
	private final ThreadPoolExecutor executor;

	protected ThreadManager() {
		this(10);
	}

	public ThreadManager(int maxNumberOfWorker) {
		poolSize = maxNumberOfWorker;
		//RejectedExecutionHandler implementation
		RejectedExecutionHandlerImpl rejectionHandler = new RejectedExecutionHandlerImpl();
		//Get the ThreadFactory implementation to use
		ThreadFactory threadFactory = Executors.defaultThreadFactory();
		//creating the ThreadPoolExecutor
		executor = new ThreadPoolExecutor(poolSize, poolSize, 60 * 2, TimeUnit.SECONDS, new ArrayBlockingQueue<>(50, true), threadFactory, rejectionHandler);
	}

	public int getPoolSize() {
		return poolSize;
	}

	ThreadPoolExecutor getExecutor() {
		return executor;
	}

	/** Warning !
	 * 
	 */
	@Override
	public void run() {
		ThreadTask task;
		for (int i = 0; i < 10; i++) {
			//TODO: get elements from Glassfish Pool Queue
			task = ThreadTask.createNewThreadTask(new CoreTask());
			addSpecTask(task);
		}
		//stop();
	}

	public void addSpecTask(ThreadTask task) {
		executor.execute(task);
	}
	
	public void addTask(ThreadTask task) {
		executor.execute(task);
		if(task.getTask() instanceof SplitTask){
			//if()
		}
	}

	public void stop() {
		executor.shutdown();
		while (!executor.isTerminated()) {
			try {
				Thread.sleep(3 * 1000);
			} catch (InterruptedException ex) {
				Logger.getLogger(ThreadManager.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		System.out.println("Finished all threads");
	}
}
