/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import core.worker.ThreadWorker;
import core.worker.WorkerType;
import entity.Video;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Mokok
 */
public class CoreTest {

    public CoreTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of shutdown method, of class WorkerMonitor.
     * @throws java.lang.InterruptedException
     */
    @Test
    public void testShutdown() throws InterruptedException {
	//RejectedExecutionHandler implementation
	RejectedExecutionHandlerImpl rejectionHandler = new RejectedExecutionHandlerImpl();
	//Get the ThreadFactory implementation to use
	ThreadFactory threadFactory = Executors.defaultThreadFactory();
	//creating the ThreadPoolExecutor
	ThreadPoolExecutor executorPool = new ThreadPoolExecutor(2, 4, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<>(2), threadFactory, rejectionHandler);
	//start the monitoring thread
	WorkerMonitor monitor = new WorkerMonitor(executorPool, 2);
	Thread monitorThread = new Thread(monitor);
	monitorThread.start();
	//submit work to the thread pool
	for (int i = 0; i < 10; i++) {
	    executorPool.execute(ThreadWorker.getWorker(WorkerType.TEST, new Video()));
	}

	Thread.sleep(30000);
	//shut down the pool
	executorPool.shutdown();
	//shut down the monitor thread
	Thread.sleep(5000);
	monitor.shutdown();

	assertFalse(monitor.isRunning());
    }
}
