/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import core.worker.ThreadWorker;
import core.worker.WorkerType;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Mokok
 */
public class WorkerManager implements Runnable {

    private final int poolSize;
    private ExecutorService executor;

    public WorkerManager(int maxNumberOfWorker) {
	poolSize = maxNumberOfWorker;
	executor = Executors.newFixedThreadPool(poolSize);
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public void run() {
	executor = Executors.newFixedThreadPool(5);
	for (int i = 0; i < 10; i++) {
	    //TODO: get elements in Glassfish Pool Queue
	    Runnable worker = ThreadWorker.getWorker(WorkerType.SPLIT, null);
	    executor.execute(worker);
	}
	executor.shutdown();
	while (!executor.isTerminated()) {
	}
	System.out.println("Finished all threads");
    }
}
