package com.rtrk.server.adapter;

public interface ProtobufAdapter {

	void encode(byte[] bytes);
	
	byte[] decode();
	
}
