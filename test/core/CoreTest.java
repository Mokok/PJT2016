/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import core.worker.CoreTask;
import core.worker.SplitTask;
import entity.User;
import entity.Video;
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
	public void testPoolBehavior() throws InterruptedException {
		ThreadManager manager = new ThreadManager(6);
		//start the monitoring thread
		ThreadMonitor monitor = new ThreadMonitor(manager.getExecutor(), 2);
		Thread monitorThread = new Thread(monitor);
		monitorThread.start();
		//submit work to the thread pool
		manager.run();
		Thread.sleep(3 * 1000);
		manager.stop();
		monitor.stop();
		assertFalse(monitor.isRunning());
		assertTrue(manager.getExecutor().isTerminated());
	}

	@Test
	public void testSplit() throws InterruptedException {
		Video video = new Video();
		video.setExtInput("avi");
		video.setNameInput("test");
		video.setExtOutput("mp4");
		video.setNameOutput("test-transcoded");

		User user = new User();
		user.setId(1);
		user.setFirstName("firstName");
		user.setLastName("lastName");
		video.setUser(user);

		ThreadManager manager = new ThreadManager(6);
		//start the monitoring thread
		ThreadMonitor monitor = new ThreadMonitor(manager.getExecutor(), 2);
		Thread monitorThread = new Thread(monitor);
		monitorThread.start();
		//submit work to the thread pool
		manager.run();

		Thread.sleep(3 * 1000);

		//add the video-to-split-test
		CoreTask task = new SplitTask(video);
		ThreadTask worker = ThreadTask.createNewThreadTask(task);
		manager.addSpecTask(worker);

		Thread.sleep(3 * 1000);
		manager.stop();
		monitor.stop();
		//assertTrue("no crash",true);
	}
}
