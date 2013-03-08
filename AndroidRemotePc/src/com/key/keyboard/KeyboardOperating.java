package com.key.keyboard;

public class KeyboardOperating {
	int press;
	int keycode;
	KeyboardOperating(int _press,int _keycode){
		this.press = _press;
		this.keycode = _keycode;
	}
	KeyboardOperating(){
		this.keycode = -1000;
	}
	public int getPress() {
		return press;
	}
	public void setPress(int _press) {
		this.press = _press;
	}
	public int getKeycode() {
		return keycode;
	}
	public void setKeycode(int _keycode) {
		this.keycode = _keycode;
	}
}
