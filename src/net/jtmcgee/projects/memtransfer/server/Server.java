package net.jtmcgee.projects.memtransfer.server;

import java.net.ServerSocket;
import java.util.ArrayList;

public class Server {

	private int port;
	private ServerSocket serverSocket;
	private ArrayList<WorkerThread> activeWorkers;
	
	public Server(int port) {
		this.port = port;
		activeWorkers = new ArrayList<WorkerThread>();
	}
	
	public void listen() {
		try {
			serverSocket = new ServerSocket(port);
			
			while(true) {			
				WorkerThread thread = new WorkerThread(callback, serverSocket.accept());
				thread.start();
				activeWorkers.add(thread);
			}
		} catch (Exception e) {
			System.err.println("Error while listening for incoming connections to the server.");
			e.printStackTrace(System.err);
			return;
		}
	}
	
	private OnTransferCompletedCallback callback = new OnTransferCompletedCallback() {
		@Override
		public void onTransferCompleted(WorkerThread thread) {
			System.out.println("Finished transfer.");
			activeWorkers.remove(thread);
		}	
	};
	
}
