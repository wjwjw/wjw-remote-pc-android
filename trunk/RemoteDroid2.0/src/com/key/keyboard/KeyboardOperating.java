package com.key.keyboard;

public class KeyboardOperating {
	int isup;
	int keycode;
	KeyboardOperating(int _isup,int _keycode){
		this.isup = _isup;
		this.keycode = _keycode;
	}
	KeyboardOperating(){
		this.keycode = -1000;
	}
	public int getIsup() {
		return isup;
	}
	public void setIsup(int isup) {
		this.isup = isup;
	}
	public int getKeycode() {
		return keycode;
	}
	public void setKeycode(int keycode) {
		this.keycode = keycode;
	}
}
