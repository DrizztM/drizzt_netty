package drizzt.netty.domain;

import io.netty.channel.Channel;

public class ClientRequest {

	private Channel channel;

	private String msg;

	private long currentTime;

	public ClientRequest(Channel channel, String msg, long currentTime) {
		this.channel = channel;
		this.msg = msg;
		this.currentTime = currentTime;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public long getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(long currentTime) {
		this.currentTime = currentTime;
	}

}
