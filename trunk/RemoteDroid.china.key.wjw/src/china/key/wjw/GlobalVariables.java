package china.key.wjw;

import java.util.Vector;

import china.key.keyboard.MyButton;


public class GlobalVariables {
	
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
	
	
	public static Vector<Integer> buttonleft = new Vector<Integer>();
	public static Vector<Integer> buttontop = new Vector<Integer>();
	public static Vector<String> buttonname = new Vector<String>();
	public static Vector<MyButton> buttons = new Vector<MyButton>();

	


	
	static String menutext[] = {
			"Mouse", 
			"Keyboard",
			"KeyDesign", 
			"Toothpaste",
			"Ice Crea"
	};
	
	
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
