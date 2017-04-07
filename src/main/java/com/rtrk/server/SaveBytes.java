package com.rtrk.server;

import java.io.File;
import java.io.FileOutputStream;

/**
 * 
 * Take bytes from queue and write them to file
 * 
 * @author djekanovic
 *
 */

public class SaveBytes extends Thread {

	private String filePath;

	public SaveBytes(String filePath) {
		super();
		this.filePath = filePath;
	}

	public void run() {
		FileOutputStream fout;
		try {
			while (!Server.end) {
				byte[] bytes = Server.queue.take();
				if (bytes != null) {
					fout = new FileOutputStream(new File(filePath), true);
					fout.write(bytes);
					fout.close();
				} else {
					// wait 100 ms
					sleep(100);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
