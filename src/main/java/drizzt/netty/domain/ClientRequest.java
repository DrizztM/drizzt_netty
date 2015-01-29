package drizzt.netty.domain;

import io.netty.channel.Channel;

public class ClientRequest {

	private Channel channel;

	private String msg;

	public ClientRequest(Channel channel, String msg) {
		this.channel = channel;
		this.msg = msg;
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

}
