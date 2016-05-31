/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Config;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Anthony
 */
@Stateless
@LocalBean
public class ConfigDAO {

	@PersistenceContext(unitName = "CorePU")
	private EntityManager em;

	public int getMaxSplitTime() {
		Config config = (Config) em.createNamedQuery("maxSplitTime", Config.class).getSingleResult();
		return Integer.parseInt(config.getVal());
	}

	public int getMinSplitTimeDuration() {
		Config config = (Config) em.createNamedQuery("minSplitTimeDuration", Config.class).getSingleResult();
		return Integer.parseInt(config.getVal());
	}

	public String getFFMPEGPath() {
		Config config = (Config) em.createNamedQuery("pathFFMPEG", Config.class).getSingleResult();
		return config.getVal();
	}

	public String getFFProbePath() {
		Config config = (Config) em.createNamedQuery("pathFFProbe", Config.class).getSingleResult();
		return config.getVal();
	}

	public String getPathVideoInput() {
		Config config = (Config) em.createNamedQuery("pathInputFile", Config.class).getSingleResult();
		return config.getVal();
	}

	public String getPathVideoOutput() {
		Config config = (Config) em.createNamedQuery("pathOutputFile", Config.class).getSingleResult();
		return config.getVal();
	}

	public String getPathVideoSplittedInput() {
		Config config = (Config) em.createNamedQuery("pathSplittedInputFile", Config.class).getSingleResult();
		return config.getVal();
	}

	public String getPathVideoSplittedOutput() {
		Config config = (Config) em.createNamedQuery("pathSplittedOutputFile", Config.class).getSingleResult();
		return config.getVal();
	}

	public String getListFileName() {
		Config config = (Config) em.createNamedQuery("listFileName", Config.class).getSingleResult();
		return config.getVal();
	}

	private Config setMinSplitTimeDuration(int seconds) throws Exception {
		if (seconds <= 0) {
			throw new Exception("le temps en seconde doit être positif");
		}

		Config config;
		try {
			config = (Config) em.createNamedQuery("minSplitTimeDuration", Config.class).getSingleResult();
		} catch (NoResultException ex) {
			config = new Config();
			config.setName("minSplitTimeDuration");
		}
		config.setVal(Integer.toString(seconds));
		em.persist(config);
		return config;
	}

	public Config setMaxSplitTime(int seconds) throws Exception {
		if (seconds <= 0) {
			throw new Exception("le temps en seconde doit être positif");
		}

		Config config;
		try {
			config = (Config) em.createNamedQuery("maxSplitTime", Config.class).getSingleResult();
		} catch (NoResultException ex) {
			config = new Config();
			config.setName("maxSplitTime");
		}
		config.setVal(Integer.toString(seconds));
		em.persist(config);
		return config;
	}

	public Config setFFMPEGPath(String path) {
		Config config;
		try {
			config = (Config) em.createNamedQuery("pathFFMPEG", Config.class).getSingleResult();
		} catch (NoResultException ex) {
			config = new Config();
			config.setName("pathFFMPEG");
		}
		config.setVal(path);
		em.persist(config);
		return config;
	}

	public Config setFFProbePath(String path) {
		Config config;
		try {
			config = (Config) em.createNamedQuery("pathFFProbe", Config.class).getSingleResult();
		} catch (NoResultException ex) {
			config = new Config();
			config.setName("pathFFProbe");
		}
		config.setVal(path);
		em.persist(config);
		return config;
	}

	public Config setPathVideoInput(String path) {
		Config config;
		try {
			config = (Config) em.createNamedQuery("pathInputFile", Config.class).getSingleResult();
		} catch (NoResultException ex) {
			config = new Config();
			config.setName("pathInputFile");
		}
		config.setVal(path);
		em.persist(config);
		return config;
	}

	public Config setPathVideoOutput(String path) {
		Config config;
		try {
			config = (Config) em.createNamedQuery("pathOutputFile", Config.class).getSingleResult();
		} catch (NoResultException ex) {
			config = new Config();
			config.setName("pathOutputFile");
		}
		config.setVal(path);
		em.persist(config);
		return config;
	}

	public Config setPathVideoSplittedInput(String path) {
		Config config;
		try {
			config = (Config) em.createNamedQuery("pathSplittedInputFile", Config.class).getSingleResult();
		} catch (NoResultException ex) {
			config = new Config();
			config.setName("pathSplittedInputFile");
		}
		config.setVal(path);
		em.persist(config);
		return config;
	}

	public Config setPathVideoSplittedOutput(String path) {
		Config config;
		try {
			config = (Config) em.createNamedQuery("pathSplittedOutputFile", Config.class).getSingleResult();
		} catch (NoResultException ex) {
			config = new Config();
			config.setName("pathSplittedOutputFile");
		}
		config.setVal(path);
		em.persist(config);
		return config;
	}

	public Config setListFileName(String listtxt) {
		Config config;
		try {
			config = (Config) em.createNamedQuery("listFileName", Config.class).getSingleResult();
		} catch (NoResultException ex) {
			config = new Config();
			config.setName("listFileName");
		}
		config.setVal(listtxt);
		em.persist(config);
		return config;
	}

	public List<Config> getConfigList() {
		return em.createNamedQuery("listConfig", Config.class).getResultList();
	}

	public void setDefaultConfig() throws Exception {
		this.setMaxSplitTime(15);
		this.setMinSplitTimeDuration(600);
		this.setFFMPEGPath("E:\\ffmpeg\\bin\\ffmpeg");
		this.setFFProbePath("E:\\ffmpeg\\bin\\ffprobe");
		this.setPathVideoInput("E:\\FILES\\VideoInput\\");
		this.setPathVideoOutput("E:\\FILES\\VideoOutput\\");
		this.setPathVideoSplittedInput("E:\\FILES\\VideoSplitted\\Input\\");
		this.setPathVideoSplittedOutput("E:\\FILES\\VideoSplitted\\Output\\");
		this.setListFileName("list.ffconcat");
	}
}
