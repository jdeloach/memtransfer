package net.jtmcgee.projects.memtransfer.client;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;

public class Client extends Thread {
	
	private String ip;
	private int port;
	private String fileName;
	
	public Client(String ip, int port, String fileName) {
		this.ip = ip;
		this.port = port;
		this.fileName = fileName;
	}
	
	public void run() {
		try {
			Socket socket = new Socket(ip, port);
			
			BufferedOutputStream bOut = new BufferedOutputStream(socket.getOutputStream());
			
			System.out.println("Opening/loading file ...");
			int length = (int) (new File(fileName)).length();
			byte[] fBuff = new byte[length];
			FileInputStream fIn = new FileInputStream(fileName);
			fIn.read(fBuff, 0, length);
			System.out.println("Loaded " + length + " bytes");
			
			fileName = (new File(fileName)).getName(); // just send the file name, not full path
			byte[] stringBuff = new byte[256];
			System.arraycopy(fileName.getBytes(), 0, stringBuff, 0, fileName.getBytes().length);
			bOut.write(stringBuff, 0, 256);
			stringBuff = new byte[256];
			String fileSize = "" + length + '\n';
			System.arraycopy(fileSize.getBytes(), 0, stringBuff, 0, fileSize.getBytes().length);
			bOut.write(stringBuff, 0, 256);
			
			System.out.println("Sending data ..");
			long startTime = System.nanoTime();
			bOut.write(fBuff, 0, length);
			bOut.flush();
			System.out.println("Done sending data ... time " + ((System.nanoTime() - startTime)/1e6) + " ms");
		} catch (Exception e) {
			System.err.println("Error running client.");
			e.printStackTrace();
		}
		
	}
}
