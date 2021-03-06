package com.lxr.torr2mag.bencode;

public class IntElement implements Element {

	private int value;

	public IntElement(int value) {
		this.value = value;
	}

	public String toEncodedString() {
		return "i" + this.value + "e";
	}

	@Override
	public boolean equals(Object obj) {
		IntElement other = (IntElement) obj;
		return other.value == value;
	}

}
