/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import java.util.HashMap;

/**
 *
 * @author Mokok
 */
public abstract class LocalConfig{
	
	private static HashMap<String,Object> config;
		
	public static int getMaxSplitTime() {
		return (int) config.get("maxSplitTime");
	}
	
	public static int getMinSplitTimeDuration() {
		return (int) config.get("minSplitTimeDuration");
	}

	public static String getFFMPEGPath() {
		return (String) config.get("pathFFMPEG");
	}
	
	public static String getFFProbePath() {
		return (String) config.get("pathFFProbe");
	}

	public static String getPathVideoInput() {
		return (String) config.get("pathInputFile");
	}

	public static String getPathVideoOutput() {
		return (String) config.get("pathOutputFile");
	}

	public static String getPathVideoSplittedInput() {
		return (String) config.get("pathSplittedInputFile");
	}

	public static String getPathVideoSplittedOutput() {
		return (String) config.get("pathSplittedOutputFile");
	}

	public static String getListFileName() {
		return (String) config.get("listFileName");
	}
	
	public static void setMaxSplitTime(int maxSplitTime) {
		if(config == null){
			config = new HashMap<>();
		}
		config.put("maxSplitTime", maxSplitTime);
	}
	
	public static void setMinSplitTimeDuration(int minSplitTimeDuration) {
		if(config == null){
			config = new HashMap<>();
		}
		config.put("minSplitTimeDuration", minSplitTimeDuration);
	}

	public static void setFFMPEGPath(String pathFFMPEG) {
		if(config == null){
			config = new HashMap<>();
		}
		config.put("pathFFMPEG", pathFFMPEG);
	}
	
	public static void setFFProbePath(String pathFFProbe) {
		if(config == null){
			config = new HashMap<>();
		}
		config.put("pathFFProbe", pathFFProbe);
	}

	public static void setPathVideoInput(String pathInputFile ) {
		if(config == null){
			config = new HashMap<>();
		}
		config.put("pathInputFile", pathInputFile);
	}

	public static void setPathVideoOutput(String pathOutputFile) {
		if(config == null){
			config = new HashMap<>();
		}
		config.put("pathOutputFile", pathOutputFile);
	}

	public static void setPathVideoSplittedInput(String pathSplittedInputFile) {
		if(config == null){
			config = new HashMap<>();
		}
		config.put("pathSplittedInputFile", pathSplittedInputFile);
	}

	public static void setPathVideoSplittedOutput(String pathSplittedOutputFile) {
		if(config == null){
			config = new HashMap<>();
		}
		config.put("pathSplittedOutputFile", pathSplittedOutputFile);
	}

	public static void setListFileName(String listFileName) {
		if(config == null){
			config = new HashMap<>();
		}
		config.put("listFileName", listFileName);
	}
}
