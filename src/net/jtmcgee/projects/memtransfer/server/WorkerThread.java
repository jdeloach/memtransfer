package net.jtmcgee.projects.memtransfer.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.DecimalFormat;

public class WorkerThread extends Thread {

	private Socket socket;
	private OnTransferCompletedCallback callback;
	
	public WorkerThread(OnTransferCompletedCallback callback, Socket accept) {
		this.callback = callback;
		this.socket = accept;
	}

	@Override
	public void run() {
		try {
			BufferedInputStream bIn = new BufferedInputStream(socket.getInputStream());
			BufferedOutputStream bOut = new BufferedOutputStream(socket.getOutputStream());
			byte[] stringBuff = new byte[256];
			
			bIn.read(stringBuff, 0, 256);
		
			String fileName = new String(stringBuff);
			System.out.println("\n\n Incoming File: " + fileName);
			
			bIn.read(stringBuff, 0, 256);
			String fileSizeStr = new String(stringBuff);
			int fileSize = Integer.parseInt(fileSizeStr.substring(0, fileSizeStr.indexOf('\n')));
//			System.out.println("File Size: " + fileSize);
			
			byte[] fileBuff = new byte[fileSize];
			int bytesRead = 0;
			long startTime = System.nanoTime();
			
			while(bytesRead != fileSize) {
				bytesRead += bIn.read(fileBuff, bytesRead, (fileSize - bytesRead));
			}
			
			double milliSec = ((System.nanoTime() - startTime) / 1e6);
			double mbPerSec = 0.000953674316 * (bytesRead/milliSec);
			DecimalFormat df = new DecimalFormat("##.##");
			System.out.println("Done reading transfering! Took " + df.format(milliSec) + " ms at " + df.format(mbPerSec) + " MB/s. \nNow saving to file.");
			socket.close();
			
			FileOutputStream fOut = new FileOutputStream(fileName);
			fOut.write(fileBuff, 0, bytesRead);
			fOut.flush();
			fOut.close();
			milliSec = ((System.nanoTime() - startTime) / 1e6);
			mbPerSec = 0.000953674316 * (bytesRead/milliSec);
			System.out.println("Done writing to " + fileName + "! Took " + df.format(milliSec) + " ms at " + df.format(mbPerSec) + " MB/s to save to disk.");
		} catch (IOException e) {
			System.err.println("Error while reading in, within worker thread.");
			e.printStackTrace();
		}
	}
}