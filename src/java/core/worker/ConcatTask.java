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
public class ConcatTask extends CoreTask {

	private static final String OPTIONS = "";

	private ConcatTask() {

	}

	public ConcatTask(Video video) {
		super(video);
	}

	@Override
	public String computeCmd() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
