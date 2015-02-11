package test;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

import org.apache.log4j.Logger;

import drizzt.netty.protobuf.DrizztProtocol;
import drizzt.netty.protobuf.DrizztProtocol.Msg;

public class TcpClient {
	private static final Logger logger = Logger.getLogger(TcpClient.class);
//	public static String HOST = "115.29.112.103";
	public static String HOST = "127.0.0.1";
	public static int PORT = 9999;

	public static Bootstrap bootstrap = getBootstrap();
	public static Channel channel = getChannel(HOST, PORT);

	/**
	 * 初始化Bootstrap
	 * 
	 * @return
	 */
	public static final Bootstrap getBootstrap() {
		EventLoopGroup group = new NioEventLoopGroup();
		Bootstrap b = new Bootstrap();
		b.group(group).channel(NioSocketChannel.class);
		b.handler(new ChannelInitializer<Channel>() {
			@Override
			protected void initChannel(Channel ch) throws Exception {
				ChannelPipeline pipeline = ch.pipeline();
				pipeline.addLast(new ProtobufVarint32FrameDecoder());
				pipeline.addLast(new ProtobufDecoder(DrizztProtocol.Msg.getDefaultInstance()));
				pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
				pipeline.addLast(new ProtobufEncoder());
				pipeline.addLast("handler", new TcpClientHandler());
			}
		});
		b.option(ChannelOption.SO_KEEPALIVE, true);
		return b;
	}

	public static final Channel getChannel(String host, int port) {
		Channel channel = null;
		try {
			channel = bootstrap.connect(host, port).sync().channel();
		} catch (Exception e) {
			logger.error(
					String.format("连接Server(IP[%s],PORT[%s])失败", host, port), e);
			return null;
		}
		return channel;
	}

	public void sendMsg(Msg msg) throws Exception {
		if (channel != null) {
			channel.writeAndFlush(msg).sync();
		} else {
			logger.warn("消息发送失败,连接尚未建立!");
		}
	}

	public static void main(String[] args) throws Exception {
		Msg.Builder builder = Msg.newBuilder();
		builder.getAuthRequestBuilder().setAndroidId("1");
		builder.getAuthRequestBuilder().setAppId("2222222");
		builder.getAuthRequestBuilder().setBluetooth("3");
		builder.getAuthRequestBuilder().setDeveloperId("4");
		builder.getAuthRequestBuilder().setImei("5");
		builder.getAuthRequestBuilder().setImsi("6");
		builder.getAuthRequestBuilder().setMac("7");
		builder.getAuthRequestBuilder().setPn("8");
		new TcpClient().sendMsg(builder.build());
	}
}
