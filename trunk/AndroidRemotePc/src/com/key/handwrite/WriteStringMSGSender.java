package com.key.handwrite;

import java.net.InetAddress;

import android.util.Log;

import com.key.DataSettings;
import com.key.socketdata.OSCMessage;
import com.key.socketdata.OSCPort;
import com.key.socketdata.OSCPortOut;

public class WriteStringMSGSender {
	private static final String TAG = "WriteStringMSGSender";
	private OSCPortOut sender;
	
	public void sendStringEvent(String str) {
		try {
			this.sender = new OSCPortOut(
					InetAddress.getByName(DataSettings.ip),
					OSCPort.defaultSCOSCPort());
		} catch (Exception ex) {
			Log.d(TAG, ex.toString());
		}
		
		char c = str.charAt(0);
		int i_for_Unicode = c;
		
		Object[] args = new Object[1];
		args[0] = i_for_Unicode;
		//
		OSCMessage msg = new OSCMessage("/string", args);
		Log.e(TAG,"/string:"+str);
		try {
			this.sender.send(msg);
		} catch (Exception ex) {
			Log.d(TAG, ex.toString());
		}
	}

}
