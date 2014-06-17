package com.lxr.torr2mag.bencode;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.lxr.torr2mag.bencode.exception.ParseException;

public class Bencode {

	public Element parseString(String encodedString) {
		return parseString(new ByteArrayInputStream(encodedString.getBytes()));
	}

	public Element parseString(InputStream is) {
		if (!is.markSupported()) {
			throw new ParseException("Unsupported inputStream");
		}
		try {
			StringBuffer buffer = new StringBuffer();
			is.reset();
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

			return new StringElement(new String(byteArray));

		} catch (IOException | NumberFormatException e) {
			throw new ParseException("Invalid string format", e);
		}
	}

	public Element parseInt(String string) {
		return parseInt(new ByteArrayInputStream(string.getBytes()));
	}

	public Element parseInt(InputStream is) {
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

			return new IntElement(Integer.parseInt(buffer.toString()));
		} catch (IOException | NumberFormatException e) {
			throw new ParseException("Invalid int format", e);
		}
	}

	public ListElement parseList(String string) {
		return parseList(new ByteArrayInputStream(string.getBytes()));
	}

	public ListElement parseList(InputStream is) {
		if (!is.markSupported()) {
			throw new ParseException("Unsupported inputStream");
		}
		try {
			is.reset();
			int c = is.read();
			if (c != 'l') {
				throw new ParseException("Invalid list format");
			}
			ListElement result = new ListElement();
			is.mark(0);
			c = is.read();
			while (c != -1 && c != 'e') {
				switch (c) {
				case 'd':
					result.addElement(parseDictionary(is));
					break;
				case 'i':
					result.addElement(parseInt(is));
					break;
				case 'l':
					result.addElement(parseList(is));
					break;
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				case '9':
					result.addElement(parseString(is));
					break;
				default:
					throw new ParseException("Invalid element format");
				}
				is.mark(0);
				c = is.read();
			}
			if (c != 'e') {
				throw new ParseException("Invalid list format");
			}
			return result;

		} catch (IOException e) {
			throw new ParseException("Invalid list format", e);
		}

	}

	public DictionaryElement parseDictionary(String string) {
		return parseDictionary(new ByteArrayInputStream(string.getBytes()));
	}

	public DictionaryElement parseDictionary(InputStream is) {
		try {
			DictionaryElement result = new DictionaryElement();
			is.reset();
			int c = is.read();
			if (c != 'd') {
				throw new ParseException("Invalid dictionary format");
			}
			is.mark(0);
			c = is.read();
			while (c != -1 && c != 'e') {
				StringElement key = (StringElement) parseString(is);
				is.mark(0);
				Element value = parse(is);
				is.mark(0);
				result.addElement(key, value);
				c = is.read();
			}
			return result;

		} catch (IOException e) {
			throw new ParseException("Parse error", e);
		}
	}

	public Element parse(InputStream is) {
		if (!is.markSupported()) {
			is = new BufferedInputStream(is);
		}
		try {
			is.mark(0);
			Element result = null;
			int c = is.read();
			switch (c) {
			case 'd':
				result = parseDictionary(is);
				break;
			case 'i':
				result = parseInt(is);
				break;
			case 'l':
				result = parseList(is);
				break;
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
				result = parseString(is);
				break;
			default:
				throw new ParseException("Invalid element format");
			}
			is.mark(0);
			return result;
		} catch (IOException e) {
			throw new ParseException("Parse error", e);
		}

	}

	public Element parse(String string) {
		return parse(new ByteArrayInputStream(string.getBytes()));
	}
}
