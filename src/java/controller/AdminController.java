/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;

import dao.ConfigDAO;
import entity.User;

import javax.enterprise.context.RequestScoped;

import entity.Video;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import stateless.FileOperationBean;
import stateless.LocalConfig;

/**
 *
 * @author Anthony
 */
@Named(value = "adminController")
@RequestScoped
public class AdminController {

	@EJB
	private FileOperationBean splitBean;

	@EJB
	private ConfigDAO configDAO;

	private String res;
	
	/**
	 * Creates a new instance of AdminController
	 */
	public AdminController() {

	}

	/**
	 * Simulation à supprimer
	 */
	@PostConstruct
	public void simulateConfig() {
		try {
			configDAO.setDefaultConfig();
			initConfig();
		} catch (Exception ex) {
			Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * Simulation à supprimer
	 *
	 * @return
	 */
	public Video simulationVideoAndUser() {
		Video video = new Video();
		video.setExtInput("avi");
		video.setNameInput("test");
		video.setExtOutput("mp4");
		video.setNameOutput("test-transcoded");

		User user = new User();
		user.setId(1);
		user.setFirstName("firstName");
		user.setLastName("lastName");
		video.setUser(user);

		return video;
	}

	public void splitFile() {
		Video video = simulationVideoAndUser();
		splitBean.testFFmpeg(video);
	}

	public void testComputeCmd() throws IOException{
		res = splitBean.testComputeCmd();
	}
	
	public void testDuration(){
		res = splitBean.testDuration();
	}
	
	public void testSplit(){
		try {
			splitBean.testSplit();
		} catch (InterruptedException ex) {
			Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public String displayRes(){
		return res;
	}
	
	/**
	 * @return the configDAO
	 */
	public ConfigDAO getConfigDAO() {
		return configDAO;
	}

	public void init(){
		splitBean.init();
	}
	
	private void initConfig() {
		LocalConfig.setMaxSplitTime(configDAO.getMaxSplitTime());
		LocalConfig.setMinSplitTimeDuration(configDAO.getMinSplitTimeDuration());
		LocalConfig.setFFMPEGPath(configDAO.getFFMPEGPath());
		LocalConfig.setFFProbePath(configDAO.getFFProbePath());
		LocalConfig.setPathVideoInput(configDAO.getPathVideoInput());
		LocalConfig.setPathVideoOutput(configDAO.getPathVideoOutput());
		LocalConfig.setPathVideoSplittedInput(configDAO.getPathVideoSplittedInput());
		LocalConfig.setPathVideoSplittedOutput(configDAO.getPathVideoSplittedOutput());
		LocalConfig.setListFileName(configDAO.getListFileName());
	}
}
