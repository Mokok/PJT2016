/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.worker;

import entity.Video;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
public class SplitTask extends CoreTask {
	
	private static final String OPTIONS = "-f segment ";
	private static final String OPTIONS2 = " -segment_list ";
	private static final String OPTIONS3 = " -reset_timestamps 1 -c copy -map 0 ";

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
		strBld.append(LocalConfig.getFFMPEGPath());
		//add path to source file
		strBld.append(" -y -i ");
		{
			StringBuilder inPath = new StringBuilder();
			inPath.append(LocalConfig.getPathVideoInput());
			inPath.append(this.getVideo().getUser().getId());
			inPath.append("\\");
			inPath.append(getVideo().getFullNameInput());
			if (!new File(inPath.toString()).isFile()) {
				throw new FileNotFoundException(inPath.toString());
			}
			strBld.append(inPath.toString());
		}
		strBld.append(" ");
		strBld.append(OPTIONS);
		//add split segment time duration
		{
			strBld.append("-segment_time ");
			strBld.append(computeSplitTimer());
		}
		//add split-specific options (times)
		/*{
			
			String timers = computeSplitTimers();
			if(!timers.equals("")){
				strBld.append("-segment_times ");
				strBld.append(timers);
				strBld.append(" ");
			}
		}*/
		//add split-specific options (list)
		strBld.append(OPTIONS2);
		strBld.append(" ");
		//add option for list-file creation
		{
			StringBuilder listPath = new StringBuilder();
			listPath.append(LocalConfig.getPathVideoSplittedInput());
			listPath.append(getVideo().getUser().getId());
			listPath.append("\\");
			listPath.append(getVideo().getNameInput());
			listPath.append("\\");
			new File(listPath.toString()).mkdirs();
			listPath.append(LocalConfig.getListFileName());
			strBld.append(listPath.toString());
		}
		strBld.append(" ");
		strBld.append(OPTIONS3);
		//add path to output
		{
			StringBuilder outPath = new StringBuilder();
			outPath.append(LocalConfig.getPathVideoSplittedInput());
			outPath.append(getVideo().getUser().getId());
			outPath.append("\\");
			outPath.append(getVideo().getNameInput());
			outPath.append("\\");
			outPath.append(getVideo().getNameInput());
			outPath.append("_%2d.");
			outPath.append(getVideo().getExtInput());

			strBld.append(outPath.toString());
		}
		return strBld.toString();
	}

	/**
	 * used to compute the list of split timers
	 * Max : LocalConfig.maxSplitTime | Min : LocalConfig.minSplitTimeDuration
	 * @return list of times to split 
	 */
	private String computeSplitTimers(){
		StringBuilder timers = new StringBuilder();
		int maxSplitTime = LocalConfig.getMaxSplitTime();
		int minSplitTimeDuration = LocalConfig.getMinSplitTimeDuration();
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
	
	private String computeSplitTimer(){
		return String.valueOf(Integer.max(getVideoDuration()/LocalConfig.getMaxSplitTime(), LocalConfig.getMinSplitTimeDuration()));
	}
	
	private int getVideoDuration() {
		StringBuilder strCmd = new StringBuilder();
		strCmd.append(LocalConfig.getFFProbePath());
		strCmd.append(" -v error -show_entries format=duration -of default=noprint_wrappers=1:nokey=1 ");
		strCmd.append(LocalConfig.getPathVideoInput());
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
	
	public void reformatList() throws FileNotFoundException {
		StringBuilder strBld = new StringBuilder();
		//get the list file
		File file;
		{
			strBld.append(LocalConfig.getPathVideoSplittedInput());
			strBld.append(getVideo().getUser().getId());
			strBld.append("\\");
			strBld.append(getVideo().getNameInput());
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
				while(line != null){
					strBld.append(line);
					strBld.append("\n");
					line = br.readLine();
				}
				fr.close();
			} catch (FileNotFoundException e){
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
			strBld.append(getVideo().getUser().getId());
			strBld.append("\\");
			strBld.append(getVideo().getNameInput());
			strBld.append("\\");
			//replace
			Pattern pattern = Pattern.compile("(file )([a-zA-Z0-9_.-]+\\.\\w{3,5})[\\s\n]+");
			Matcher matcher = pattern.matcher(fileStr);
			String temp = strBld.toString().replaceAll("\\\\", "\\\\\\\\");
			while(matcher.find()){
				matcher.appendReplacement(result, matcher.group(1)+temp+matcher.group(2)+"\n");
			}
		}
		//Writting the file
		{
			PrintWriter pr = new PrintWriter(file);
			pr.print("");
			pr.append(result.toString());
			pr.flush();
		}
	}
}
