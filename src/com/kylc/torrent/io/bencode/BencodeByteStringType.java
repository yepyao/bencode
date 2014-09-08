package com.kylc.torrent.io.bencode;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class BencodeByteStringType extends BencodeType {
	private int length;
	private byte[] value;
	
	public BencodeByteStringType(byte[] value) {
		this(value.length, value);
	}
	
	public BencodeByteStringType(int length, byte[] value) {
		this.length = length;
		this.value = value;
	}
	
	public void setStringType(int length, byte[] value) {
		this.length = length;
		this.value = value;
	}
	
	public String getString(){
		try {
			return new String(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		return "";
	}
	
	public static BencodeType parse(BufferedInputStream input) throws IOException {
		int length = 0;
		
		char read;
		while((read = (char) input.read()) != ':') {
			length = (length * 10) + Character.getNumericValue(read);
		}
		byte[] data = new byte[length];
		input.read(data);
		
		return new BencodeByteStringType(length, data);
	}

	@Override
	public void dump(OutputStream output) throws IOException {
		output.write(Integer.toString(length).getBytes());
		output.write(':');
		output.write(value);
	}
	
	@Override
	public String getNativeValue() {
		return new String(value, Charset.forName("UTF-8"));
	}
	
	@Override
	public String toString() {
		return "[BencodeByteStringType length=" + length + " value=" + getNativeValue() + "]";
	}
}
