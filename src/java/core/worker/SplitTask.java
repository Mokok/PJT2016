/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.worker;

import dao.ConfigDAO;
import entity.Video;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import stateless.FileOperationBean;

/**
 *
 * @author Mokok
 */
@Stateless
@LocalBean
public class SplitTask extends CoreTask {
	
	@EJB
	private ConfigDAO config;
		
	private static final String OPTIONS = "-f segment -segment_times ";
	private static final String OPTIONS2 = " -c copy -map 0 -segment_list ";

	public SplitTask() {
	}
	
	public SplitTask(Video video) {
		super(video);
	}

	/**
	 * [ffmpeg] -i [inputPath] -f segment -segment_times 10,20,30 -map 0 -vcodec
	 * copy -acodec copy -segment_list [listFileName] [outputPath]
	 *
	 * @return complete string command, ready to be executed
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	@Override
	public String computeCmd() throws IOException {
		StringBuilder strBld = new StringBuilder();
		//add ffmpeg exe path
		strBld.append(config.getFFMPEGPath());
		//add path to source file
		strBld.append(" -i ");
		{
			StringBuilder inPath = new StringBuilder();
			inPath.append(config.getPathVideoInput());
			inPath.append(this.getVideo().getUser().getId());
			inPath.append("\\");
			inPath.append(getVideo().getFullNameInput());
			if (!new File(inPath.toString()).isFile()) {
				throw new FileNotFoundException(inPath.toString());
			}
			strBld.append(inPath.toString());
		}
		strBld.append(" ");
		//add split-specific options (times)
		{
			String timers = computeSplitTimers();
			if(!timers.equals("")){
				strBld.append(OPTIONS);
				strBld.append(computeSplitTimers());
				strBld.append(" ");
			}
		}
		//add split-specific options (list)
		strBld.append(OPTIONS2);
		strBld.append(" ");
		//add option for list-file creation
		{
			StringBuilder listPath = new StringBuilder();
			listPath.append(config.getPathVideoSplittedInput());
			listPath.append(getVideo().getUser().getId());
			listPath.append("\\");
			listPath.append(getVideo().getFullNameInput());
			listPath.append("\\");
			new File(listPath.toString()).mkdirs();
			listPath.append(config.getListFileName());
			strBld.append(listPath.toString());
		}
		strBld.append(" ");
		//add path to output
		{
			StringBuilder outPath = new StringBuilder();
			outPath.append(config.getPathVideoSplittedInput());
			outPath.append(getVideo().getUser().getId());
			outPath.append("\\");
			outPath.append(getVideo().getNameInput());
			outPath.append("\\");
			outPath.append(getVideo().getNameInput());
			outPath.append("%4d.");
			outPath.append(getVideo().getExtInput());

			strBld.append(outPath.toString());
		}
		return strBld.toString();
	}

	/**
	 * Max : config.maxSplitTime | Min : config.minSplitTimeDuration
	 * @return list of times to split 
	 */
	private String computeSplitTimers(){
		StringBuilder timers = new StringBuilder();
		int maxSplitTime = config.getMaxSplitTime();
		int minSplitTimeDuration = config.getMinSplitTimeDuration();
		int videoDuration = getVideoDuration();
		int numberOfSlice = Integer.min(maxSplitTime, videoDuration/minSplitTimeDuration);
		
		if(numberOfSlice != 0){
			int splitDuration = videoDuration/numberOfSlice;
			timers.append(splitDuration);
			for(int i = 2 ; i < numberOfSlice ; i++){
				timers.append(",");
				timers.append(String.valueOf(splitDuration*i));
			}
		}
				
		return timers.toString();
	}
	
	private int getVideoDuration() {
		StringBuilder strCmd = new StringBuilder();
		strCmd.append(config.getFFProbePath());
		strCmd.append(" -v error -show_entries format=duration -of default=noprint_wrappers=1:nokey=1 ");
		strCmd.append(config.getPathVideoInput());
		strCmd.append("\\");
		strCmd.append(getVideo().getUser().getId());
		strCmd.append("\\");
		strCmd.append(getVideo().getFullNameInput());
		
		
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
}
