package test;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import drizzt.netty.protobuf.DrizztProtocol.Msg;

public class TcpClientHandler extends SimpleChannelInboundHandler<Msg> {

	protected void channelRead0(ChannelHandlerContext ctx, Msg msg)
			throws Exception {
		System.out.println(msg.getAuthResponse().getDes());
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		new TcpClient().sendMsg("auth");
	}

}
