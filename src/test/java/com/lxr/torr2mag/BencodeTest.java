package com.lxr.torr2mag;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.lxr.torr2mag.bencode.Bencode;
import com.lxr.torr2mag.bencode.exception.ParseException;

public class BencodeTest {

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	private Bencode bencode;

	@Before
	public void init() {
		bencode = new Bencode();
	}

	@Test
	public void testparseStringSuccess() throws FileNotFoundException {
		String actual = bencode.parseString("4:info");
		assertEquals("info", actual);

		actual = bencode.parseString("6:stringbb");
		assertEquals("string", actual);

		actual = bencode.parseString(new ByteArrayInputStream("6:string"
				.getBytes()));
		assertEquals("string", actual);
	}

	@Test
	public void testparseStringThrowExceptionWhenNoColon() {
		expectedEx.expect(ParseException.class);
		expectedEx.expectMessage("Invalid string format");
		bencode.parseString("string");
	}

	@Test
	public void testparseStringThrowExceptionWhenLengthIsNotADecimalInteger() {
		expectedEx.expect(ParseException.class);
		expectedEx.expectMessage("Invalid string format");
		bencode.parseString("0x12:string");
	}

	@Test
	public void testparseStringThrowExceptionWhenLengthLessThanZero() {
		expectedEx.expect(ParseException.class);
		expectedEx.expectMessage("Invalid string format");
		bencode.parseString("-1:string");
	}

	@Test
	public void testparseStringThrowExceptionWhenLengthEqualZero() {
		expectedEx.expect(ParseException.class);
		expectedEx.expectMessage("Invalid string format");
		bencode.parseString("0:string");
	}

	@Test
	public void testparseStringThrowExceptionWhenLengthGrateThanContentStringLength() {
		expectedEx.expect(ParseException.class);
		expectedEx.expectMessage("Invalid string format");
		bencode.parseString("7:string");
	}

	@Test
	public void testParseInt() {
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
	public void testParseIntThrowExceptionWhenPrefixIsNotValid() {
		expectedEx.expect(ParseException.class);
		expectedEx.expectMessage("Invalid int format");
		bencode.parseInt("I123e");
	}

	@Test
	public void testParseIntThrowExceptionWhenSuffixIsNotValid() {
		expectedEx.expect(ParseException.class);
		expectedEx.expectMessage("Invalid int format");
		bencode.parseInt("i123aa");
	}

	@Test
	public void testParseIntThrowExceptionWhenContentIsNotZeroButStartWithZero() {
		expectedEx.expect(ParseException.class);
		expectedEx.expectMessage("Invalid int format");
		bencode.parseInt("i0123e");
	}

	@Test
	public void testParseIntThrowExceptionWhenContentIsNegativeZero() {
		expectedEx.expect(ParseException.class);
		expectedEx.expectMessage("Invalid int format");
		bencode.parseInt("i-0e");
	}

	@Test
	public void testParseIntThrowExceptionWhenContentIsStartWithNegativeZero() {
		expectedEx.expect(ParseException.class);
		expectedEx.expectMessage("Invalid int format");
		bencode.parseInt("i-0123e");
	}

	@Test
	public void testParseIntThrowExceptionWhenContentIsNotDecimalFormat() {
		expectedEx.expect(ParseException.class);
		expectedEx.expectMessage("Invalid int format");
		bencode.parseInt("i123AAe");
	}

	@Test
	public void testParseIntThrowExceptionWhenContentIsBlank() {
		expectedEx.expect(ParseException.class);
		expectedEx.expectMessage("Invalid int format");
		bencode.parseInt("ie");
	}

	

}
