package net.jtmcgee.projects.memtransfer.server;

import java.net.Socket;

public interface OnTransferCompletedCallback {
	public void onTransferCompleted(WorkerThread thread);
}
