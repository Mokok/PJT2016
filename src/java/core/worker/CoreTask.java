/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.worker;

import entity.Video;
import java.io.IOException;

/**
 *
 * @author Mokok
 */
public class CoreTask implements ITask {

	private final Video video;
	private static final String OPTIONS = "";
	private TaskStatus status = TaskStatus.NOT_STARTED;

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

	public void setStatus(TaskStatus newStatus) {
		status = newStatus;
	}

	public TaskStatus getStatus() {
		return status;
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
