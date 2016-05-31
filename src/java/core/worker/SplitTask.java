/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.worker;

import core.WorkerUtils;
import entity.Video;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import stateless.LocalConfig;

/**
 *
 * @author Mokok
 */
public class SplitTask extends CoreTask {

	private static final String OPTIONS = "-f segment ";
	private static final String OPTIONS2 = " -segment_list ";
	private static final String OPTIONS3 = " -reset_timestamps 1 -c copy -map 0 ";

	/**
	 *
	 * @param video
	 */
	public SplitTask(Video video) {
		super(video);
	}

	/**
	 * @return the complete command line to be executed
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
	 * used to compute the list of split timers Max : LocalConfig.maxSplitTime |
	 * Min : LocalConfig.minSplitTimeDuration
	 *
	 * @return list of times to split
	 */
	private String computeSplitTimers() {
		StringBuilder timers = new StringBuilder();
		int maxSplitTime = LocalConfig.getMaxSplitTime();
		int minSplitTimeDuration = LocalConfig.getMinSplitTimeDuration();
		int videoDuration = WorkerUtils.getVideoDuration(getVideo());
		int numberOfSlice = Integer.min(maxSplitTime, videoDuration / minSplitTimeDuration);

		if (numberOfSlice != 0) {
			int splitDuration = videoDuration / numberOfSlice;
			timers.append(splitDuration);
			for (int i = 2; i < numberOfSlice; i++) {
				timers.append(",");
				timers.append(String.valueOf(splitDuration * i));
			}
		}

		return timers.toString();
	}

	private String computeSplitTimer() {
		int duration = WorkerUtils.getVideoDuration(getVideo());
		//video.setDuration(duration);
		return String.valueOf(Integer.max(duration / LocalConfig.getMaxSplitTime(), LocalConfig.getMinSplitTimeDuration()));
	}
}
