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
		String actual = bencode.parseString("4:info");
		assertEquals("info", actual);

		actual = bencode.parseString("6:stringbb");
		assertEquals("string", actual);
	}

	@Test
	public void testGetStringThrowExceptionWhenNoColon() {
		expectedEx.expect(ParseException.class);
		expectedEx.expectMessage("String format is <length>:<content>, there is no ':' appeared");
		bencode.parseString("string");
	}

	@Test
	public void testGetStringThrowExceptionWhenLengthIsNotADecimalInteger() {
		expectedEx.expect(ParseException.class);
		expectedEx.expectMessage("String format is <length>:<content>, length should a decimal integer and greate than 0");
		bencode.parseString("0x12:string");
	}

	@Test
	public void testGetStringThrowExceptionWhenLengthLessThanZero() {
		expectedEx.expect(ParseException.class);
		expectedEx.expectMessage("String format is <length>:<content>, length should a decimal integer and greate than 0");
		bencode.parseString("-1:string");
	}

	@Test
	public void testGetStringThrowExceptionWhenLengthEqualZero() {
		expectedEx.expect(ParseException.class);
		expectedEx.expectMessage("String format is <length>:<content>, length should a decimal integer and greate than 0");
		bencode.parseString("0:string");
	}

	@Test
	public void testGetStringThrowExceptionWhenLengthGrateThanContentStringLength() {
		expectedEx.expect(ParseException.class);
		expectedEx.expectMessage("String format is <length>:<content>, length should less or equal to content length");
		bencode.parseString("7:string");
	}
	
	@Test
	public void testGetInt() {
		int actual = bencode.parseInt("i123e");
		assertEquals(123, actual);
		
		actual = bencode.parseInt("i0e");
		assertEquals(0, actual);
		
		actual = bencode.parseInt("i-123e");
		assertEquals(-123, actual);
		
		actual = bencode.parseInt("i123e23");
		assertEquals(123, actual);
	}
	
	@Test
	public void testGetIntThrowExceptionWhenPrefixIsNotValid() {
		expectedEx.expect(ParseException.class);
		expectedEx.expectMessage("Int format is i<content>e, the prefix should be 'i'");
		bencode.parseInt("I123e");
	}
	
	@Test
	public void testGetIntThrowExceptionWhenSuffixIsNotValid() {
		expectedEx.expect(ParseException.class);
		expectedEx.expectMessage("Int format is i<content>e, the suffix should be 'e'");
		bencode.parseInt("i123aa");
	}
	
	@Test
	public void testGetIntThrowExceptionWhenContentIsNotZeroButStartWithZero() {
		expectedEx.expect(ParseException.class);
		expectedEx.expectMessage("Int format is i<content>e, the content should not start with 0 unless is only 0");
		bencode.parseInt("i0123e");
	}
	
	@Test
	public void testGetIntThrowExceptionWhenContentIsNegativeZero() {
		expectedEx.expect(ParseException.class);
		expectedEx.expectMessage("Int format is i<content>e, the content should not start with -0");
		bencode.parseInt("i-0e");
	}
	
	@Test
	public void testGetIntThrowExceptionWhenContentIsStartWithNegativeZero() {
		expectedEx.expect(ParseException.class);
		expectedEx.expectMessage("Int format is i<content>e, the content should not start with -0");
		bencode.parseInt("i-0123e");
	}
	
	@Test
	public void testGetIntThrowExceptionWhenContentIsNotDecimalFormat() {
		expectedEx.expect(ParseException.class);
		expectedEx.expectMessage("Int format is i<content>e, the content is not a decimal format");
		bencode.parseInt("i123AAe");
	}
	
	@Test
	public void testGetIntThrowExceptionWhenContentIsBlank() {
		expectedEx.expect(ParseException.class);
		expectedEx.expectMessage("Int format is i<content>e, the content is not a decimal format");
		bencode.parseInt("ie");
	}

}
