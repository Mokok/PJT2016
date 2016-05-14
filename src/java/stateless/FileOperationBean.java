/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import core.ThreadCoordinator;
import core.ThreadManager;
import core.ThreadMonitor;
import core.ThreadTask;
import core.worker.ConcatTask;
import core.worker.CoreTask;
import core.worker.SplitTask;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import dao.ConfigDAO;
import entity.User;
import entity.Video;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import javax.ejb.LocalBean;

/**
 *
 * @author Anthony
 */
@Stateless
@LocalBean
public class FileOperationBean {
	
	private Video video;
	private User user;

	@EJB
	private ConfigDAO configDAO;
	
	public void init(){
		video = new Video();
		video.setExtInput("avi");
		video.setNameInput("test3");
		video.setExtOutput("mp4");
		video.setNameOutput(video.getNameInput()+"_transcoded");

		user = new User();
		user.setId(1);
		user.setFirstName("firstName");
		user.setLastName("lastName");
		video.setUser(user);
	}

	public String testComputeCmd() throws IOException {		
		System.out.println("computeCmd");		
		CoreTask task = new ConcatTask(video);
		ThreadTask worker = ThreadTask.createNewThreadTask(task);
		//task.setConfig(configDAO);
		return task.computeCmd();
	}

	public String testDuration() {		
		StringBuilder strCmd = new StringBuilder();
		strCmd.append(configDAO.getFFProbePath());
		strCmd.append(" -v error -show_entries format=duration -of default=noprint_wrappers=1:nokey=1 ");
		strCmd.append(configDAO.getPathVideoInput());
		strCmd.append("\\");
		strCmd.append(video.getUser().getId());
		strCmd.append("\\");
		strCmd.append(video.getFullNameInput());
		
		
		Runtime runtime = Runtime.getRuntime();
		StringBuilder strBld = new StringBuilder();
		try {
			Process proc = runtime.exec(strCmd.toString());
			
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

			String s;

			// read the output from the command
			System.out.println("Here is the standard output of the command:\n");
			while ((s = stdInput.readLine()) != null) {
				System.out.println(s);
				strBld.append("\n");
				strBld.append(s);
			}

			// read any errors from the attempted command
			System.err.println("Here is the standard error of the command (if any):\n");
			while ((s = stdError.readLine()) != null) {
				System.out.println(s);
				strBld.append("\n");
				strBld.append(s);
			}
		} catch (IOException ex) {
			Logger.getLogger(FileOperationBean.class.getName()).log(Level.SEVERE, null, ex);
		}
		return strBld.toString();
	}

	public void testSplit() throws InterruptedException {
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
		ThreadTask thread = ThreadTask.createNewThreadTask(task);
		ThreadCoordinator coord = new ThreadCoordinator();
		coord.videoSubmitProcessStep1(thread, manager);

		Thread.sleep(120 * 1000);
		manager.stop();
		monitor.stop();
	}
}
