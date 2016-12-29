package com.rtrk.server.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ClientSocket extends Thread {

	private static final String address = "127.0.0.1";
	private static final int TCP_PORT = 9090;
	private static final int bufferSize = 1024;
	private static final String filesPath = "sending";
	private static final String sentFilesPath = "sent";

	public void run() {
		try {
			while (!com.rtrk.server.Server.end) {
				// Read file if exists
				File folder = new File(filesPath);
				File[] files = folder.listFiles();
				if (files.length != 0) {
					for (File file : files) {
						InetAddress addr = InetAddress.getByName(address);
						Socket socket = new Socket(addr, TCP_PORT);
						// Send bytes
						InputStream inBytes = new FileInputStream(file);
						DataInputStream in = new DataInputStream(socket.getInputStream());
						DataOutputStream out = new DataOutputStream(socket.getOutputStream());
						// Send file length
						out.writeInt((int) file.length());
						out.flush();
						byte[] buffer = new byte[bufferSize];
						int count;
						while ((count = inBytes.read(buffer)) > 0) {
							out.write(buffer, 0, count);
							out.flush();
						}
						// Copy file to sent folder
						inBytes.close();
						inBytes = new FileInputStream(file);
						Files.copy(inBytes, new File((sentFilesPath + File.separator + file.getName())).toPath(),
								StandardCopyOption.REPLACE_EXISTING);
						inBytes.close();
						file.delete();
						// Read response status
						boolean sent = in.readBoolean();
						if (sent) {
							System.out.println("SOCKET: File sent.");
						} else {
							System.out.println("SOCKET: File not sent.");
						}
						out.close();
						in.close();
						socket.close();
					}
				}
				sleep(500);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
