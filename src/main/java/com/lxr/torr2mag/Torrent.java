package com.lxr.torr2mag;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.lxr.torr2mag.bencode.Bencode;
import com.lxr.torr2mag.bencode.DictionaryElement;
import com.lxr.torr2mag.bencode.IntElement;
import com.lxr.torr2mag.bencode.ListElement;
import com.lxr.torr2mag.bencode.StringElement;
import com.lxr.torr2mag.utils.StringUtils;

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
		ListElement<ListElement<StringElement>> listElement = (ListElement<ListElement<StringElement>>) delgate.get("announce-list");
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
	
	public FileInfo getFileInfo() {
		DictionaryElement info = (DictionaryElement) delgate.get("info");
		if(info.get("files") != null) {
			MultiFileInfo fileInfo = new MultiFileInfo(info);
		}
		return null;
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

	class FileInfo {
		private String name;
		private String nameUtf8;
		private int pieceLength;
		private String pieces;
		private String publisher;
		private String publisherUrl;
		private String publisherUrlUtf8;
		private String publisherUtf8;

		public FileInfo(DictionaryElement info) {
			this.name = ((StringElement) info.get("name")).getValue();
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getNameUtf8() {
			return nameUtf8;
		}

		public void setNameUtf8(String nameUtf8) {
			this.nameUtf8 = nameUtf8;
		}

		public int getPieceLength() {
			return pieceLength;
		}

		public void setPieceLength(int pieceLength) {
			this.pieceLength = pieceLength;
		}

		public String getPieces() {
			return pieces;
		}

		public void setPieces(String pieces) {
			this.pieces = pieces;
		}

		public String getPublisher() {
			return publisher;
		}

		public void setPublisher(String publisher) {
			this.publisher = publisher;
		}

		public String getPublisherUrl() {
			return publisherUrl;
		}

		public void setPublisherUrl(String publisherUrl) {
			this.publisherUrl = publisherUrl;
		}

		public String getPublisherUrlUtf8() {
			return publisherUrlUtf8;
		}

		public void setPublisherUrlUtf8(String publisherUrlUtf8) {
			this.publisherUrlUtf8 = publisherUrlUtf8;
		}

		public String getPublisherUtf8() {
			return publisherUtf8;
		}

		public void setPublisherUtf8(String publisherUtf8) {
			this.publisherUtf8 = publisherUtf8;
		}

		public DictionaryElement format() {
			DictionaryElement result = new DictionaryElement();

			if (StringUtils.isNotEmpty(name)) {
				result.addElement(new StringElement("name"), new StringElement(name));
			}

			if (StringUtils.isNotEmpty(nameUtf8)) {
				result.addElement(new StringElement("name.utf-8"), new StringElement(nameUtf8));
			}

			result.addElement(new StringElement("piece length"), new IntElement(pieceLength));

			result.addElement(new StringElement("pieces"), new StringElement(pieces));

			if (StringUtils.isNotEmpty(publisher)) {
				result.addElement(new StringElement("publisher"), new StringElement(publisher));
			}

			if (StringUtils.isNotEmpty(publisherUrl)) {
				result.addElement(new StringElement("publisher-url"), new StringElement(publisherUrl));
			}

			if (StringUtils.isNotEmpty(publisherUrlUtf8)) {
				result.addElement(new StringElement("publisher-url.utf-8"), new StringElement(publisherUrlUtf8));
			}

			if (StringUtils.isNotEmpty(publisherUtf8)) {
				result.addElement(new StringElement("publisher.utf-8"), new StringElement(publisherUtf8));
			}

			return result;
		}

	}

	class SingleFileInfo extends FileInfo {
		public SingleFileInfo(DictionaryElement info) {
			super(info);
			// TODO Auto-generated constructor stub
		}

		private int length;

		public int getLength() {
			return length;
		}

		public void setLength(int length) {
			this.length = length;
		}

		public DictionaryElement format() {
			DictionaryElement result = super.format();
			result.addElement(new StringElement("length"), new IntElement(length));
			return result;
		}

	}

	class MultiFileInfo extends FileInfo {
		private List<MultiFileItem> files = new ArrayList<MultiFileItem>();

		public MultiFileInfo(DictionaryElement info) {
			super(info);
		}

		public List<MultiFileItem> getFiles() {
			return files;
		}

		public void setFiles(List<MultiFileItem> files) {
			this.files = files;
		}

		public DictionaryElement format() {
			DictionaryElement result = super.format();

			ListElement<DictionaryElement> filesElement = new ListElement<DictionaryElement>();
			for (MultiFileItem item : files) {
				filesElement.addElement(item.format());
			}

			result.addElement(new StringElement("files"), filesElement);

			return result;
		}
	}

	class MultiFileItem {
		private int length;
		private String[] path;
		private String[] utf8Path;

		public DictionaryElement format() {
			DictionaryElement result = new DictionaryElement();
			result.addElement(new StringElement("length"), new IntElement(length));
			ListElement<StringElement> paths = new ListElement<StringElement>();
			for (String string : path) {
				paths.addElement(new StringElement(string));
			}
			result.addElement(new StringElement("path"), paths);

			if (utf8Path != null && utf8Path.length > 0) {
				paths = new ListElement<StringElement>();
				for (String string : path) {
					paths.addElement(new StringElement(string));
				}
				result.addElement(new StringElement("path.utf-8"), paths);
			}

			return result;
		}

		public int getLength() {
			return length;
		}

		public void setLength(int length) {
			this.length = length;
		}

		public String[] getPath() {
			return path;
		}

		public void setPath(String[] path) {
			this.path = path;
		}

		public String[] getUtf8Path() {
			return utf8Path;
		}

		public void setUtf8Path(String[] utf8Path) {
			this.utf8Path = utf8Path;
		}

	}

}
