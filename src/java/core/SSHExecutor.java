/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidParameterException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mokok
 */
public class SSHExecutor implements Runnable {

	private final String command;

	SSHExecutor(String computeCmd) {
		if (computeCmd == null || computeCmd.isEmpty()) {
			throw new InvalidParameterException("ssh command must be set");
		}
		command = computeCmd;
	}

	private void executeSSHCommand() {

		////////////////////////////////////////////
		/////////////CREDENTIALS////////////////////
		////////////////////////////////////////////
		String host = "127.0.0.1";////////////////////
		String user = "user";/////////////////////////
		String password = "password";/////////////////
		////////////////////////////////////////////
		////////////////////////////////////////////

		try {

			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			JSch jsch = new JSch();
			Session session = jsch.getSession(user, host, 22);
			session.setPassword(password);
			session.setConfig(config);
			session.connect();
			System.out.println("Connected");

			Channel channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(command);
			channel.setInputStream(null);
			((ChannelExec) channel).setErrStream(System.err);

			InputStream in = channel.getInputStream();
			channel.connect();
			byte[] tmp = new byte[1024];
			while (true) {
				while (in.available() > 0) {
					int i = in.read(tmp, 0, 1024);
					if (i < 0) {
						break;
					}
					System.out.print(new String(tmp, 0, i));
				}
				if (channel.isClosed()) {
					System.out.println("exit-status: " + channel.getExitStatus());
					break;
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException ex) {
					Logger.getLogger(SSHExecutor.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
			channel.disconnect();
			session.disconnect();
			System.out.println("DONE");
		} catch (JSchException | IOException ex) {
			Logger.getLogger(ThreadTask.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	@Override
	public void run() {
		executeSSHCommand();
	}
}
