/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.worker;

import entity.Video;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author Mokok
 */
public class SplitTask extends CoreTask {

	private static final String OPTIONS = "-f segment -segment_times 10,20,30 -map 0 -vcodec copy -acodec copy -segment_list";

	private SplitTask() {

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
		strBld.append(getConfig().getFFMPEGPath());
		//add path to source file
		strBld.append(" -i ");
		{
			StringBuilder inPath = new StringBuilder();
			inPath.append(getConfig().getPathVideoInput());
			inPath.append(this.getVideo().getUser().getId());
			inPath.append("\\");
			inPath.append(getVideo().getFullNameInput());
			if (!new File(inPath.toString()).isFile()) {
				throw new FileNotFoundException(inPath.toString());
			}
			strBld.append(inPath.toString());
		}
		strBld.append(" ");
		//add split-specific options
		strBld.append(OPTIONS);
		strBld.append(" ");
		//add option for list-file creation
		{
			StringBuilder listPath = new StringBuilder();
			listPath.append(getConfig().getPathVideoInput());
			listPath.append(getVideo().getUser().getId());
			listPath.append("\\");
			listPath.append(getVideo().getFullNameInput());
			listPath.append("\\");
			new File(listPath.toString()).mkdirs();
			listPath.append(getConfig().getListFileName());
			strBld.append(listPath.toString());
		}
		strBld.append(" ");
		//add path to output
		{
			StringBuilder outPath = new StringBuilder();
			outPath.append(getConfig().getPathVideoSplittedInput());
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
}
