/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import core.ThreadManager;
import core.ThreadMonitor;
import core.ThreadTask;
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
import java.io.File;
import java.io.InputStreamReader;
import javax.ejb.LocalBean;
import utils.FileUtils;

/**
 *
 * @author Anthony
 */
@Stateless
@LocalBean
public class FileOperationBean {

	private String ffmpegPath;
	private String inputFile;
	private String outputFile;
	private String splittedFileInput;
	private String splittedFileOutput;

	@EJB
	private ConfigDAO configDAO;

	public void testFFmpeg(Video video) {
		try {
			this.prepareSplit(video);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}

		final String[] cmd = new String[]{ffmpegPath,
			"-i",
			inputFile,
			"-f",
			"segment",
			"-segment_time",
			String.valueOf(configDAO.getMaxSplitTime()),
			"-codec",
			"copy",
			"-map",
			"0",
			splittedFileInput};

		Runtime runtime = Runtime.getRuntime();
		try {
			Process proc = runtime.exec(cmd);

			BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

			String s;

			// read the output from the command
			System.out.println("Here is the standard output of the command:\n");
			while ((s = stdInput.readLine()) != null) {
				System.out.println(s);
			}

			// read any errors from the attempted command
			System.err.println("Here is the standard error of the command (if any):\n");
			while ((s = stdError.readLine()) != null) {
				System.out.println(s);
			}
		} catch (IOException ex) {
			Logger.getLogger(FileOperationBean.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * Prepares strings such as
	 * inputFile,outputFile,splittedFileInput,splittedFileOutput
	 *
	 * @param video
	 */
	private void prepareSplit(Video video) throws IOException {
		StringBuilder strBld = new StringBuilder();
		File tempFile;

		ffmpegPath = configDAO.getFFMPEGPath();

		//path : VideoInput innitiatl source, waitting for split
		strBld.append(configDAO.getPathVideoInput());
		strBld.append(video.getUser().getId());
		strBld.append("\\");
		strBld.append(video.getFullNameInput());
		inputFile = configDAO.getPathVideoInput() + video.getUser().getId() + "\\" + video.getFullNameInput();
		tempFile = new File(inputFile);
		if (!tempFile.isFile()) {
			throw new IOException("Unreachable source file (" + inputFile + ")");
		}

		//path VideoOutput (concated and transcoded)
		strBld.setLength(0);
		strBld.append(configDAO.getPathVideoOutput());
		strBld.append(video.getUser().getId());
		//remove if exists
		tempFile = new File(strBld.toString());
		FileUtils.delete(tempFile);
		//created folder path
		tempFile.mkdirs();
		strBld.append("\\");
		strBld.append(video.getFullNameInput());
		outputFile = configDAO.getPathVideoOutput() + video.getUser().getId() + "\\" + video.getFullNameInput();

		//path : VideoSplitted\input (cut, but waitting for transcode
		strBld.setLength(0);
		strBld.append(configDAO.getPathVideoSplittedInput());
		strBld.append(video.getUser().getId());
		//remove if exists
		tempFile = new File(strBld.toString());
		FileUtils.delete(tempFile);
		//created folder path
		tempFile.mkdirs();
		strBld.append("\\");
		strBld.append(video.getNameInput());
		strBld.append("%4d.");
		strBld.append(video.getExtInput());
		splittedFileInput = strBld.toString();

		//path : VideoSplited\Output cut and transcoded, waitting for concat
		strBld.setLength(0);
		strBld.append(configDAO.getPathVideoSplittedOutput());
		strBld.append(video.getUser().getId());
		//remove if exists
		tempFile = new File(strBld.toString());
		FileUtils.delete(tempFile);
		//created folder path
		tempFile.mkdirs();
		strBld.append("\\");
		strBld.append(video.getNameOutput());
		strBld.append("%4d.");
		strBld.append(video.getExtOutput());
		splittedFileOutput = strBld.toString();

		strBld.setLength(0);
		strBld = null;
	}

	public String test() throws IOException {
		Video video = new Video();
		video.setExtInput("mkv");
		video.setNameInput("test2");
		video.setExtOutput("mp4");
		video.setNameOutput("test-transcoded");

		User user = new User();
		user.setId(1);
		user.setFirstName("firstName");
		user.setLastName("lastName");
		video.setUser(user);
		
		
		System.out.println("computeCmd");		
		CoreTask task = new SplitTask(video);
		ThreadTask worker = ThreadTask.createNewThreadTask(task);
		//task.setConfig(configDAO);
		return task.computeCmd();
	}

	public String testDuration() {
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
		Video video = new Video();
		video.setExtInput("avi");
		video.setNameInput("test2");
		video.setExtOutput("mp4");
		video.setNameOutput(video.getNameInput()+"_transcoded");

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
		manager.addTask(worker);

		Thread.sleep(3 * 1000);
		manager.stop();
		monitor.stop();
	}

	
}
