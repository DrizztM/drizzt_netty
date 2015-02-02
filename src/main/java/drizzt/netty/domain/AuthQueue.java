package drizzt.netty.domain;

import java.util.Queue;

public final class AuthQueue {

	private Queue<ClientRequest> clientQueue;
	private boolean running = false;

	public AuthQueue(Queue<ClientRequest> clientQueue) {
		this.clientQueue = clientQueue;
	}

	public Queue<ClientRequest> getClientQueue() {
		return clientQueue;
	}

	/**
	 * 名称：clear <br/>
	 * 描述：清除消息队列 <br/>
	*/
	public void clear() {
		clientQueue.clear();
		clientQueue = null;
	}
	
	/**
	 * 名称：clear <br/>
	 * 描述：清空消息队列 <br/>
	*/
	public void empty() {
		clientQueue.clear();
	}

	/**
	 * 名称：size <br/>
	 * 描述：获取消息队列长度 <br/>
	 * @return
	*/
	public int size() {
		return clientQueue != null ? clientQueue.size() : 0;
	}

	/**
	 * 名称：add <br/>
	 * 描述：向消息队列中添加请求消息 <br/>
	 * @param msg
	 * @return
	*/
	public boolean add(ClientRequest clientRequest) {
		return this.clientQueue.add(clientRequest);
	}

	/**
	 * 名称：setRunning <br/>
	 * 描述：设置消息队列运行状态 <br/>
	 * @param running
	*/
	public void setRunning(boolean running) {
		this.running = running;
	}

	/**
	 * 名称：isRunning <br/>
	 * 描述：消息队列是否正在被轮询 <br/>
	 * @return
	*/
	public boolean isRunning() {
		return running;
	}

}