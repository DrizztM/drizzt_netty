package drizzt.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;

import java.net.InetSocketAddress;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TCPServer {

	private static final Logger Logger = LoggerFactory.getLogger(TCPServer.class);
	
	@Autowired
	private ServerBootstrap b;
	
	@Autowired
	private InetSocketAddress tcpPort;

	private Channel serverChannel;

	@PostConstruct
	public void start() throws Exception {
		Logger.info("Starting server at " + tcpPort);
		serverChannel = b.bind(tcpPort).sync().channel().closeFuture().sync()
				.channel();
	}

	@PreDestroy
	public void stop() {
		serverChannel.close();
	}

}
