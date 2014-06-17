package bencode;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.lxr.torr2mag.bencode.Bencode;
import com.lxr.torr2mag.bencode.DictionaryElement;
import com.lxr.torr2mag.bencode.Element;
import com.lxr.torr2mag.bencode.IntElement;
import com.lxr.torr2mag.bencode.ListElement;
import com.lxr.torr2mag.bencode.StringElement;
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
		Element actual = bencode.parseString("4:info");
		assertEquals(new StringElement("info"), actual);

		actual = bencode.parseString("6:stringbb");
		assertEquals(new StringElement("string"), actual);

		actual = bencode.parseString(new ByteArrayInputStream("6:string"
				.getBytes()));
		assertEquals(new StringElement("string"), actual);
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
		Element actual = bencode.parseInt("i123e");
		assertEquals(new IntElement(123), actual);

		actual = bencode.parseInt("i0e");
		assertEquals(new IntElement(0), actual);

		actual = bencode.parseInt("i-123e");
		assertEquals(new IntElement(-123), actual);

		actual = bencode.parseInt("i123e23");
		assertEquals(new IntElement(123), actual);
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

	@Test
	public void testParseListSuccess() {
		ListElement actual = bencode.parseList("l6:stringi123ee");

		StringElement stringElement = new StringElement("string");
		IntElement intElement = new IntElement(123);
		ListElement expected = new ListElement();
		expected.addElement(stringElement);
		expected.addElement(intElement);

		assertEquals(expected, actual);
	}

	@Test
	public void testParseListThrowExceptionWhenPrefixIsNotValid() {
		expectedEx.expect(ParseException.class);
		expectedEx.expectMessage("Invalid list format");
		bencode.parseList("3l6:stringi123ee");
	}

	@Test
	public void testParseListThrowExceptionWhenSuffixIsNotValid() {
		expectedEx.expect(ParseException.class);
		expectedEx.expectMessage("Invalid list format");
		bencode.parseList("l6:stringi123e");
	}

	@Test
	public void testParseListThrowExceptionWhenContainInvalidElement() {
		expectedEx.expect(ParseException.class);
		expectedEx.expectMessage("Invalid element format");
		bencode.parseList("l6:stringg123e");
	}

	@Test
	public void testParseDictionarySuccess() {
		DictionaryElement actual = bencode
				.parseDictionary("d3:bar4:spam3:fooi42ee");

		DictionaryElement expected = new DictionaryElement();
		expected.addElement(new StringElement("bar"), new StringElement("spam"));
		expected.addElement(new StringElement("foo"), new IntElement(42));

		assertEquals(expected, actual);
	}

	@Test
	public void testParse() throws FileNotFoundException {
		Element element = bencode.parse(new FileInputStream(new File(
				"src/test/resources/2.torrent")));

		System.out.println(element.toEncodedString());

	}

}
