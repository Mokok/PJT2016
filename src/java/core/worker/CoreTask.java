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

	/**
	 * Empty constructor, no use in usual cases
	 */
	public CoreTask() {
		video = null;
	}

	/**
	 *
	 * @param video
	 */
	public CoreTask(Video video) {
		this.video = video;
	}

	/**
	 *
	 * @return the complete command line to be executed
	 * @throws IOException
	 */
	@Override
	public String computeCmd() throws IOException {
		return "echo CoreTask default output";
	}

	/**
	 *
	 * @return the video
	 */
	@Override
	public Video getVideo() {
		return video;
	}

	/**
	 *
	 * @param newStatus
	 */
	public void setStatus(TaskStatus newStatus) {
		status = newStatus;
	}

	/**
	 *
	 * @return the task status (ended,canceled,..)
	 */
	public TaskStatus getStatus() {
		return status;
	}
}
