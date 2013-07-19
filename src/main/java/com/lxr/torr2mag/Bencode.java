package com.lxr.torr2mag;

import java.io.InputStream;

import com.lxr.torr2mag.becode.exception.ParseException;

public class Bencode {

	public String parseString(String encodedString) {

		int tokenIndex = encodedString.indexOf(":");

		if (tokenIndex == -1) {
			throw new ParseException("String format is <length>:<content>, there is no ':' appeared");
		}

		try {
			int length = Integer.valueOf(encodedString.substring(0, tokenIndex), 10);

			if (length <= 0) {
				throw new ParseException("String format is <length>:<content>, length should a decimal integer and greate than 0");
			}

			int contentLength = encodedString.length() - tokenIndex - 1;

			if (length > contentLength) {
				throw new ParseException("String format is <length>:<content>, length should less or equal to content length");
			}

			return encodedString.substring(tokenIndex + 1, tokenIndex + length + 1);
		} catch (NumberFormatException e) {
			throw new ParseException("String format is <length>:<content>, length should a decimal integer and greate than 0", e);
		}

	}

	public String parseString(InputStream is) {
		
		return "";
	}

	public int parseInt(String string) {

		if (!string.startsWith("i")) {
			throw new ParseException("Int format is i<content>e, the prefix should be 'i'");
		}

		StringBuffer buffer = new StringBuffer();
		for (int i = 1; i < string.length(); i++) {
			char c = string.charAt(i);
			if (c == 'e') {
				String integerAsString = buffer.toString();

				if (integerAsString.startsWith("-0")) {
					throw new ParseException("Int format is i<content>e, the content should not start with -0");
				}
				if (integerAsString.startsWith("0") && integerAsString.length() > 1) {
					throw new ParseException("Int format is i<content>e, the content should not start with 0 unless is only 0");
				}

				try {
					return Integer.valueOf(integerAsString, 10);
				} catch (NumberFormatException e) {
					throw new ParseException("Int format is i<content>e, the content is not a decimal format", e);
				}

			} else {
				buffer.append(c);
			}
		}

		throw new ParseException("Int format is i<content>e, the suffix should be 'e'");
	}

}
