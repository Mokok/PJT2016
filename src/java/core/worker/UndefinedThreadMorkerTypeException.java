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
public class UndefinedThreadMorkerTypeException extends Exception {

    /**
     * Creates a new instance of <code>UndifinedThreadMorkerTypeException</code>
     * without detail message.
     */
    public UndefinedThreadMorkerTypeException() {
    }

    /**
     * Constructs an instance of <code>UndifinedThreadMorkerTypeException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public UndefinedThreadMorkerTypeException(String msg) {
	super(msg);
    }
}
