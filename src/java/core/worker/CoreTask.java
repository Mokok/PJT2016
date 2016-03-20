/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.worker;

import dao.ConfigDAO;
import entity.Video;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mokok
 */
public class CoreTask implements ITask {

	private static ConfigDAO configDAO;

	private final Video video;
	private String cmd;
	private static final String OPTIONS = "";

	/**
	 * This constructor should only be used as default empty constructor, and is
	 * not able to perform anything.
	 */
	public CoreTask() {
		video = new Video();
	}

	public CoreTask(Video video) {
		this.video = video;
	}

	@Override
	public String computeCmd() throws IOException {
		if (cmd == null || cmd.equals("")) {
			cmd = "CoreTask default output";
		}
		return cmd;
	}

	@Override
	public final String getOptions() {
		return OPTIONS;
	}

	@Override
	public Video getVideo() {
		return video;
	}

	public static ConfigDAO getConfig() {
		return configDAO;
	}

	@Override
	public final String toString() {
		String res = null;
		try {
			res = this.computeCmd();
		} catch (IOException ex) {
			Logger.getLogger(CoreTask.class.getName()).log(Level.SEVERE, null, ex);
		}
		return res;
	}
}
