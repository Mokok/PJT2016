/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import core.worker.SplitTask;
import core.worker.TranscodeTask;
import entity.Video;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import stateless.FileOperationBean;
import stateless.LocalConfig;

/**
 *
 * @author Mokok
 */
public abstract class WorkerUtils {

	/**
	 *
	 * @param video
	 * @return the video duration
	 */
	public static int getVideoDuration(Video video) {
		StringBuilder strCmd = new StringBuilder();
		strCmd.append(LocalConfig.getFFProbePath());
		strCmd.append(" -v error -show_entries format=duration -of default=noprint_wrappers=1:nokey=1 ");
		strCmd.append(LocalConfig.getPathVideoInput());
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
				strBld.append(s);
				strBld.append("\n");
			}

			// read any errors from the attempted command
			System.err.println("Here is the standard error of the command (if any):\n");
			while ((s = stdError.readLine()) != null) {
				System.out.println(s);
				strBld.append(s);
				strBld.append("\n");
			}
		} catch (IOException ex) {
			Logger.getLogger(FileOperationBean.class.getName()).log(Level.SEVERE, null, ex);
		}
		return (int) Math.floor(Float.valueOf(strBld.toString().trim()));
	}

	static File reformatList(Video video) throws FileNotFoundException {
		StringBuilder strBld = new StringBuilder();
		//get the list file
		File file;
		{
			strBld.append(LocalConfig.getPathVideoSplittedInput());
			strBld.append(video.getUser().getId());
			strBld.append("\\");
			strBld.append(video.getNameInput());
			strBld.append("\\");
			strBld.append(LocalConfig.getListFileName());
			file = new File(strBld.toString());
			if (!file.exists()) {
				throw new FileNotFoundException(strBld.toString());
			}
		}
		//empty the StringBuilder
		strBld.setLength(0);
		//Reading the file
		{
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			try {
				String line = br.readLine();
				while (line != null) {
					strBld.append(line);
					strBld.append("\n");
					line = br.readLine();
				}
				fr.close();
			} catch (FileNotFoundException e) {
				System.out.println("File was not found!");
			} catch (IOException ex) {
				Logger.getLogger(SplitTask.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		//Modifying the file
		StringBuffer result = new StringBuffer();
		{
			String fileStr = strBld.toString();
			strBld.setLength(0);
			//reconstruct file path
			strBld.append(LocalConfig.getPathVideoSplittedInput());
			strBld.append(video.getUser().getId());
			strBld.append("\\");
			strBld.append(video.getNameInput());
			strBld.append("\\");
			//replace
			Pattern pattern = Pattern.compile("(file )([a-zA-Z0-9_.-]+\\.\\w{3,5})[\\s\n]+");
			Matcher matcher = pattern.matcher(fileStr);
			String temp = strBld.toString().replaceAll("\\\\", "/");
			while (matcher.find()) {
				matcher.appendReplacement(result, matcher.group(1) + temp + matcher.group(2) + "\n");
			}
		}
		//Writting the file
		{
			PrintWriter pr = new PrintWriter(file);
			pr.print("");
			pr.append(result.toString());
			pr.flush();
		}
		return file;
	}

	static List<TranscodeTask> generateListOfTranscodeTasks(final Video video) throws FileNotFoundException {
		File fileList = reformatList(video);
		ArrayList<TranscodeTask> list = new ArrayList<>();

		//read the fileList and create dedicated TranscodeTask
		{
			StringBuilder strBld = new StringBuilder();
			//Read the fileList
			{
				FileReader fr = new FileReader(fileList);
				BufferedReader br = new BufferedReader(fr);
				try {
					String line = br.readLine();
					while (line != null) {
						strBld.append(line);
						strBld.append("\n");
						line = br.readLine();
					}
					fr.close();
				} catch (FileNotFoundException e) {
					System.out.println("File was not found!");
				} catch (IOException ex) {
					Logger.getLogger(SplitTask.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
			Pattern pattern = Pattern.compile("file ([A-Z]:[a-zA-Z0-9_.-\\\\/]+)\\.\\w{3,5}[\\s\\n]+");
			Matcher matcher = pattern.matcher(strBld.toString());
			String originalName = video.getNameInput();
			String tempName;
			Video tempVideo = video.clone();
			//store the original nameInput to be use to compute storage path with the name on the video
			tempVideo.setNameOutput(originalName);
			TranscodeTask tTask;
			while (matcher.find()) {
				tempName = matcher.group(1);
				tempName = Paths.get(tempName).getFileName().toString();
				tempVideo.setNameInput(tempName);
				tTask = new TranscodeTask(tempVideo);
				list.add(tTask);

				//Create a new instance of video for the next task
				tempVideo = tempVideo.clone();
			}
		}
		return list;
	}
}
