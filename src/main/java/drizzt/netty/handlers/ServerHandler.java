package drizzt.netty.handlers;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.ConcurrentLinkedQueue;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import drizzt.netty.dispatcher.AuthDispatcher;
import drizzt.netty.domain.AuthQueue;
import drizzt.netty.domain.ClientRequest;
import drizzt.netty.protobuf.DrizztProtocol.Msg;

@Component
@Qualifier("serverHandler")
@Sharable
public class ServerHandler extends SimpleChannelInboundHandler<Msg> {

	private static final Logger Logger = LoggerFactory
			.getLogger(ServerHandler.class);

	@Autowired
	private AuthDispatcher authDispatcher;

	@PostConstruct
	public void init() {
		new Thread(authDispatcher).start();
	}

	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Logger.info("接入一个channel：" + ctx.channel().hashCode());
		AuthQueue authQueue = new AuthQueue(
				new ConcurrentLinkedQueue<ClientRequest>());
		authDispatcher.addAuthQueue(ctx.channel().hashCode(), authQueue);
	}

	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		Logger.error("关掉一个channel：" + ctx.channel().hashCode());
		authDispatcher.removeAuthQueue(ctx.channel().hashCode());
		ctx.channel().close();
	}

	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		Logger.error("出现一个异常：" + ctx.channel().hashCode());
		cause.printStackTrace();
		authDispatcher.removeAuthQueue(ctx.channel().hashCode());
		ctx.channel().close();
	}

	public void channelRead0(ChannelHandlerContext ctx, Msg msg)
			throws Exception {
		Msg.Builder builder = Msg.newBuilder();
		builder.getAuthResponseBuilder().setDes("成功");
		builder.getAuthResponseBuilder().setResultCode("1");
		builder.getAuthResponseBuilder().setTransId("123");
		ctx.channel().writeAndFlush(builder.build());
		ClientRequest clientRequest = new ClientRequest(ctx.channel(), msg,
				System.currentTimeMillis());
		authDispatcher.addAuth(clientRequest);
	}

}
