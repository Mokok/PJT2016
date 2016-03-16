/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

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
	 *
	 * @throws java.lang.InterruptedException
	 */
	@Test
	public void testMoreThanPoolSize() throws InterruptedException {
		ThreadManager manager = new ThreadManager(6);
		//start the monitoring thread
		ThreadMonitor monitor = new ThreadMonitor(manager.getExecutor(), 2);
		Thread monitorThread = new Thread(monitor);
		monitorThread.start();
		//submit work to the thread pool
		manager.run();
		Thread.sleep(3 * 1000);
		monitor.shutdown();
		assertFalse(monitor.isRunning());
		assertTrue(manager.getExecutor().isTerminated());
	}
}
