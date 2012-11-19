package com.key.keyboard;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.util.Log;
import android.view.View;

import com.key.R;

public class KeyboardUtil {
	private static String TAG = "KeyboardUtil";
	private Context ctx;
	private Activity act;
	private KeyboardView keyboardView;
	private Keyboard k1;// 字母键盘
	private Keyboard k2;// 数字键盘
	public boolean isnun = false;// 是否数据键盘
	public boolean isupper = false;// 是否大写
	
	public String lowersymbol = "1234567890-=\\[];',./abcdefghijklmnopqrstuvwxyz";
	public String uppersymbol = "!@#$%^&*()_+|{}:\"<>?ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	//TODO 改键值啊~亲
	public int KeyCodesSymbol[] = {

			// 1 - 0
			49, 50, 51, 52, 53, 54, 55, 56, 57, 48,
			// -=\\[];',./
			45, 61, 92, 91, 93, 59, 222, 44, 46, 47,
			// A - Z
			65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81,
			82, 83, 84, 85, 86, 87, 88, 89, 90, };



	private static boolean isShiftDown = false;
	private static boolean isCtrlDown = false;
	private static boolean isWinDown = false;
	private static boolean isAltDown = false;

	public KeyboardUtil(Activity act, Context ctx) {
		this.act = act;
		this.ctx = ctx;
		k1 = new Keyboard(ctx, R.layout.my_letter_keyboard);
		k2 = new Keyboard(ctx, R.layout.my_num_keyboard);
		keyboardView = (KeyboardView) act.findViewById(R.id.keyboard_view);
		keyboardView.setKeyboard(k1);
		keyboardView.setEnabled(true);
		keyboardView.setPreviewEnabled(true);
		keyboardView.setOnKeyboardActionListener(listener);
	}

	private OnKeyboardActionListener listener = new OnKeyboardActionListener() {
		@Override
		public void swipeUp() {
		}

		@Override
		public void swipeRight() {
		}

		@Override
		public void swipeLeft() {
		}

		@Override
		public void swipeDown() {
		}

		@Override
		public void onText(CharSequence text) {
		}

		@Override
		public void onRelease(int primaryCode) {
		}

		@Override
		public void onPress(int primaryCode) {
		}

		@Override
		public void onKey(int primaryCode, int[] keyCodes) {
			Log.d("TAG","onKey"+primaryCode);

			if (primaryCode == Keyboard.KEYCODE_MODE_CHANGE) {// 数字键盘切换
				if (isnun) { 
					isnun = false;
					keyboardView.setKeyboard(k1);
				} else {
					isnun = true;
					keyboardView.setKeyboard(k2);
				}
				return;
			}

			if (primaryCode == -12) {// shift
				changeKey();
				keyboardView.setKeyboard(k1);
			}

			if (primaryCode > 0) {// not sticky & not specil
				pushKeyboard(primaryCode, 2);// Click
			} else if (primaryCode==-16 || primaryCode > -12) {// esc || specil
				pushspecialkey(primaryCode, 2);// click
			}  else if (primaryCode == -12) { // shift sticky
				if (isShiftDown) {
					pushspecialkey(primaryCode, 0);// Release
				} else {
					pushspecialkey(primaryCode, 1);// press
				}
				isShiftDown = !isShiftDown;
			} else if (primaryCode == -13) { // ctrl sticky
				if (isCtrlDown) {
					pushspecialkey(primaryCode, 0);// Release
				} else {
					pushspecialkey(primaryCode, 1);// press
				}
				isCtrlDown = !isCtrlDown;
			} else if (primaryCode == -14) { // win sticky
				if (isWinDown) {
					pushspecialkey(primaryCode, 0);// Release
				} else {
					pushspecialkey(primaryCode, 1);// press
				}
				isWinDown = !isWinDown;
			} else if (primaryCode == -15) { // Alt sticky
				if (isAltDown) {
					pushspecialkey(primaryCode, 0);// Release
				} else {
					pushspecialkey(primaryCode, 1);// press
				}
				isAltDown = !isAltDown;
			}

		}
	};
	/**
	 * 键盘大小写切换
	 */
	private void changeKey() {
		List<Key> keylist = k1.getKeys();
		if (isupper) {// 大写切换小写
			isupper = false;
			for (Key key : keylist) {
				if (key.label == null)
					continue;
				if(key.label.length()>1)
					continue;
				char keychar = key.label.charAt(0);
				
				int index = -1;
				int n = uppersymbol.length();
				for (int i = 0; i < n; i++) {
					if (uppersymbol.charAt(i) == keychar) {
						index = i;
						break;
					}
				}
				if (index > -1) {
					key.label = lowersymbol.substring(index, index + 1);
				}
			}
		} else {// 小写切换大写
			isupper = true;
			for (Key key : keylist) {
				if (key.label == null)
					continue;
				if(key.label.length()>1)
					continue;
				char keychar = key.label.charAt(0);
				
				int index = -1;
				int n = lowersymbol.length();
				for (int i = 0; i < n; i++) {
					if (lowersymbol.charAt(i) == keychar) {
						index = i;
						break;
					}
				}
				if (index > -1) {
					key.label = uppersymbol.substring(index, index + 1);
				}
			}
		}
	}

	public void showKeyboard() {
		int visibility = keyboardView.getVisibility();
		if (visibility == View.GONE || visibility == View.INVISIBLE) {
			keyboardView.setVisibility(View.VISIBLE);
		}
	}

	public void hideKeyboard() {
		int visibility = keyboardView.getVisibility();
		if (visibility == View.VISIBLE) {
			keyboardView.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * 把freekayboard事件push进消息队列中 Keycodes>0
	 * 
	 * @param press
	 * @param Keycodes
	 */
	private void pushKeyboard(int Keycodes, int press) {

		KeyboardOperating keyOP_temp = new KeyboardOperating();
		keyOP_temp.setPress(press);
		keyOP_temp.setKeycode(Keycodes);
		if (keyOP_temp.getKeycode() == -1000)
			return;
		GlobalVariables.KeycodesQueue.add(keyOP_temp);
	}

	/**
	 * Keycodes<0
	 * 
	 * @param press		1<br>
	 * @param release	0<br>
	 * @param click		2<br>
	 * 
	 * <br>
	 * Specil Key Codes <br>
	 * -1 shift <br>
	 * -2 123# <br>
	 * -3 cancle# <br>
	 * -4 Enter <br>
	 * -5 backspace <br>
	 * -6 Alt<br>
	 * -7 Tab <br>
	 * -8 Caps Lock <br>
	 * -9 Ctrl <br>
	 * -10 Win <br>
	 * -11 Menu<br>
	 * -12 Shift sticky <br>
	 * -13 Ctrl Sticky <br>
	 * -14 Win Sticky <br>
	 * -15 Alt Sticky<br>
	 * -16 Esc<br>
	 */
	private void pushspecialkey(int Keycodes, int press) {

		switch (Keycodes) {
		case -1://shift
			Keycodes = 16;
			break;
		case -2://123#
			Keycodes = -1000;
			break;
		case -3://cancle#
			Keycodes = -1000;
			break;
		case -4://Enter
			Keycodes = 10;
			break;
		case -5://backspace
			Keycodes = 8;
			break;
		case -6://Alt
			Keycodes = 18;
			break;
		case -7://Tab
			Keycodes = 9;
			break;
		case -8://Caps Lock
			Keycodes = 20;
			break;
		case -9://Ctrl
			Keycodes = 17;
			break;
		case -10://Win
			Keycodes = 524;
			break;
		case -11://Menu
			Keycodes = 525;
			break;
		case -12://Shift sticky
			Keycodes = 16;
			break;
		case -13://Ctrl Sticky
			Keycodes = 17;
			break;
		case -14://Win Sticky
			Keycodes = 524;
			break;
		case -15://Alt Sticky
			Keycodes = 18;
			break;
		case -16://Esc
			Keycodes = 27;
			break;
		default:
			Keycodes = -1000;
			break;
		}
		KeyboardOperating keyOP_temp = new KeyboardOperating();
		keyOP_temp.setPress(press);
		keyOP_temp.setKeycode(Keycodes);
		if (keyOP_temp.getKeycode() == -1000)
			return;
		GlobalVariables.KeycodesQueue.add(keyOP_temp);

	}

}
