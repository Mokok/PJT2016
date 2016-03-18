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

	public String computeCmd() throws IOException;

	abstract String getOptions();

	public Video getVideo();
}
