package com.lxr.torr2mag.bencode;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.lxr.torr2mag.bencode.exception.ParseException;

public class Bencode {

	
	public String parseString(String encodedString) {
		return parseString(new ByteArrayInputStream(encodedString.getBytes()));
	}

	public String parseString(InputStream is) {
		if (!is.markSupported()) {
			throw new ParseException("Unsupported inputStream");
		}
		try {
			StringBuffer buffer = new StringBuffer();
			int c = is.read();
			while (c != -1 && c != ':') {
				buffer.append((char) c);
				c = is.read();
			}
			int stringLength = Integer.parseInt(buffer.toString(), 10);

			if (stringLength <= 0) {
				throw new ParseException("Invalid string format");
			}

			byte[] byteArray = new byte[stringLength];
			int realLength = is.read(byteArray);

			if (realLength < stringLength) {
				throw new ParseException("Invalid string format");
			}

			return new String(byteArray);

		} catch (IOException | NumberFormatException e) {
			throw new ParseException("Invalid string format", e);
		}
	}

	public int parseInt(String string) {
		return parseInt(new ByteArrayInputStream(string.getBytes()));
	}

	public int parseInt(InputStream is) {
		if (!is.markSupported()) {
			throw new ParseException("Unsupported inputStream");
		}

		try {
			is.reset();
			StringBuffer buffer = new StringBuffer();
			int c = is.read();
			if (c != 'i') {
				throw new ParseException("Invalid int format");
			}
			c = is.read();
			while (c != -1 && c != 'e') {
				buffer.append((char) c);
				c = is.read();
			}

			String intAsString = buffer.toString();
			if (intAsString.matches("(^0.+)|(^-0.*)")) {
				throw new ParseException("Invalid int format");
			}

			return Integer.parseInt(buffer.toString());
		} catch (IOException | NumberFormatException e) {
			throw new ParseException("Invalid int format", e);
		}
	}
}
