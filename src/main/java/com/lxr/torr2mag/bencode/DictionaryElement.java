package com.lxr.torr2mag.bencode;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class DictionaryElement implements Element {

	private Map<StringElement, Element> value;

	public DictionaryElement() {
		value = new HashMap<StringElement, Element>();
	}

	@Override
	public String toEncodedString() {
		StringBuffer buffer = new StringBuffer("d");
		for (Entry<StringElement, Element> entry : value.entrySet()) {
			StringElement key = entry.getKey();
			Element value = entry.getValue();
			buffer.append(key.toEncodedString());
			buffer.append(value.toEncodedString());
		}
		buffer.append("e");
		return buffer.toString();
	}

	public void addElement(StringElement stringElement, Element element) {
		value.put(stringElement, element);
	}

}
