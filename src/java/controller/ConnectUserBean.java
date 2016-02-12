/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.UserDAO;
import entity.User;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import javax.ejb.EJB;
import util.Crypto;

/**
 *
 * @author Anthony
 */
@Named(value = "connectUserBean")
@SessionScoped
public class ConnectUserBean implements Serializable {

    @EJB
    private UserDAO userDAO;

    private User userConnected = null;
    private User user;
    private final ArrayList<String> errorMessage;
    private final ArrayList<String> successBlock;
    
    /**
     * Creates a new instance of ConnectUserBean
     */
    public ConnectUserBean() 
    {
        user = new User();
        errorMessage = new ArrayList<>();
        successBlock = new ArrayList<>();
        for(int i = 0; i < 2; i++)
        {
            errorMessage.add("");
            successBlock.add("");
        }
    }

    public User getUser() {
        return user;
    }

    public User getUserConnected() {
        return userConnected;
    }
    
    public ArrayList<String> getErrorMessage() {
        return errorMessage;
    }

    public ArrayList<String> getSuccessBlock() {
        return successBlock;
    }
    
    public boolean connectUser()
    {
        User userTemp;
        if((userTemp = userDAO.findUserByUsername(user.getUsername())) == null)
        {
            errorMessage.set(0, "Cet Utilisateur n'existe pas");
            successBlock.set(0, "has-error");
            return false;
        }
        if(!userTemp.getPassword().equals(Crypto.encrypt(user.getPassword())))
        {
            errorMessage.set(1, "Le mot de passe n'est pas bon");
            successBlock.set(1, "has-error");
            return false;
        }
        userConnected = userTemp;
        return true;
    }
    
    public void disconnectUser()
    {
        userConnected = null;
    }
    
    
    
}
