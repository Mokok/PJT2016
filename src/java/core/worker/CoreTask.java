/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.worker;

import dao.ConfigDAO;
import entity.Video;
import java.io.IOException;

/**
 *
 * @author Mokok
 */
public class CoreTask implements ITask {
		
	private final Video video;
	private String cmd;
	private static final String OPTIONS = "";
	
	public CoreTask() {
		video = null;
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
	public Video getVideo() {
		return video;
	}
	
	/*public void setConfig(ConfigDAO configDAO){
		config = configDAO;
	}*/

	/*@Override
	public final String toString() {
		String res = null;
		try {
			res = this.computeCmd();
		} catch (IOException ex) {
			Logger.getLogger(CoreTask.class.getName()).log(Level.SEVERE, null, ex);
		}
		return res;
	}*/

}
