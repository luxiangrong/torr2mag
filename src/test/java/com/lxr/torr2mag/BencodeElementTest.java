package com.lxr.torr2mag;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.lxr.torr2mag.bencode.DictionaryElement;
import com.lxr.torr2mag.bencode.IntElement;
import com.lxr.torr2mag.bencode.ListElement;
import com.lxr.torr2mag.bencode.StringElement;

public class BencodeElementTest {

	@Test
	public void testStringEncode() {
		StringElement stringElement = new StringElement("string");
		assertEquals("6:string", stringElement.toEncodedString());

		stringElement = new StringElement("bencoding");
		assertEquals("9:bencoding", stringElement.toEncodedString());

		stringElement = new StringElement("");
		assertEquals("", stringElement.toEncodedString());

		stringElement = new StringElement(null);
		assertEquals("", stringElement.toEncodedString());
	}

	@Test
	public void testIntEncode() {
		IntElement intElement = new IntElement(123);
		assertEquals("i123e", intElement.toEncodedString());

		intElement = new IntElement(0);
		assertEquals("i0e", intElement.toEncodedString());

		intElement = new IntElement(-123);
		assertEquals("i-123e", intElement.toEncodedString());

		intElement = new IntElement(-0);
		assertEquals("i0e", intElement.toEncodedString());
	}

	@Test
	public void testListEncode() {
		StringElement stringElement = new StringElement("string");
		IntElement intElement = new IntElement(123);

		ListElement listElement = new ListElement();
		listElement.addElement(stringElement);
		listElement.addElement(intElement);

		assertEquals("l6:stringi123ee", listElement.toEncodedString());

	}

	@Test
	public void testDictionaryEncode() {
		StringElement stringElement = new StringElement("string");
		IntElement intElement = new IntElement(123);

		DictionaryElement dictionaryElement = new DictionaryElement();
		dictionaryElement.addElement(stringElement, intElement);

		assertEquals("d6:stringi123ee", dictionaryElement.toEncodedString());
	}
}
