package com.example.socketest;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class KeyboardUtil {
	private Context ctx;
	private Activity act;
	private KeyboardView keyboardView;
	private Keyboard k1;// ×ÖÄ¸¼üÅÌ
	private Keyboard k2;// Êý×Ö¼üÅÌ
	public boolean isnun = false;// ÊÇ·ñÊý¾Ý¼üÅÌ
	public boolean isupper = false;// ÊÇ·ñ´óÐ´
	
	public String lowersymbol = "`1234567890-=\\[];',./abcdefghijklmnopqrstuvwxyz";
	public int loweransi[] = { 96, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 45,
			61, 92, 91, 93, 59, 39, 44, 46, 47, 97, 98, 99, 100, 101, 102, 103,
			104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116,
			117, 118, 119, 120, 121, 122 };
	public String uppersymbol = "~!@#$%^&*()_+|{}:\"<>?ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public int upperansi[] = { 126, 33, 64, 35, 36, 37, 94, 38, 42, 40, 41, 95,
			43, 124, 123, 125, 58, 34, 60, 62, 63, 65, 66, 67, 68, 69, 70, 71,
			72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88,
			89, 90 };

	private EditText ed;

	public KeyboardUtil(Activity act, Context ctx, EditText edit) {
		this.act = act;
		this.ctx = ctx;
		this.ed = edit;
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
			Editable editable = ed.getText();
			int start = ed.getSelectionStart();
			if (primaryCode == Keyboard.KEYCODE_CANCEL) {// Íê³É
				hideKeyboard();
			} else if (primaryCode == Keyboard.KEYCODE_DELETE) {// »ØÍË
				if (editable != null && editable.length() > 0) {
					if (start > 0) {
						editable.delete(start - 1, start);
					}
				}
			} else if (primaryCode == Keyboard.KEYCODE_SHIFT) {// ´óÐ¡Ð´ÇÐ»»
				changeKey();
				keyboardView.setKeyboard(k1);
			} else if (primaryCode == Keyboard.KEYCODE_MODE_CHANGE) {// Êý×Ö¼üÅÌÇÐ»»
				if (isnun) {
					isnun = false;
					keyboardView.setKeyboard(k1);
				} else {
					isnun = true;
					keyboardView.setKeyboard(k2);
				}
			} else {
				editable.insert(start, Character.toString((char) primaryCode));
			}
		}
	};

	/**
	 * ¼üÅÌ´óÐ¡Ð´ÇÐ»»
	 */
	private void changeKey() {
		List<Key> keylist = k1.getKeys();
		if (isupper) {// ´óÐ´ÇÐ»»Ð¡Ð´
			isupper = false;
			for (Key key : keylist) {
				if (key.label == null)
					continue;
				int index = -1;
				for (int i = 0; i < upperansi.length; i++) {
					if (upperansi[i] == key.codes[0]) {
						index = i;
						break;
					}
				}
				if (index > -1) {
					key.label = lowersymbol.substring(index, index+1);
					key.codes[0] = loweransi[index];
				}
			}
		} else {// Ð¡Ð´ÇÐ»»´óÐ´
			isupper = true;
			for (Key key : keylist) {
				Log.d("",""+key.label+ " "+ key.codes[0]);
				if (key.label == null)
					continue;
				int index = -1;
				for (int i = 0; i < loweransi.length; i++) {
					if (loweransi[i] == key.codes[0]) {
						index = i;
						break;
					}
				}
				if (index > -1) {
					key.label = uppersymbol.substring(index, index+1);
					key.codes[0] = upperansi[index];
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

	private boolean isword(String str) {
		String wordstr = "abcdefghijklmnopqrstuvwxyz";
		if (wordstr.indexOf(str.toLowerCase()) > -1) {
			return true;
		}
		return false;
	}

}
