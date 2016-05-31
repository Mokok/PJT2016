/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.exception;

/**
 *
 * @author Mokok
 */
public class InvalidPreviousThreadTaskException extends Exception {

	/**
	 * Standard exception called when a #CoreTask with a wrong dynamic type
	 * called the #ThreadTaskEndListener
	 *
	 * @param message
	 */
	public InvalidPreviousThreadTaskException(String message) {
		super(message);
	}

}
