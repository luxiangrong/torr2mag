package com.lxr.torr2mag;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.lxr.torr2mag.bencode.Bencode;
import com.lxr.torr2mag.bencode.DictionaryElement;
import com.lxr.torr2mag.bencode.ListElement;
import com.lxr.torr2mag.bencode.StringElement;

public class Torrent extends Bencode {

	//Tracker的主服务器
	private String announce;

	private List<List<String>> announceList = new ArrayList<List<String>>();

	private DictionaryElement delgate;

	private Bencode bencode;

	public Torrent(InputStream in) {
		this.delgate = (DictionaryElement) parse(in);
	}

	public List<List<String>> getAnnounceList() {
		ListElement<ListElement<StringElement>> listElement =  (ListElement<ListElement<StringElement>>) delgate.get("announce-list");
		List<ListElement<StringElement>> listElement2 = listElement.getValue();
		
		for (ListElement<StringElement> listElement3 : listElement2) {
			List<String> stringList = new ArrayList<String>();
			List<StringElement> stringElement = listElement3.getValue();
			for (StringElement stringElement2 : stringElement) {
				stringList.add(stringElement2.getValue());
			}
			announceList.add(stringList);
		}
		return announceList;
	}
	
	
	/**
	 * 获取Tracker的主服务器地址
	 * 
	 * @return
	 */
	public String getAnnounce() {
		StringElement element = (StringElement) delgate.get("announce");
		announce = element.getValue();
		return announce;
	}

	/**
	 * 设置Tracker的主服务器地址
	 * 
	 * @return
	 */
	public void setAnnounce(String announce) {
		this.announce = announce;
		delgate.put("announce", new StringElement(announce));
	}

	public String export() {
		return delgate.toEncodedString();
	}

}
