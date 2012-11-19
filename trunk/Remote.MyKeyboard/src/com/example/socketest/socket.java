package com.example.socketest;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class socket {
	private char[] cameraid;

	void SendMessage() {
		try {
			Socket socket = new Socket("192.168.10.124", 1234);
			PrintWriter out = new PrintWriter(new BufferedWriter(
					new OutputStreamWriter(socket.getOutputStream())), true);
			out.println(getCameraid());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setCameraid(char[] cameraid) {
		this.cameraid = cameraid;
	}

	public char[] getCameraid() {
		return cameraid;
	}
}
