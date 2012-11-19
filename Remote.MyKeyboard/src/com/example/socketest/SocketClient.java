package com.example.socketest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import android.util.Log;

public class SocketClient {
	public boolean mClientFlag = false;
	private static Socket client;

	public SocketClient() {
		Log.d("WineStock", "WineStock SocketClient()");
		client = new Socket();
	}

	public SocketClient(String HostName, int iPort) {
		client = new Socket();
		SocketAddress socketAddress = new InetSocketAddress(HostName, iPort);
		try {
			Log.d("WineStock", "WineStock SocketClient connect");
			client.connect(socketAddress, 3000);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.d("WineStock", "WineStock SocketClient IOException");

		} catch (IllegalArgumentException e1) {
			// TODO: handle exception
			Log.d("WineStock",
					"WineStock SocketClient IllegalArgumentException ");

		}

		return;
	}

	public boolean SocketConnect(String HostName, int iPort) {

		SocketAddress socketAddress = new InetSocketAddress(HostName, iPort);
		try {
			Log.d("WineStock", "WineStock SocketConnect connect ");

			client.connect(socketAddress, 3000);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.d("WineStock", "WineStock SocketConnect IOException ");
			return false;

		} catch (IllegalArgumentException e1) {
			// TODO: handle exception
			Log.d("WineStock",
					"WineStock SocketConnect IllegalArgumentException ");
			return false;
		}

		return true;
	}

	public void SocketClose() {
		try {
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.d("WineStock", "socket close error" + e.getMessage());
		}

	}

	// 建立连接
	public static boolean SClient(Object SendModel)
			throws UnknownHostException, IOException {
		boolean bRet = false;
		try {
			if (client.isConnected()) {
				// 发送请求

				sendMessage(SendModel);
				bRet = true;
			}
		} catch (UnknownHostException e) {
			Log.d("WineStock", "socket SClienterror" + e.getMessage());
		} catch (IOException e) {
			Log.d("WineStock", "socket SClienterror" + e.getMessage());
		}

		return bRet;

	}

	// 发送请求
	private static void sendMessage(Object model) throws IOException {
		// boolean bRet = false;
		try {
			PrintWriter out = new PrintWriter(client.getOutputStream());
			out.print(objToJsonString(model).replace("\n", " "));
			out.flush();
			// bRet = true;
		} catch (IOException e) {

		}

		return;
	}

	// 接收服务器数据
	public static String readMessage() throws IOException {
		String str = "";
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					client.getInputStream()));
			str = br.readLine().replace("{", "").replace("}", "")
					.replace("\"", "");
		} catch (IOException e) {

		}

		return str;

	}

	// 将Java Object对象转换为Json对象
	private static String objToJsonString(Object obj) {
		// 初始化返回值
		String json = "str_empty";
		if (obj == null) {
			return json;
		}
		StringBuilder buff = new StringBuilder();
		Field fields[] = obj.getClass().getFields();
		try {
			buff.append("[");
			buff.append("{");
			int i = 0;
			for (Field field : fields) {
				if (i != 0) {
					buff.append(",");
				}
				buff.append(field.getName());
				buff.append(":");
				buff.append("\"");
				buff.append(field.get(obj) == null ? "" : field.get(obj));
				buff.append("\"");
				i++;
			}
			buff.append("}");
			buff.append("]");
			json = buff.toString();
		} catch (Exception e) {
			throw new RuntimeException("cause:" + e.toString());
		}
		return json;
	}
}