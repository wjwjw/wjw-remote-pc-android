package com.key.keyboard;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;

import android.util.Log;

import com.key.DataSettings;
import com.key.socketdata.OSCMessage;
import com.key.socketdata.OSCPort;
import com.key.socketdata.OSCPortOut;


public class GlobalVariables {
	
	private static final String TAG = "GlobalVariables";
	static int scaleWidth = 0;
	static int scaleHeight = 0;
	public static float Ayimuth = 0.00f; // 绕y轴旋转的角度
	public static float Aximuth = 0.00f; // 绕x轴旋转的角度
	public static int pos_x = -100000;
	static int pos_y = -100000; // 记录上一次鼠标的坐标
	static int dir = 2;
	public static boolean glviewtouchdown = false;
	
	static int MAX_NUM_BUTTON = 100;
	public static int numofbutton = 0;
	
	public static Vector<MyButton> buttons = new Vector<MyButton>();
	public static Vector<MyButtonData> buttondatas = new Vector<MyButtonData>();

	//传送freekeyboard事件的消息队列
	public static ConcurrentLinkedQueue<KeyboardOperating> KeycodesQueue =
		new ConcurrentLinkedQueue<KeyboardOperating>();
	
	static String menutext[] = {
			"Mouse", 
			"Keyboard",
			"KeyDesign", 
			"Toothpaste",
			"Ice Crea"
	};
	/**
	 * 传送freekeyboard事件的消息队列
	 */
	public static void KeycodesQueueBeginListen(){
		new Thread( new Runnable() {   
			OSCPortOut sender;
		    public void run() {     
				try {
					//
					sender = new OSCPortOut(InetAddress
							.getByName(DataSettings.ip), OSCPort
							.defaultSCOSCPort());
				} catch (Exception ex) {
					Log.d(TAG, ex.toString());
				}
		    	while(true){
		    		if(GlobalVariables.KeycodesQueue.isEmpty()) continue;
		    		KeyboardOperating optemp = GlobalVariables.KeycodesQueue.poll(); 
					Object[] args = new Object[2];
					args[0] = optemp.isup; /* key up or not*/
					args[1] = optemp.keycode;// (int)c;
					OSCMessage msg = new OSCMessage("/freekeyboard", args);
					Log.e("optemp.isup"+args[0],"optemp.keycode"+args[1]);
					try {
						sender.send(msg);
					} catch (IOException e) {
					}
		    	}
		     }            
		}).start();
	}
	/**
	 * 	 传入改变opengl绘图数据
	 * @param x touch.event.x
	 * @param y touch.event.y
	 */
	public static void judTouchEvent(int x, int y) {
//		Log.e("X = " + x, "Y = " + y);
		if (pos_x != -100000) {
			if (pos_x < x) {
				if (x - pos_x > dir) {
					Ayimuth += 1.5;
					if (Ayimuth > 90.0f) {
						Ayimuth = 90.0f;
					}
					pos_x = x;
				}
			}
			if (pos_x > x) {
				if (pos_x - x > dir) {
					Ayimuth -= 1.5;
					if (Ayimuth < -90.0f) {
						Ayimuth = -90.0f;
					}
					pos_x = x;
				}
			}
			if (pos_y < y) {
				if (y - pos_y > dir) {
					Aximuth += 1.5;
					if (Aximuth > 90.0f) {
						Aximuth = 90.0f;
					}
					pos_y = y;
				}
			}
			if (pos_y > y) {
				if (pos_y - y > dir) {
					Aximuth -= 1.5;
					if (Aximuth < -90.0f) {
						Aximuth = -90.0f;
					}
					pos_y = y;
				}
			}
		} else {
			pos_x = x;
			pos_y = y;
		}

	}
}

