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

import drizzt.netty.domain.ClientRequest;
import drizzt.netty.queue.MessageQueue;

@Component
@Qualifier("serverHandler")
@Sharable
public class ServerHandler extends SimpleChannelInboundHandler<String> {

	private static final Logger Logger = LoggerFactory
			.getLogger(ServerHandler.class);

	@Autowired
	private HandlerDispatcher handlerDispatcher;

	@PostConstruct
	public void init() {
		new Thread(handlerDispatcher).start();
	}

	public void channelRead0(ChannelHandlerContext ctx, String msg)
			throws Exception {
		Logger.info("收到客户端信息：" + msg);
		ctx.channel().writeAndFlush("18888889527");
		System.out.println(ctx.channel().hashCode());
		MessageQueue messageQueue = new MessageQueue(
				new ConcurrentLinkedQueue<ClientRequest>());
		ClientRequest clientRequest = new ClientRequest(ctx.channel(), msg);
		messageQueue.add(clientRequest);
		handlerDispatcher.addMessageQueue(ctx.channel().hashCode(), messageQueue);
	}

	public HandlerDispatcher getHandlerDispatcher() {
		return handlerDispatcher;
	}

	public void setHandlerDispatcher(HandlerDispatcher handlerDispatcher) {
		this.handlerDispatcher = handlerDispatcher;
	}

}
