/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import core.worker.ConcatTask;
import core.worker.CoreTask;
import core.worker.SplitTask;
import core.worker.TranscodeTask;
import dao.ConfigDAO;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Mokok
 */
@Stateless
public class ThreadTask implements Runnable {

	@EJB
	public ConfigDAO configDAO;

	private CoreTask task;

	protected ThreadTask() {
	}

	private ThreadTask(CoreTask task) {
		this();
		this.task = task;
	}

	public static ThreadTask createNewThreadTask(CoreTask task) {
		ThreadTask result = new ThreadTask(task);
		return result;
	}

	public CoreTask getTask() {
		return task;
	}

	@Override
	public void run() {
		System.out.println("Start : " + Thread.currentThread().getName() + " Type : " + task.getClass().getName());
		if (getTask() instanceof SplitTask || getTask() instanceof TranscodeTask || getTask() instanceof ConcatTask) {
			processTask();
		} else {
			System.out.println("Info  : running");
		}
		try {
			Thread.sleep((new Random().nextInt(5) + 3) * 1000L);
		} catch (InterruptedException ex) {
			Logger.getLogger(ThreadTask.class.getName()).log(Level.SEVERE, null, ex);
		}
		System.out.println("Stop  : " + Thread.currentThread().getName() + " Type : " + task.getClass().getName());
	}

	private void processTask() {
		Runtime runtime = Runtime.getRuntime();
		try {
			Process proc = runtime.exec(this.getTask().toString());

			BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

			String s;
			// read the output from the command
			while ((s = stdInput.readLine()) != null) {
				System.out.println("Std : " + s);
			}

			// read any errors from the attempted command
			while ((s = stdError.readLine()) != null) {
				System.err.println("Err : " + s);
			}
		} catch (IOException ex) {
			Logger.getLogger(ThreadTask.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
