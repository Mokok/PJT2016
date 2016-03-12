/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author Anthony
 */
@Entity
public class Video implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String nameInput;
    private String nameOutput;
    private String extInput;
    private String extOutput;
    @ManyToOne()
    private User user;

    public long getId() {
	return id;
    }

    public void setId(long id) {
	this.id = id;
    }

    public String getNameInput() {
	return nameInput;
    }

    public void setNameInput(String nameInput) {
	this.nameInput = nameInput;
    }

    public String getNameOutput() {
	return nameOutput;
    }

    public void setNameOutput(String nameOutput) {
	this.nameOutput = nameOutput;
    }

    public String getExtInput() {
	return extInput;
    }

    public void setExtInput(String extInput) {
	this.extInput = extInput;
    }

    public String getExtOutput() {
	return extOutput;
    }

    public void setExtOutput(String extOutput) {
	this.extOutput = extOutput;
    }

    public User getUser() {
	return user;
    }

    public void setUser(User user) {
	this.user = user;
    }

    public String getFullNameInput() {
	return this.nameInput + "." + this.extInput;
    }

    public String getFullNameOutput() {
	return this.nameOutput + "." + this.extOutput;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + (int) (id ^ (id >>> 32));
	return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	Video other = (Video) obj;
	if (id != other.id) {
	    return false;
	}
	return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "Video [id=" + id + "]";
    }

}
