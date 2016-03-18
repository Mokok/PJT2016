/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import dao.ConfigDAO;
import entity.Video;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import utils.FileUtils;

/**
 *
 * @author Anthony
 */
@Stateless
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
			String.valueOf(configDAO.getSplitTime()),
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

}
