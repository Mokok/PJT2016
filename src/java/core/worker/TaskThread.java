/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.worker;

import dao.ConfigDAO;
import entity.Video;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import utils.FileUtils;

/**
 *
 * @author Mokok
 */
@Stateless
public class TaskThread implements Runnable {

	@EJB
	private ConfigDAO configDAO;

	private static final String CONCAT_OPT = "";
	private static final String SPLIT_OPT = "";
	private static final String TRANSCODE_OPT = "";

	private WorkerType cmdType = WorkerType.UNDEFINED;
	private Video videoTask;
	private String cmd;//ffmpeg -i input.mp4 -f segment -segment_times 10,20 -c copy -map 0 output02%d.mp4";

	public TaskThread() {
	}

	private TaskThread(WorkerType type, Video task) {
		setVideoTask(task);
		setCmdType(type);
	}

	public static TaskThread getWorker(WorkerType type, Video task) {
		TaskThread result = new TaskThread(type, task);
		result.setVideoTask(task);
		result.setCmdType(type);
		try {
			result.computeCmd();
		} catch (IOException | UndefinedThreadMorkerTypeException ex) {
			result = null;
			Logger.getLogger(TaskThread.class.getName()).log(Level.SEVERE, null, ex);
		}
		return result;
	}

	private WorkerType getCmdType() {
		return cmdType;
	}

	private void setCmdType(WorkerType cmdType) {
		this.cmdType = cmdType;
	}

	private Video getVideoTask() {
		return videoTask;
	}

	private void setVideoTask(Video videoTask) {
		this.videoTask = videoTask;
	}

	@Override
	public void run() {
		System.out.println(Thread.currentThread().getName() + " Start. Command = " + cmd);
		try {
			processCommand();
		} catch (UndefinedThreadMorkerTypeException ex) {
			Logger.getLogger(TaskThread.class.getName()).log(Level.SEVERE, null, ex);
		}
		System.out.println(Thread.currentThread().getName() + " End.");
	}

	private void processCommand() throws UndefinedThreadMorkerTypeException {
		if (getCmdType() == WorkerType.UNDEFINED) {
			throw new UndefinedThreadMorkerTypeException();
		}
		try {
			Thread.sleep(5000);
		} catch (InterruptedException ex) {
			Logger.getLogger(TaskThread.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private String computeCmd() throws UndefinedThreadMorkerTypeException, IOException {
		StringBuilder strBld = new StringBuilder();
		//start the string command with the path to ffmpeg
		switch (getCmdType()) {
			case SPLIT:
				strBld.append(configDAO.getFFMPEGPath());
				strBld.append(computeSplitCmd());
				break;
			case TRANSCODE:
				strBld.append(configDAO.getFFMPEGPath());
				strBld.append(computeTranscodeCmd());
				break;
			case CONCAT:
				strBld.append(configDAO.getFFMPEGPath());
				strBld.append(computeConcatCmd());
				break;
			case TEST:
				strBld.setLength(0);
				strBld.append(computeTestCmd());
				break;
			default:
				strBld.setLength(0);
				throw new UndefinedThreadMorkerTypeException();
		}
		cmd = strBld.toString();
		return cmd;
	}

	private String computeSplitCmd() throws FileNotFoundException, IOException {
		StringBuilder strBld = new StringBuilder();
		//add path to source file
		strBld.append("-i ");
		StringBuilder path = new StringBuilder();
		path.append(configDAO.getPathVideoInput());
		path.append(getVideoTask().getUser().getId());
		path.append("\\");
		path.append(getVideoTask().getFullNameInput());
		if (!new File(path.toString()).isFile()) {
			throw new FileNotFoundException(path.toString());
		}
		strBld.append(path);
		path.setLength(0);
		strBld.append(" ");
		//add split-specific options
		strBld.append(SPLIT_OPT);
		strBld.append(" ");
		//add path to output
		path.setLength(0);
		path.append(configDAO.getPathVideoSplittedInput());
		path.append(getVideoTask().getUser().getId());
		path.append("\\");
		path.append(getVideoTask().getNameInput());
		path.append("%4d.");
		path.append(getVideoTask().getExtInput());
		//create directories if not exist
		FileUtils.resetFolder(new File(path.toString()));
		strBld.append(path);
		path.setLength(0);
		return strBld.toString();
	}

	private String computeTranscodeCmd() throws FileNotFoundException, IOException {
		StringBuilder strBld = new StringBuilder();
		//add path to source file
		strBld.append("-i ");
		StringBuilder path = new StringBuilder();
		path.append(configDAO.getPathVideoSplittedInput());
		path.append(getVideoTask().getUser().getId());
		path.append("\\");
		path.append(getVideoTask().getFullNameInput());
		if (!new File(path.toString()).isFile()) {
			throw new FileNotFoundException(path.toString());
		}
		strBld.append(path);
		path.setLength(0);
		strBld.append(" ");
		//add split-specific options
		strBld.append(TRANSCODE_OPT);
		strBld.append(" ");
		//add path to output
		path.setLength(0);
		path.append(configDAO.getPathVideoSplittedOutput());
		path.append(getVideoTask().getUser().getId());
		path.append("\\");
		path.append(getVideoTask().getFullNameInput());
		//create directories if not exist
		FileUtils.resetFolder(new File(path.toString()));
		strBld.append(path);
		path.setLength(0);
		return strBld.toString();
	}

	private String computeConcatCmd() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	private String computeTestCmd() {
		return "echo \" CECI EST UN TEST\"";
	}
}
