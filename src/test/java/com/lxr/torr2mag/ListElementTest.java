package com.lxr.torr2mag;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.lxr.torr2mag.bencode.Element;
import com.lxr.torr2mag.bencode.ListElement;
import com.lxr.torr2mag.bencode.StringElement;

@RunWith(MockitoJUnitRunner.class)
public class ListElementTest {

	private ListElement listElement;

	@Mock
	private List<Element> mockedValue = new ArrayList<Element>();

	@Before
	public void setUp() {
		listElement = new ListElement(mockedValue);
	}

	@Test
	public void testAddElement() {
		StringElement stringElement = new StringElement("string");
		listElement.addElement(stringElement);
		Mockito.verify(mockedValue).add(stringElement);
	}

}
