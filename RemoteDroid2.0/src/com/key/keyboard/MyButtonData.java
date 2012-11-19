package com.key.keyboard;

public class MyButtonData {
	private int left;
	private int top;
	private String name;
	private int keycodes;
	
	public MyButtonData() {
		left = 0;
		top = 0;
		name = "";
		keycodes = -1000;
	}
	
	public int getLeft() {
		return left;
	}

	public void setLeft(int left) {
		this.left = left;
	}

	public int getTop() {
		return top;
	}

	public void setTop(int top) {
		this.top = top;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getKeycodes() {
		return keycodes;
	}

	public void setKeycodes(int keycodes) {
		this.keycodes = keycodes;
	}

}
