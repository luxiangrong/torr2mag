package com.lxr.torr2mag.bencode;

public interface Element {
	String toEncodedString();
	boolean equals(Object obj);
}
