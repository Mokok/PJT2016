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
import stateless.FileOperationBean;

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
	    configDAO.setSplitTime(60);
	    configDAO.setFFMPEGPath("E:\\ffmpeg\\bin\\ffmpeg");
	    configDAO.setPathVideoInput("E:\\FILES\\VideoInput\\");
	    configDAO.setPathVideoOutput("E:\\FILES\\VideoOutput\\");
	    configDAO.setPathVideoSplittedInput("E:\\FILES\\VideoSplitted\\Input\\");
	    configDAO.setPathVideoSplittedOutput("E:\\FILES\\VideoSplitted\\Output\\");
	    configDAO.setListFileName("list.txt");
	} catch (Exception e) {
	    System.err.printf(e.getMessage());
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

    /**
     * @return the configDAO
     */
    public ConfigDAO getConfigDAO() {
	return configDAO;
    }
}
