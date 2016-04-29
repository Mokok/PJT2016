/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.worker;

import entity.Video;

/**
 *
 * @author Mokok
 */
public class TranscodeTask extends CoreTask {

	private static final String OPTIONS = "";

	public TranscodeTask(Video video) {
		super(video);
	}

	@Override
	public String computeCmd() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
}
/*
	private String computeTranscodeCmd() throws FileNotFoundException, IOException {
		StringBuilder strBld = new StringBuilder();
		//add path to source file
		strBld.append("-i ");
		StringBuilder path = new StringBuilder();
		path.append(configDAO.getPathVideoSplittedInput());
		path.append(getVideoTask().getUser().getId());
		path.append("\\");
		path.append(getVideoTask().getFullNameInput());
		if (!new File(path.toString()).isFile()) {
			throw new FileNotFoundException(path.toString());
		}
		strBld.append(path);
		path.setLength(0);
		strBld.append(" ");
		//add split-specific options
		strBld.append(TRANSCODE_OPT);
		strBld.append(" ");
		//add path to output
		path.setLength(0);
		path.append(configDAO.getPathVideoSplittedOutput());
		path.append(getVideoTask().getUser().getId());
		path.append("\\");
		path.append(getVideoTask().getFullNameInput());
		//create directories if not exist
		FileUtils.resetFolder(new File(path.toString()));
		strBld.append(path);
		path.setLength(0);
		return strBld.toString();
	}
 */
