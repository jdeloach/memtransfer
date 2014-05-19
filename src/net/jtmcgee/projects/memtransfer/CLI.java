package net.jtmcgee.projects.memtransfer;

import net.jtmcgee.projects.memtransfer.client.Client;
import net.jtmcgee.projects.memtransfer.server.Server;

public class CLI {

	public static void main(String[] args) {	
		try {
			if(args.length == 0) {
				System.err.println("Arguments error. For server mode, run \"cli -s [PORT]\" for client mode, run \"cli -c [IP.HE.RE.00] [PORT] [FILENAME]\".");
				return;
			} else { // should be server
				if(args[0].equalsIgnoreCase("-s")) {
					setupServer(Integer.parseInt(args[1]));
				} else if(args[0].equalsIgnoreCase("-c")) {				
					setupClient(args[1], Integer.parseInt(args[2]), args[3]);
				} else {
					System.err.println("Arguments not recognized. Try again.");
					return;
				}
			}
		} catch (Exception e) {
			System.err.println("Error running CLI.");
			e.printStackTrace(System.err);
		}
	}
	
	private static void setupServer(int port) {
		(new Server(port)).listen();
	}
	
	private static void setupClient(String ip, int port, String file) {
		(new Client(ip, port, file)).run();
	}

}
