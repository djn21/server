package com.rtrk.server.adapter;

/**
 * 
 * @author djekanovic
 *
 *         Encode bytes to PtorocolBuffer message
 */
public interface ProtobufAdapter {

	void encode(byte[] bytes);

	byte[] decode();

}
