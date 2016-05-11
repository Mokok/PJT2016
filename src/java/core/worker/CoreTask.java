/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.worker;

import entity.Video;
import java.io.IOException;
import java.util.concurrent.Callable;

/**
 *
 * @author Mokok
 */
public class CoreTask implements ITask,Callable {
		
	private final Video video;
	private static final String OPTIONS = "";
	
	public CoreTask() {
		video = null;
	}	

	public CoreTask(Video video) {
		this.video = video;
	}

	@Override
	public String computeCmd() throws IOException {
		return "echo CoreTask default output";
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

	@Override
	public String call() throws Exception {
		return this.computeCmd();
	}
}
