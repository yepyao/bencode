package com.kylc.torrent.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.kylc.torrent.io.bencode.BencodeDictionaryType;
import com.kylc.torrent.io.bencode.BencodeInputStream;

public class BitTorrentFile {
	private final String announce;
	private final BencodeDictionaryType info;
	public final BencodeDictionaryType root;
	
	public BitTorrentFile(BencodeDictionaryType root) {
		this.root = root;
		announce = (String) root.lookup("announce").getNativeValue();
		info = (BencodeDictionaryType) root.lookup("info");
	}
	
	public static BitTorrentFile open(String path) throws IOException {
		return BitTorrentFile.open(new File(path));
	}
	
	public static BitTorrentFile open(File file) throws IOException {
		BencodeInputStream in = new BencodeInputStream(new FileInputStream(file));
		BencodeDictionaryType root = (BencodeDictionaryType) in.readNextType();
		
		return new BitTorrentFile(root);
	}
	
	public BencodeDictionaryType getRoot() {
		return root;
	}
	
	public String getAnnounce() {
		return announce;
	}
	
	public BencodeDictionaryType getInfo() {
		return info;
	}
}
