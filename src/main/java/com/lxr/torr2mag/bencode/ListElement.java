package com.lxr.torr2mag.bencode;

import java.util.ArrayList;
import java.util.List;

public class ListElement implements Element {

	private List<Element> delegate;

	public ListElement() {
		this.delegate = new ArrayList<Element>();
	}

	public ListElement(Element element) {
		this();
		this.addElement(element);
	}

	public void addElement(Element element) {
		delegate.add(element);
	}

	@Override
	public String toEncodedString() {
		String result = "l";
		for (Element element : delegate) {
			result += element.toEncodedString();
		}
		return result += "e";
	}

}
