package com.rtrk.server;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rtrk.server.socket.ClientSocket;

//import com.rtrk.server.socket.ClientSocket;

/**
 * Servlet implementation class Server
 */
public class Server extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public static final int bufferSize = 1024; // 1 kB
	public static boolean end = false;
	public static BlockingQueue<byte[]> queue = new SynchronousQueue<byte[]>();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Server() {
		super();
	}

	@Override
	public void init() throws ServletException {
		super.init();
		
		// Get config
		String filePathHttp = getInitParameter("httppath");
		
		// Start thread for saving bytes
		new SaveBytes(filePathHttp).start();
		System.out.println("SaveBytes started");
		
		// Start socket client
		String address=getInitParameter("SocketAddress");
		int port=Integer.parseInt(getInitParameter("SocketPort"));
		String sendingFilesPath=getInitParameter("socketsendingpath");
		String sentFilesPath=getInitParameter("socketsentpath");
		new ClientSocket(address, port, sendingFilesPath, sentFilesPath).start();
	}

	@Override
	public void destroy() {
		end = true;
		super.destroy();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 * 
	 *      Receive bytes and put them to queue
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ServletInputStream in = request.getInputStream();
		byte[] buffer = new byte[bufferSize];
		int size;
		while ((size = in.read(buffer)) != -1) {
			byte[] bytes = new byte[size];
			System.arraycopy(buffer, 0, bytes, 0, size);
			try {
				queue.put(bytes);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
