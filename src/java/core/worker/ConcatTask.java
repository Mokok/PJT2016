/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.worker;

import dao.ConfigDAO;
import entity.Video;
import java.io.FileNotFoundException;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *
 * @author Mokok
 */
@Stateless
@LocalBean
public class ConcatTask extends CoreTask {
	
	@EJB
	private ConfigDAO config;
	
	private static final String OPTIONS = " -f concat -c copy -map 0";

	public ConcatTask() {
	}

	public ConcatTask(Video video) {
		super(video);
	}

	@Override
	public String computeCmd() throws FileNotFoundException {
		StringBuilder strBld = new StringBuilder();
		//add ffmpeg exe path
		strBld.append(config.getFFMPEGPath());
		//add option for list-file read
		strBld.append(" -i ");
		{
			StringBuilder listPath = new StringBuilder();
			listPath.append(config.getPathVideoSplittedInput());
			listPath.append(getVideo().getUser().getId());
			listPath.append("\\");
			listPath.append(getVideo().getFullNameInput());
			listPath.append("\\");
			listPath.append(config.getListFileName());
			strBld.append(listPath.toString());
		}
		//add split-specific options
		strBld.append(OPTIONS);
		strBld.append(" ");
		//add path to output
		{
			StringBuilder outPath = new StringBuilder();
			outPath.append(config.getPathVideoOutput());
			outPath.append(getVideo().getUser().getId());
			outPath.append("\\");
			outPath.append(getVideo().getNameOutput());
			outPath.append("\\");
			outPath.append(getVideo().getNameOutput());
			outPath.append(".");
			outPath.append(getVideo().getExtOutput());

			strBld.append(outPath.toString());
		}
		return strBld.toString();
	}
}
