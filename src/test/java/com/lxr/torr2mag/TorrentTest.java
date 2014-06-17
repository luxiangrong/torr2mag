package com.lxr.torr2mag;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TorrentTest {

	private Torrent torrent;

	@Before
	public void init() throws FileNotFoundException {
		torrent = new Torrent(new FileInputStream(new File("src/test/resources/2.torrent")));
	}

	@Test
	public void testGetAnnounce() throws FileNotFoundException {
		assertEquals("http://tracker.thepiratebay.org/announce", torrent.getAnnounce());
	}

	@Test
	public void testSetAnnounce() throws FileNotFoundException {
		torrent.setAnnounce("xxx");
		assertEquals("xxx", torrent.getAnnounce());
		System.out.println(torrent.export());
	}
	
	@Test
	public void testGetAnnounceList() {
		List<List<String>> actural = torrent.getAnnounceList();
		assertEquals(10, actural.size());
		for (List<String> list : actural) {
			for (String string : list) {
				System.out.println(string);
			}
		}
	}

}
