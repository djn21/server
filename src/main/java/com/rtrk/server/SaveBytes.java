package com.rtrk.server;

import java.io.File;
import java.io.FileOutputStream;

/**
 * 
 * @author djekanovic
 *
 *         Take bytes from queue and write it to file
 */

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
			while (!Server.end) {
				fout = new FileOutputStream(new File(filePath), true);
				byte[] bytes = Server.queue.take();
				if (bytes != null) {
					fout.write(header.getBytes());
					fout.write(bytes);
					fout.write(footer.getBytes());
					fout.close();
				} else {
					// Sleep 0.5 seconds
					sleep(500);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
