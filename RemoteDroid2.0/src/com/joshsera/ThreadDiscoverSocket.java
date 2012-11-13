package com.joshsera;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * ���Ϸ��ֽ�������
 * wait for packet
 */
public class ThreadDiscoverSocket extends Thread {
	//TAG
	private static final String TAG = "DiscoverThread";
	//���泤��
	private static int BUFFER_LENGTH = 1024;
	//��㲥�͵�ַ
	public static String MULTICAST_ADDRESS = "230.6.6.6";
	//ID����
	private static final String ID_REQUEST = "RemoteDroid:AnyoneHome";
	//ID������Ӧ
	private static final String ID_REQUEST_RESPONSE = "RemoteDroid:ImHome";
	
	//
	private int port = 57111;
	private MulticastSocket socket;
	private DatagramSocket inSocket;
	private DiscoverListener listener;
	
	public ThreadDiscoverSocket(DiscoverListener listener) {
		this.listener = listener;
	}
	
	public ThreadDiscoverSocket(int port, DiscoverListener listener) {
		this.port = port;
		this.listener = listener;
	}
	
	public void run() {
		try {
			this.socket = new MulticastSocket(this.port);
			this.socket.joinGroup(InetAddress.getByName(MULTICAST_ADDRESS));
			this.inSocket = new DatagramSocket(this.port+1);
			this.sendIDRequest();
			this.waitForResponse();
		} catch (IOException e) {
			
		} catch (InterruptedException e) {
			
		}
	}
	
	public void closeSocket() {
		this.socket.close();
		this.inSocket.close();
	}
	
	private void sendIDRequest() throws IOException, InterruptedException {
		byte[] b = ID_REQUEST.getBytes();
		DatagramPacket packet = new DatagramPacket(b, b.length);
		packet.setAddress(InetAddress.getByName(MULTICAST_ADDRESS));
		packet.setPort(this.port);
		this.socket.send(packet);
		Thread.sleep(500);
	}
	
	private void waitForResponse() throws IOException {
		byte[] b = new byte[BUFFER_LENGTH];
		DatagramPacket packet = new DatagramPacket(b, b.length);
		//Log.d(TAG, "Going to wait for packet");
		while (true) {
			this.inSocket.receive(packet);
			this.handleReceivedPacket(packet);
		}
	}
	
	// 
	
	private void handleReceivedPacket(DatagramPacket packet) {
		String data = new String(packet.getData());
		//Log.d(TAG, "Got packet! data:"+data);
		//Log.d(TAG, "IP:"+packet.getAddress().getHostAddress());
		if (data.substring(0, ID_REQUEST_RESPONSE.length()).compareTo(ID_REQUEST_RESPONSE) == 0) {
			//�Ѿ�������Ӧ������listener
			// We've received a response. Notify the listener
			this.listener.onAddressReceived(packet.getAddress().getHostAddress());
		}
	}
	
	//
	/**
	 * �����ӿڣ�������ʵ��
	 */
	public static interface DiscoverListener {
		void onAddressReceived(String address);
	}
}
