package com.lxr.torr2mag.bencode;

import java.util.ArrayList;
import java.util.List;

public class ListElement implements Element {

	private List<Element> value;

	public ListElement(List<Element> value) {
		this.value = value;
	}

	public ListElement() {
		this(new ArrayList<Element>());
	}

	public ListElement(Element element) {
		this();
		addElement(element);
	}

	public void addElement(Element element) {
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

}
