/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
public class FileOperationBean 
{

    @EJB
    private ConfigDAO configDAO;
    
    public void splitVideoFile(Video video)
    {
        String inputFile = configDAO.getPathVideoInput() + video.getUser().getId() + "\\" + video.getFullNameInput();
        String outputFile = configDAO.getPathVideoOutput() + video.getUser().getId() + "\\" + video.getFullNameInput();
        String splittedFileInput = configDAO.getPathVideoSplittedInput() + video.getUser().getId() + "\\" + video.getNameInput() + "%4d." + video.getExtInput();
        String splittedFileOutput = configDAO.getPathVideoSplittedOutput() + video.getUser().getId() + "\\" + video.getNameOutput() + "%4d." + video.getExtOutput();
        
        
        String[] cmd = new String[]{configDAO.getFFMPEGPath(),
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
        try 
        {
            runtime.exec(cmd);
        }
        catch (IOException ex) 
        {
            Logger.getLogger(FileOperationBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void ConvertFile(Video video)
    {
        
    }
    
}
