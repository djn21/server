package com.rtrk.server.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ClientSocket extends Thread {

	private static final String address = "127.0.0.1";
	private static final int TCP_PORT = 9090;
	private static final int bufferSize = 65;
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
						BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						PrintWriter out = new PrintWriter(
								new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
						// Send file length
						out.println(file.length());
						// Send bytes
						InputStream inBytes = new FileInputStream(file);
						OutputStream outBytes = socket.getOutputStream();
						byte[] buffer = new byte[bufferSize];
						int count;
						while ((count = inBytes.read(buffer)) > 0) {
							outBytes.write(buffer, 0, count);
						}
						// Copy file to sent folder
						inBytes.close();
						inBytes = new FileInputStream(file);
						Files.copy(inBytes, new File((sentFilesPath + File.separator + file.getName())).toPath(),
								StandardCopyOption.REPLACE_EXISTING);
						inBytes.close();
						file.delete();
						// Read response status
						String status = in.readLine();
						System.out.println(status);
						outBytes.close();
						out.close();
						in.close();
						socket.close();
					}
				}
				System.out.println("NEMA");
				sleep(500);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
