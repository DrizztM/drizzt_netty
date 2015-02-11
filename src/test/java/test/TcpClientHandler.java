package test;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import drizzt.netty.protobuf.DrizztProtocol.Msg;

public class TcpClientHandler extends SimpleChannelInboundHandler<Msg> {

	protected void channelRead0(ChannelHandlerContext ctx, Msg msg)
			throws Exception {
		System.out.println(msg.getAuthResponse().getDes());
		Msg.Builder builder = Msg.newBuilder();
		builder.getAuthRequestBuilder().setAndroidId("1");
		builder.getAuthRequestBuilder().setAppId("2222222");
		builder.getAuthRequestBuilder().setBluetooth("3");
		builder.getAuthRequestBuilder().setDeveloperId("4");
		builder.getAuthRequestBuilder().setImei("5");
		builder.getAuthRequestBuilder().setImsi("6");
		builder.getAuthRequestBuilder().setMac("7");
		builder.getAuthRequestBuilder().setPn("8");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		new TcpClient().sendMsg(builder.build());
	}

}
