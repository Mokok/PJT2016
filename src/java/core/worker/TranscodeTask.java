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
import stateless.LocalConfig;

/**
 *
 * @author Mokok
 */
public class TranscodeTask extends CoreTask {
	
	private static final String OPTIONS = " -c copy -map 0 ";
	
	public TranscodeTask() {
	}
	
	public TranscodeTask(Video video) {
		super(video);
	}

	@Override
	public String computeCmd() throws IOException {
		StringBuilder strBld = new StringBuilder();
		//add ffmpeg exe path
		strBld.append(LocalConfig.getFFMPEGPath());
		//add path to source file
		strBld.append(" -y -i ");
		{
			StringBuilder inPath = new StringBuilder();
			inPath.append(LocalConfig.getPathVideoSplittedInput());
			inPath.append(this.getVideo().getUser().getId());
			inPath.append("\\");
			inPath.append(getVideo().getNameOutput());
			inPath.append("\\");
			inPath.append(getVideo().getFullNameInput());
			if (!new File(inPath.toString()).isFile()) {
				throw new FileNotFoundException(inPath.toString());
			}
			strBld.append(inPath.toString());
		}
		strBld.append(OPTIONS);
		//add path to source file
		{
			StringBuilder outPath = new StringBuilder();
			outPath.append(LocalConfig.getPathVideoSplittedOutput());
			outPath.append(getVideo().getUser().getId());
			outPath.append("\\");
			outPath.append(getVideo().getNameOutput());
			new File(outPath.toString()).mkdirs();
			outPath.append("\\");
			outPath.append(getVideo().getNameInput());
			outPath.append(".");
			outPath.append(getVideo().getExtOutput());
			strBld.append(outPath.toString());
		}		
		return strBld.toString();
	}
}
