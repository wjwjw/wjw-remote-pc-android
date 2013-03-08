package com.key.socketdata;

import java.net.*;

//import java.io.IOException;

/**
 * OSCPort is an 抽象超类. 用 @see OSCPortOut 来发送 OSC messages. 用 @see OSCPortIn 来监听
 * OSC messages.
 * 
 * @author
 * @version 1.0
 */
public abstract class OSCPort {

	protected DatagramSocket socket;
	protected int port;
	static int defaultPort = 57110;

	/**
	 * The port that the SuperCollider <b>synth</b> engine ususally listens to
	 * &mdash; 57110.
	 */
	public static int defaultSCOSCPort() {
		return defaultPort;
	}

	/**
	 * The port that the SuperCollider <b>language</b> engine ususally listens
	 * to &mdash; 57120.
	 */
	public static int defaultSCLangOSCPort() {
		return defaultPort;
	}

	/**
	 * Close the socket if this hasn't already happened.
	 * 
	 * @see java.lang.Object#finalize()
	 */
	protected void finalize() throws Throwable {
		super.finalize();
		socket.close();
	}

	/**
	 * Close the socket and free-up resources. It's recommended that clients
	 * call this when they are done with the port.
	 */
	public void close() {
		socket.close();
	}

}
