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
	
	@Test
	public void testGetInt() {
		int actual = bencode.getInt("i123e");
		assertEquals(123, actual);
		
		actual = bencode.getInt("i0e");
		assertEquals(0, actual);
		
		actual = bencode.getInt("i-123e");
		assertEquals(-123, actual);
		
		actual = bencode.getInt("i123e23");
		assertEquals(123, actual);
	}
	
	@Test
	public void testGetIntThrowExceptionWhenPrefixIsNotValid() {
		expectedEx.expect(ParseException.class);
		expectedEx.expectMessage("Int format is i<content>e, the prefix should be 'i'");
		bencode.getInt("I123e");
	}
	
	@Test
	public void testGetIntThrowExceptionWhenSuffixIsNotValid() {
		expectedEx.expect(ParseException.class);
		expectedEx.expectMessage("Int format is i<content>e, the suffix should be 'e'");
		bencode.getInt("i123aa");
	}
	
	@Test
	public void testGetIntThrowExceptionWhenContentIsNotZeroButStartWithZero() {
		expectedEx.expect(ParseException.class);
		expectedEx.expectMessage("Int format is i<content>e, the content should not start with 0 unless is only 0");
		bencode.getInt("i0123e");
	}
	
	@Test
	public void testGetIntThrowExceptionWhenContentIsNegativeZero() {
		expectedEx.expect(ParseException.class);
		expectedEx.expectMessage("Int format is i<content>e, the content should not -0");
		bencode.getInt("i-0e");
	}
	
	@Test
	public void testGetIntThrowExceptionWhenContentIsNotDecimalFormat() {
		expectedEx.expect(ParseException.class);
		expectedEx.expectMessage("Int format is i<content>e, the content is not a decimal format");
		bencode.getInt("i123AAe");
	}
	
	@Test
	public void testGetIntThrowExceptionWhenContentIsBlank() {
		expectedEx.expect(ParseException.class);
		expectedEx.expectMessage("Int format is i<content>e, the content is not a decimal format");
		bencode.getInt("ie");
	}

}
