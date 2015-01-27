package test;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class TcpClientHandler extends SimpleChannelInboundHandler<String> {

	protected void channelRead0(ChannelHandlerContext ctx, String msg)
			throws Exception {
		System.out.println(msg);
	}


}
