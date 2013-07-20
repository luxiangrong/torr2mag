package com.lxr.torr2mag.bencode;

public class StringElement implements Element{

	private String value;

	public StringElement(String value) {
		this.value = value;
	}

	public String toEncodedString() {
		if (null == value || "".equals(value)) {
			return "";
		}

		int length = value.length();

		return length + ":" + value;

	}

}
