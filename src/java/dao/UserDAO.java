package dao;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import entity.User;
import java.util.List;
import util.Crypto;

/**
 * Session Bean implementation class UserDAO
 */
@Stateless
@LocalBean
public class UserDAO {
	
    @PersistenceContext(unitName = "WebPU")
    private EntityManager em;

    /**
     * Default constructor. 
     */
    public UserDAO() {
        // TODO Auto-generated constructor stub
    }
    
    public User createUser(User user)
    {
    	user.setPassword(Crypto.encrypt(user.getPassword()));
    	em.persist(user);
        return user;
    }
    
    public User findUserById(int id)
    {
    	return em.find(User.class, id);
    }
    
    public User findUserByUsername(String username)
    {
        Query query = em.createNamedQuery("findByUserName", User.class);
        try
        {
            User user = (User) query.setParameter("username", username).getSingleResult();
            return user;
        }
        catch(NoResultException ex)
        {
            return null;
        }
    }
    
    public User findUserByEmail(String email)
    {
    	Query query = em.createNamedQuery("findByEmail", User.class);
        try
        {
            User user = (User) query.setParameter("email", email).getSingleResult();
            return user;
        }
        catch(NoResultException ex)
        {
            return null;
        }
    }
    
    public User deleteUser(User user)
    {
    	em.remove(user);
    	return user;
    }
    
    public User updateUser(User user)
    {
        em.merge(user);
        return user;
    }
    
    public List<User> getAllUser()
    {
        Query query = em.createNamedQuery("listUser", User.class);
        return query.getResultList();
    }
}
