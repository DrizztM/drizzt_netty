package drizzt.netty.handlers;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("springProtocolInitializer")
public class StringProtocolInitalizer extends ChannelInitializer<SocketChannel> {

	@Autowired
	StringDecoder stringDecoder;

	@Autowired
	StringEncoder stringEncoder;

	@Autowired
	ServerHandler serverHandler;
	
	@Autowired
	AuthReadTimeoutHandler authReadTimeoutHandler;

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast("timeout", authReadTimeoutHandler);
		pipeline.addLast("decoder", stringDecoder);
		pipeline.addLast("handler", serverHandler);
		pipeline.addLast("encoder", stringEncoder);
	}

}
