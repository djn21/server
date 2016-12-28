package com.rtrk.server;

import java.io.File;
import java.io.FileOutputStream;

public class SaveBytes extends Thread {
	
	private static final String filePath=".\\requests";
	private static final String header = "MESSAGE[";
	private static final String footer = "]ENDOFMESSAGE\n";

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
