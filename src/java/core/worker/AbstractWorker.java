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
public abstract class AbstractWorker implements Runnable {

    protected String command;

    @Override
    public void run() {
	System.out.println(Thread.currentThread().getName() + " Start. Command = " + command);
	processCommand();
	System.out.println(Thread.currentThread().getName() + " End.");
    }

    private void processCommand() {
	try {
	    Thread.sleep(5000);
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
    }

    @Override
    public String toString() {
	return this.command;
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
