package com.lxr.torr2mag;

import com.lxr.torr2mag.becode.exception.ParseException;

public class Bencode {

	public String getString(String encodedString) {

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

}
