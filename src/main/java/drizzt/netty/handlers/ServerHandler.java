package drizzt.netty.handlers;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import drizzt.netty.domain.ClientRequest;
import drizzt.netty.domain.MessageQueue;

@Component
@Qualifier("serverHandler")
@Sharable
public class ServerHandler extends SimpleChannelInboundHandler<String> {

	private static final Logger Logger = LoggerFactory
			.getLogger(ServerHandler.class);

	@Autowired
	private Environment env;

	@Autowired
	private HandlerDispatcher handlerDispatcher;

	@Resource
	private Map<Integer, MessageQueue> queueMap;

	@PostConstruct
	public void init() {
		new Thread(handlerDispatcher).start();
	}

	public void channelRead0(ChannelHandlerContext ctx, String msg)
			throws Exception {
		int vipSize = Integer.parseInt(env.getProperty("queue.vipSize"));
		int userSize = Integer.parseInt(env.getProperty("queue.userSize"));
		Logger.info("收到客户端信息：" + ctx.channel().hashCode() + "_" + msg);
		ctx.channel().writeAndFlush("18888889527");
		ClientRequest clientRequest = new ClientRequest(ctx.channel(), msg);
		queueMap.get(Math.abs(ctx.channel().hashCode())%(vipSize+userSize)).add(clientRequest);
	}

	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		super.exceptionCaught(ctx, cause);
	}

}
