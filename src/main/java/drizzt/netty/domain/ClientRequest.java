package drizzt.netty.domain;

import io.netty.channel.Channel;
import drizzt.netty.protobuf.DrizztProtocol.Msg;

public class ClientRequest {

	private Channel channel;

	private Msg msg;

	private long currentTime;

	public ClientRequest(Channel channel, Msg msg, long currentTime) {
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

	public Msg getMsg() {
		return msg;
	}

	public void setMsg(Msg msg) {
		this.msg = msg;
	}

	public long getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(long currentTime) {
		this.currentTime = currentTime;
	}

}
