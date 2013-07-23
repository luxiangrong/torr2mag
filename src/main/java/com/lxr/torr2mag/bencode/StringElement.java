package com.lxr.torr2mag.bencode;

public class StringElement implements Element {

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

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof String) {
			return obj.equals(value);
		}
		StringElement other = (StringElement) obj;
		return other.value.equals(value);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

}
