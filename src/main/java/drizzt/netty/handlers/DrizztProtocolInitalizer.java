package drizzt.netty.handlers;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import drizzt.netty.protobuf.DrizztProtocol;

@Component
@Qualifier("springProtocolInitializer")
public class DrizztProtocolInitalizer extends ChannelInitializer<SocketChannel> {

	@Autowired
	ServerHandler serverHandler;

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast(new ProtobufVarint32FrameDecoder());
		pipeline.addLast(new ProtobufDecoder(DrizztProtocol.Msg.getDefaultInstance()));
		pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
		pipeline.addLast(new ProtobufEncoder());
		pipeline.addLast("handler", serverHandler);
	}

}
