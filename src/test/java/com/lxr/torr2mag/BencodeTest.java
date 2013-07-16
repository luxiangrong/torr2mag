package com.lxr.torr2mag;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.lxr.torr2mag.becode.exception.ParseException;

public class BencodeTest {

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	private Bencode bencode;

	@Before
	public void init() {
		bencode = new Bencode();
	}

	@Test
	public void testGetStringSuccess() {
		String actual = bencode.getString("4:info");
		assertEquals("info", actual);

		actual = bencode.getString("6:stringbb");
		assertEquals("string", actual);
	}

	@Test
	public void testGetStringThrowExceptionWhenNoColon() {
		expectedEx.expect(ParseException.class);
		expectedEx.expectMessage("String format is <length>:<content>, there is no ':' appeared");
		bencode.getString("string");
	}

	@Test
	public void testGetStringThrowExceptionWhenLengthIsNotADecimalInteger() {
		expectedEx.expect(ParseException.class);
		expectedEx.expectMessage("String format is <length>:<content>, length should a decimal integer and greate than 0");
		bencode.getString("0x12:string");
	}

	@Test
	public void testGetStringThrowExceptionWhenLengthLessThanZero() {
		expectedEx.expect(ParseException.class);
		expectedEx.expectMessage("String format is <length>:<content>, length should a decimal integer and greate than 0");
		bencode.getString("-1:string");
	}

	@Test
	public void testGetStringThrowExceptionWhenLengthEqualZero() {
		expectedEx.expect(ParseException.class);
		expectedEx.expectMessage("String format is <length>:<content>, length should a decimal integer and greate than 0");
		bencode.getString("0:string");
	}

	@Test
	public void testGetStringThrowExceptionWhenLengthGrateThanContentStringLength() {
		expectedEx.expect(ParseException.class);
		expectedEx.expectMessage("String format is <length>:<content>, length should less or equal to content length");
		bencode.getString("7:string");
	}

}
