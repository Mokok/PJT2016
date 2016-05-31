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
public interface ITask {

	/**
	 *
	 * @return the complete command line to be executed
	 * @throws IOException
	 */
	public String computeCmd() throws IOException;

	/**
	 *
	 * @return the video
	 */
	public Video getVideo();

}
