package com.lxr.torr2mag.bencode;

import java.util.ArrayList;
import java.util.List;

public class ListElement<T extends Element> implements Element {

	private List<T> value;

	public ListElement(List<T> value) {
		this.value = value;
	}

	public ListElement() {
		this(new ArrayList<T>());
	}

	public ListElement(T element) {
		this();
		addElement(element);
	}

	public void addElement(T element) {
		value.add(element);
	}

	@Override
	public String toEncodedString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("l");
		for (Element element : value) {
			buffer.append(element.toEncodedString());
		}
		buffer.append("e");
		return buffer.toString();
	}

	@Override
	public boolean equals(Object obj) {
		ListElement other = (ListElement) obj;
		return other.value.equals(value);
	}

	public List<T> getValue() {
		return value;
	}

}
