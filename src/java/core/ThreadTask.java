/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import core.exception.InvalidPreviousThreadTaskException;
import core.worker.ConcatTask;
import core.worker.CoreTask;
import core.worker.SplitTask;
import core.worker.TaskStatus;
import core.worker.TranscodeTask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mokok
 */
public final class ThreadTask extends Thread {

	private Process proc;
	private CoreTask task;
	private List<ThreadTaskEndListener> listeners;

	public ThreadTask() {
		super();
		listeners = new ArrayList<>();
	}

	public ThreadTask(CoreTask task) {
		this();
		insertTask(task);
	}

	public void insertTask(CoreTask task) {
		this.task = task;
		//task.setConfig(config);
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
			try {
				Thread.sleep((new Random().nextInt(5) + 3) * 1000L);
			} catch (InterruptedException ex) {
				Logger.getLogger(ThreadTask.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		System.out.println("Stop  : " + Thread.currentThread().getName() + " Type : " + task.getClass().getName());
	}

	private void processTask() {
		/*try {
			Runtime runtime = Runtime.getRuntime();
			
			proc = runtime.exec(this.getTask().computeCmd());
			//listen(proc);
			
			proc.waitFor();
			for (ThreadTaskEndListener listener : listeners) {
				listener.processFinished(this);
			}
		} catch (IOException | InterruptedException | InvalidPreviousThreadTaskException ex) {
			this.getTask().setStatus(TaskStatus.CANCELLED);
			Logger.getLogger(ThreadTask.class.getName()).log(Level.SEVERE, null, ex);
		}
		 */
		Thread th;
		try {
			th = new Thread(new SSHExecutor(this.getTask().computeCmd()));
			th.start();
			th.join();
			for (ThreadTaskEndListener listener : listeners) {
				listener.processFinished(this);
			}
		} catch (IOException | InvalidPreviousThreadTaskException | InterruptedException ex) {
			this.getTask().setStatus(TaskStatus.CANCELLED);
			Logger.getLogger(ThreadTask.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void listenProc(Process proc) {
		try {
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

	Process getProc() {
		return proc;
	}

	public void addListener(ThreadTaskEndListener listener) {
		listeners.add(listener);
	}

	public void removeListener(ThreadTaskEndListener listener) {
		listeners.remove(listener);
	}
}
