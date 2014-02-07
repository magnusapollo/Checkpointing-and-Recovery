package protocol;

import model.NodeImpl;

public class CheckpointingTimer implements Runnable {

	private long timeout;
	
	private NodeImpl node;
	
	public CheckpointingTimer(long timeout, NodeImpl node) {
		this.timeout = timeout;
		this.node = node;
	}

	@Override
	public void run() {
		boolean end = false;
		synchronized (node) {
			while(!node.connectionEstablished()) {
				try {
					node.wait();
				} catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		while(!end) {
			node.initiateCheckPoint();
			synchronized (node) {
				while(node.failed()) {
					try {
						node.wait();
					} catch(InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			try {
				Thread.sleep(timeout);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
