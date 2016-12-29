package com.rtrk.server;

import java.io.File;
import java.io.FileOutputStream;

public class SaveBytes extends Thread {
	
	private String filePath;
	private String header;
	private String footer;
	
	public SaveBytes(String filePath, String header, String footer) {
		super();
		this.filePath = filePath;
		this.header = header;
		this.footer = footer;
	}

	public void run() {
		FileOutputStream fout;
		try {
			fout = new FileOutputStream(new File(filePath), true);
			while (!Server.end) {

				byte[] bytes = Server.queue.take();
				if (bytes != null) {
					fout.write(header.getBytes());
					fout.write(bytes);
					fout.write(footer.getBytes());
				}
			}
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
