/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import entity.Config;

/**
 *
 * @author Anthony
 */
@Stateless
@LocalBean
public class ConfigDAO {

    @PersistenceContext(unitName = "CorePU")
    private EntityManager em;

    public int getSplitTime()
    {
    	Config config = (Config) em.createNamedQuery("splitTime").getSingleResult();
    	return Integer.parseInt(config.getVal());
    }
    
    public String getFFMPEGPath()
    {
    	Config config = (Config) em.createNamedQuery("pathFFMPEG").getSingleResult();
        return config.getVal();
    }
    
    public String getPathVideoInput()
    {
    	Config config = (Config) em.createNamedQuery("pathInputFile").getSingleResult();
        return config.getVal();
    }
    
    public String getPathVideoOutput()
    {
    	Config config = (Config) em.createNamedQuery("pathOutputFile").getSingleResult();
        return config.getVal();
    }
    
    public String getPathVideoSplittedInput()
    {
    	Config config = (Config) em.createNamedQuery("pathSplittedInputFile").getSingleResult();
        return config.getVal();
    }
    
    public String getPathVideoSplittedOutput()
    {
    	Config config = (Config) em.createNamedQuery("pathSplittedOutputFile").getSingleResult();
        return config.getVal();
    }
    
    public Config setSplitTime(int seconds) throws Exception
    {
    	if(seconds <= 0)
    		throw new Exception("le temps en seconde doit être positif");
    	
    	Config config;
    	try
    	{
    		config = (Config) em.createNamedQuery("splitTime").getSingleResult();
    	}
    	catch(NoResultException ex)
    	{
    		config = new Config();
    		config.setName("splitTime");
    	}
    	config.setVal(Integer.toString(seconds));
    	em.persist(config);
    	return config;
    }
    
    public Config setFFMPEGPath(String path)
    {
    	
    	Config config;
    	try
    	{
    		config = (Config) em.createNamedQuery("pathFFMPEG").getSingleResult();
    	}
    	catch(NoResultException ex)
    	{
    		config = new Config();
    		config.setName("pathFFMPEG");
    	}
    	config.setVal(path);
    	em.persist(config);
        return config;
    }
    
    public Config setPathVideoInput(String path)
    {
    	Config config;
    	try
    	{
    		config = (Config) em.createNamedQuery("pathInputFile").getSingleResult();
    	}
    	catch(NoResultException ex)
    	{
    		config = new Config();
    		config.setName("pathInputFile");
    	}
    	config.setVal(path);
    	em.persist(config);
        return config;
    }
    
    public Config setPathVideoOutput(String path)
    {
    	Config config;
    	try
    	{
    		config = (Config) em.createNamedQuery("pathOutputFile").getSingleResult();
    	}
    	catch(NoResultException ex)
    	{
    		config = new Config();
    		config.setName("pathOutputFile");
    	}
    	config.setVal(path);
    	em.persist(config);
        return config;
    }
    
    public Config setPathVideoSplittedInput(String path)
    {
    	Config config;
    	try
    	{
    		config = (Config) em.createNamedQuery("pathSplittedInputFile").getSingleResult();
    	}
    	catch(NoResultException ex)
    	{
    		config = new Config();
    		config.setName("pathSplittedInputFile");
    	}
    	config.setVal(path);
    	em.persist(config);
        return config;
    }
    
    public Config setPathVideoSplittedOutput(String path)
    {
    	Config config;
    	try
    	{
    		config = (Config) em.createNamedQuery("pathSplittedOutputFile").getSingleResult();
    	}
    	catch(NoResultException ex)
    	{
    		config = new Config();
    		config.setName("pathSplittedOutputFile");
    	}
    	config.setVal(path);
    	em.persist(config);
        return config;
    }
    
    @SuppressWarnings("unchecked")
	public List<Config> getConfigList()
    {
    	return em.createNamedQuery("listConfig").getResultList();
    }
}
