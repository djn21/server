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

/**
 * Servlet implementation class Server
 */
public class Server extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	private static final int bufferSize=1024*1024; //1 MB
	
	public static boolean end = false;
	public static BlockingQueue<byte[]> queue = new SynchronousQueue<byte[]>();
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Server() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		// Read socket config
		String socketAddress=getInitParameter("SocketAddress");
		int socketPort=Integer.parseInt(getInitParameter("SocketPort"));
		String filePathSending="C:\\Users\\djekanovic\\Desktop\\sendingsocket";
		String filePathSent="C:\\Users\\djekanovic\\Desktop\\sentsocket";
		new ClientSocket(socketAddress, socketPort, filePathSending, filePathSent).start();
		System.out.println("ClientSocket started");
		// Read http config
		String filePathHttp="C:\\Users\\djekanovic\\Desktop\\requestshttp";
		String header="MESSAGE[";
		String footer="]ENDOFMESSAGE\n";
		new SaveBytes(filePathHttp, header, footer).start();	
		System.out.println("SaveBytes started");
	}

	


	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		end=true;
		super.destroy();
	}



	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		ServletInputStream in = request.getInputStream();
		byte[] buffer = new byte[bufferSize];
		int size = in.read(buffer);
		byte[] bytes = new byte[size];
		System.arraycopy(buffer, 0, bytes, 0, size);
		try{
			queue.put(bytes);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
