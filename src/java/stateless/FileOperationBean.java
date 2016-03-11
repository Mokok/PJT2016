/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import dao.ConfigDAO;
import entity.Video;

/**
 *
 * @author Anthony
 */
@Stateless
public class FileOperationBean {

    private String inputFile;
    private String outputFile;
    private String splittedFileInput;
    private String splittedFileOutput;
    
    @EJB
    private ConfigDAO configDAO;

    public void splitVideoFile(Video video) {
	this.prepareStrings(video);
	
	final String[] cmd = new String[]{configDAO.getFFMPEGPath(),
	    "-i",
	    inputFile,
	    "-f",
	    "segment",
	    "-segment_time",
	    String.valueOf(configDAO.getSplitTime()),
	    "-codec",
	    "copy",
	    "-map",
	    "0",
	    splittedFileInput};

	Runtime runtime = Runtime.getRuntime();
	try {
	    runtime.exec(cmd);
	} catch (IOException ex) {
	    Logger.getLogger(FileOperationBean.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    public void ConvertFile(Video video) {

    }

    /**
     * Prepares strings such as inputFile,outputFile,splittedFileInput,splittedFileOutput
     * @param video 
     */
    private void prepareStrings(Video video) {
	StringBuilder strBld = new StringBuilder();
	
	strBld.append(configDAO.getPathVideoInput());
	strBld.append(video.getUser().getId());
	strBld.append("\\");
	strBld.append(video.getFullNameInput());
	inputFile = configDAO.getPathVideoInput() + video.getUser().getId() + "\\" + video.getFullNameInput();
	
	/*strBld.setLength(0);
	strBld.append(configDAO.getPathVideoOutput());
	strBld.append(video.getUser().getId());
	strBld.append("\\");
	strBld.append(video.getFullNameInput());
	outputFile = configDAO.getPathVideoOutput() + video.getUser().getId() + "\\" + video.getFullNameInput();*/
	
	strBld.setLength(0);
	strBld.append(configDAO.getPathVideoSplittedInput());
	strBld.append(video.getUser().getId());
	strBld.append("\\");
	strBld.append(video.getNameInput());
	strBld.append("%4d.");
	strBld.append(video.getExtInput());
	splittedFileInput = strBld.toString();
	
	/*strBld.setLength(0);
	strBld.append(configDAO.getPathVideoSplittedOutput());
	strBld.append(video.getUser().getId());
	strBld.append("\\");
	strBld.append(video.getNameOutput());
	strBld.append("%4d.");
	strBld.append(video.getExtOutput());
	splittedFileOutput = strBld.toString();*/
	
	strBld.setLength(0);
	strBld = null;
    }

}
