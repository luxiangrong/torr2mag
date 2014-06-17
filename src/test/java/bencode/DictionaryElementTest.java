package bencode;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.lxr.torr2mag.bencode.DictionaryElement;
import com.lxr.torr2mag.bencode.StringElement;

public class DictionaryElementTest {

	@Test
	public void testEquals() {
		DictionaryElement expected = new DictionaryElement();
		expected.addElement(new StringElement("bar"), new StringElement("spam"));
		expected.addElement(new StringElement("foo"), new StringElement("42"));

		DictionaryElement actual = new DictionaryElement();
		actual.addElement(new StringElement("bar"), new StringElement("spam"));
		actual.addElement(new StringElement("foo"), new StringElement("42"));

		assertEquals(expected.toEncodedString(), actual.toEncodedString());
	}
}
