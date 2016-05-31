/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.worker;

/**
 *
 * @author Mokok
 */
public enum TaskStatus {

	/**
	 * task still not started
	 */
	NOT_STARTED,
	/**
	 * task in progress
	 */
	IN_PROGRESS,
	/**
	 * task done (with succes)
	 */
	DONE,
	/**
	 * task cancelled or error
	 */
	CANCELLED
}
