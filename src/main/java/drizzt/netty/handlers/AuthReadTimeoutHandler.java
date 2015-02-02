package drizzt.netty.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.timeout.ReadTimeoutHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import drizzt.netty.dispatcher.AuthDispatcher;

@Component
@Sharable
public class AuthReadTimeoutHandler extends ReadTimeoutHandler {

	@Autowired
	private AuthDispatcher authDispatcher;

	public AuthReadTimeoutHandler() {
		super(30);
	}

	protected void readTimedOut(ChannelHandlerContext ctx) throws Exception {
		authDispatcher.removeAuthQueue(ctx.channel().hashCode());
	}

}
