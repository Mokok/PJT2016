/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.UserDAO;
import entity.User;
import javax.inject.Named;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.persistence.NoResultException;

/**
 *
 * @author Anthony
 */
@Named(value = "createUserBean")
@RequestScoped
public class CreateUserBean implements Serializable 
{

    @EJB
    private UserDAO userDAO;
    
    private User user;
    private final ArrayList<String> errorMessage;
    private final ArrayList<String> successBlock;
    

    /**
     * Creates a new instance of UserManagedBean
     */
    public CreateUserBean() 
    {
        user = new User();
        errorMessage = new ArrayList<>();
        successBlock = new ArrayList<>();
        for(int i = 0; i < 5; i++)
        {
            errorMessage.add("");
            successBlock.add("");
        }
        
        
    }

    public User getUser() 
    {
        return user;
    }
    
    public void register()
    {
        if(verifInput())
        {
            initDateRegister();
            userDAO.createUser(user);
        }
    }
    
    private void initDateRegister()
    {
        Timestamp date = new Timestamp(System.currentTimeMillis());
        user.setDateRegister(date);
    }

    public ArrayList<String> getErrorMessage() 
    {
        return errorMessage;
    }

    public ArrayList<String> getSuccessBlock() {
        return successBlock;
    }
    
    public void usernameListener()
    {
        if(user.getUsername().length() < 3)
        {
            errorMessage.set(0, "Votre nom d'utilisateur doit avoir au moins 3 caractères");
            successBlock.set(0, "has-error");
        }
        else if(user.getUsername().length() > 15)
        {
            errorMessage.set(0, "Votre nom d'utilisateur doit avoir au maximum 15 caractères");
            successBlock.set(0, "has-error");
        }
        else if(userDAO.findUserByUsername(user.getUsername()) != null)
        {
            errorMessage.set(0, "Ce nom d'utilisateur existe déjà");
            successBlock.set(0, "has-error");
        }
        else
        {
            errorMessage.set(0, "");
            successBlock.set(0, "has-success");
        }
        
    }
    
    public void passwordListener()
    {
        String PASSWORD_PATTERN = "(?=^.{8,}$)(?=.*\\d)(?=.*[!@#$%^&*]+)(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$";
        if(!user.getPassword().matches(PASSWORD_PATTERN))
        {
            errorMessage.set(1, "Votre mot de passe doit comporter au moins 8 caractères dont une majuscule, un chiffre et un cractère spéciale");
            successBlock.set(1, "has-error");
        }
        else
        {
            errorMessage.set(1, "");
            successBlock.set(1, "has-success");
        }
    }
    
    public void firstnameListener()
    {
        String PASSWORD_PATTERN = "[a-zA-Z_-]{2,20}";
        if(!user.getFirstName().matches(PASSWORD_PATTERN))
        {
            errorMessage.set(2, "Votre prénom doit comprendre au moins 2 caractères, uniquement des lettres et le tiret.");
            successBlock.set(2, "has-error");
        }
        else
        {
            errorMessage.set(2, "");
            successBlock.set(2, "has-success");
        }
    }
    
    public void lastnameListener()
    {
        String PASSWORD_PATTERN = "[a-zA-Z_-]{2,20}";
        if(!user.getLastName().matches(PASSWORD_PATTERN))
        {
            errorMessage.set(3, "Votre nom doit comprendre au moins 2 caractères, uniquement des lettres et le tiret.");
            successBlock.set(3, "has-error");
        }
        else
        {
            errorMessage.set(3, "");
            successBlock.set(3, "has-success");
        }
    }
    
    public void mailListener()
    {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        if(!user.getEmail().matches(EMAIL_PATTERN))
        {
            errorMessage.set(4, "Votre saisie ne correspond pas à une adresse mail");
            successBlock.set(4, "has-error");
        }
        else if(userDAO.findUserByEmail(user.getEmail()) != null)
        {
            errorMessage.set(4, "Cette adresse mail est déjà utilisée");
            successBlock.set(4, "has-error");
        }
        else
        {
            errorMessage.set(4, "");
            successBlock.set(4, "has-success");
        }
    }
    
    private boolean verifInput()
    {
        boolean success = true;
        for(int i = 0; i < 5; i++)
        {
            if(errorMessage.get(i).equalsIgnoreCase("has-error"))
            {
                success = false;
            }
        }
        if(user.getUsername().isEmpty())
        {
            success = false;
        }
        else if(user.getPassword().isEmpty())
        {
            success = false;
        }
        else if(user.getFirstName().isEmpty())
        {
            success = false;
        }
        else if(user.getLastName().isEmpty())
        {
            success = false;
        }
        else if(user.getEmail().isEmpty())
        {
            success = false;
        }
        
        return success;
    }
}
