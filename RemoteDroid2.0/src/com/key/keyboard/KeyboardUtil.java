package com.key.keyboard;

import java.util.List;

import android.R.string;
import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.text.Editable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

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
	
	public String lowersymbol = "`1234567890-=\\[];',./abcdefghijklmnopqrstuvwxyz";
	public String uppersymbol = "~!@#$%^&*()_+|{}:\"<>?ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	//TODO 改键值啊~亲
	public int KeyCodesSymbol[] = {
			// ~
			192,
			// 1 - 0
			49, 50, 51, 52, 53, 54, 55, 56, 57, 48,
			// -=\\[];',./
			45, 61, 92, 91, 93, 59, 222, 44, 46, 47,
			// A - Z
			65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81,
			82, 83, 84, 85, 86, 87, 88, 89, 90, };



	private static boolean isShiftUp = false;
	private static boolean isCtrlUp = false;
	private static boolean isWinUp = false;
	private static boolean isAltUp = false;

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
			// Log.e("onKey",""+primaryCode);
			// Editable editable = ed.getText();
			// int start = ed.getSelectionStart();
			// if (primaryCode == Keyboard.KEYCODE_CANCEL) {// 完成
			// hideKeyboard();
			// }
			// else if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
			// if (editable != null && editable.length() > 0) {
			// if (start > 0) {
			// editable.delete(start - 1, start);
			// }
			// }
			// }
			// else
			if (primaryCode == -12) {
				changeKey();
				keyboardView.setKeyboard(k1);
			}
			if (primaryCode == Keyboard.KEYCODE_MODE_CHANGE) {// 数字键盘切换
				if (isnun) {
					isnun = false;
					keyboardView.setKeyboard(k1);
				} else {
					isnun = true;
					keyboardView.setKeyboard(k2);
				}
			} else {
				if (primaryCode > 0) {
					pushKeyboard(primaryCode, 0);
					pushKeyboard(primaryCode, 1);
				} else if (primaryCode > -12) {
					pushspecialkey(primaryCode, 0);
					pushspecialkey(primaryCode, 1);
				} else if (primaryCode == -12) { // shift sticky
					if (isShiftUp) {
						pushspecialkey(primaryCode, 0);
						isShiftUp = !isShiftUp;
					} else {
						pushspecialkey(primaryCode, 1);
						isShiftUp = !isShiftUp;
					}
				} else if (primaryCode == -13) { // ctrl sticky
					if (isCtrlUp) {
						pushspecialkey(primaryCode, 0);
						isCtrlUp = !isCtrlUp;
					} else {
						pushspecialkey(primaryCode, 1);
						isCtrlUp = !isCtrlUp;
					}
				} else if (primaryCode == -13) { // ctrl sticky
					if (isWinUp) {
						pushspecialkey(primaryCode, 0);
						isWinUp = !isWinUp;
					} else {
						pushspecialkey(primaryCode, 1);
						isWinUp = !isWinUp;
					}
				} else if (primaryCode == -13) { // ctrl sticky
					if (isAltUp) {
						pushspecialkey(primaryCode, 0);
						isAltUp = !isAltUp;
					} else {
						pushspecialkey(primaryCode, 1);
						isAltUp = !isAltUp;
					}
				}
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
	 * @param Ispress
	 * @param Keycodes
	 */
	private void pushKeyboard(int Keycodes, int Isup) {

		KeyboardOperating keyOP_temp = new KeyboardOperating();
		keyOP_temp.setIsup(Isup);
		keyOP_temp.setKeycode(Keycodes);
		if (keyOP_temp.getKeycode() == -1000)
			return;
		GlobalVariables.KeycodesQueue.add(keyOP_temp);
	}

	/**
	 * Keycodes<0
	 * 
	 * @param keyPress:1 
	 * @param keyRelease:0
	 * 
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
	 * 
	 */
	private void pushspecialkey(int Keycodes, int Isup) {

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
			Keycodes = 32;
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
		default:
			Keycodes = -1000;
			break;
		}
		KeyboardOperating keyOP_temp = new KeyboardOperating();
		keyOP_temp.setIsup(Isup);
		keyOP_temp.setKeycode(Keycodes);
		if (keyOP_temp.getKeycode() == -1000)
			return;
		GlobalVariables.KeycodesQueue.add(keyOP_temp);

	}

}
