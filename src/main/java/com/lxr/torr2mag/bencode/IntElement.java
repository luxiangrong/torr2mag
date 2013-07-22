package com.lxr.torr2mag.bencode;

public class IntElement {

	private int value;

	public IntElement(int value) {
		this.value = value;
	}

	public String toEncodedString() {
		return "i" + this.value + "e";
	}

}
