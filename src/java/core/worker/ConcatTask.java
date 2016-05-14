/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.worker;

import entity.Video;
import java.io.FileNotFoundException;
import stateless.LocalConfig;

/**
 *
 * @author Mokok
 */
public class ConcatTask extends CoreTask {
			
	private static final String OPTIONS = "  -c copy -map 0 -y ";

	public ConcatTask() {
	}

	public ConcatTask(Video video) {
		super(video);
	}

	@Override
	public String computeCmd() throws FileNotFoundException {
		StringBuilder strBld = new StringBuilder();
		//add ffmpeg exe path
		strBld.append(LocalConfig.getFFMPEGPath());
		//add option for list-file read
		strBld.append(" -f concat -safe 0 -i ");
		{
			StringBuilder listPath = new StringBuilder();
			listPath.append(LocalConfig.getPathVideoSplittedInput());
			listPath.append(getVideo().getUser().getId());
			listPath.append("\\");
			listPath.append(getVideo().getNameInput());
			listPath.append("\\");
			listPath.append(LocalConfig.getListFileName());
			strBld.append(listPath.toString());
		}
		//add split-specific options
		strBld.append(OPTIONS);
		//add path to output
		{
			StringBuilder outPath = new StringBuilder();
			outPath.append(LocalConfig.getPathVideoOutput());
			outPath.append(getVideo().getUser().getId());
			outPath.append("\\");
			outPath.append(getVideo().getFullNameOutput());
			strBld.append(outPath.toString());
		}
		return strBld.toString();
	}
}
