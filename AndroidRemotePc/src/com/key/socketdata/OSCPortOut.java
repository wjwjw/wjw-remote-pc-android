package com.key.socketdata;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.util.Log;

/**
 * OSCPortOut is the class that sends OSC messages to a specific address and
 * port. OSCPortOut��һ��������OSC messages ���͵�����ĵ�ַ�Ͷ˿ڵ��� ���� send()���� To send an OSC
 * message, call send().
 * <p>
 * An example based on com.illposed.osc.test.OSCPortTest::testMessageWithArgs()
 * :
 * 
 * <pre>
 * OSCPort sender = new OSCPort();
 * Object args[] = new Object[2];
 * args[0] = new Integer(3);
 * args[1] = &quot;hello&quot;;
 * OSCMessage msg = new OSCMessage(&quot;/sayhello&quot;, args);
 * try {
 * 	sender.send(msg);
 * } catch (Exception e) {
 * 	showError(&quot;Couldn't send&quot;);
 * }
 * </pre>
 * 
 * @author
 * @version 1.0
 */
public class OSCPortOut extends OSCPort {

	protected InetAddress address;

	/**
	 * Create an OSCPort that sends to newAddress, newPort
	 * 
	 * @param newAddress
	 *            InetAddress
	 * @param newPort
	 *            int
	 */
	public OSCPortOut(InetAddress newAddress, int newPort)
			throws SocketException {
		socket = new DatagramSocket();
		address = newAddress;
		port = newPort;
	}

	/**
	 * Create an OSCPort that sends to newAddress, on the standard SuperCollider
	 * port
	 * 
	 * @param newAddress
	 *            InetAddress
	 * 
	 *            Default the port to the standard one for SuperCollider
	 */
	public OSCPortOut(InetAddress newAddress) throws SocketException {
		this(newAddress, defaultSCOSCPort());
	}

	/**
	 * Create an OSCPort that sends to localhost, on the standard SuperCollider
	 * port Default the address to localhost Default the port to the standard
	 * one for SuperCollider
	 */
	public OSCPortOut() throws UnknownHostException, SocketException {
		this(InetAddress.getLocalHost(), defaultSCOSCPort());
	}

	/**
	 * Send an osc packet (message or bundle) to the receiver I am bound to.
	 * 
	 * @param aPacket
	 *            OSCPacket
	 */
	public void send(OSCPacket aPacket) throws IOException {

		byte[] byteArray = aPacket.getByteArray();
		final DatagramPacket packet = new DatagramPacket(byteArray,
				byteArray.length, address, port);
		new Thread() {
			@Override
			public void run() {
				try {
					socket.send(packet);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
	}
}
