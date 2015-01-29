package drizzt.netty.handlers;

import io.netty.channel.ChannelFutureListener;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import drizzt.netty.domain.ClientRequest;
import drizzt.netty.queue.MessageQueue;

/**
 * 类名称：HandlerDispatcher <br/>
 * 类描述：逻辑处理器 <br/>
 * 创建时间：2015年1月28日 上午9:48:28 <br/>
 * 
 * @author Drizzt
 * @version
 */
@Component
public class HandlerDispatcher implements Runnable {

	private static final Logger Logger = LoggerFactory
			.getLogger(HandlerDispatcher.class);

	@Autowired
	@Qualifier("executor")
	private Executor executor;
	@Resource
	private Map<Integer, MessageQueue> queueMap;
	private boolean running;

	@PostConstruct
	public void init() {
		if (!running) {
			running = true;
		}
	}

	@PreDestroy
	public void stop() {
		running = false;
	}

	public void run() {
		while (running) {
			Set<Integer> keySet = queueMap.keySet();
			for (Integer key : keySet) {
				MessageQueue messageQueue = queueMap.get(key);
				if (messageQueue == null || messageQueue.size() <= 0
						|| messageQueue.isRunning()) {
					continue;
				}
				Logger.info("这是第" + key + "个map");
				MessageWorker messageWorker = new MessageWorker(messageQueue);
				this.executor.execute(messageWorker);
			}
		}
	}

	/**
	 * 类名称：MessageWorker <br/>
	 * 类描述：消息队列处理线程实现 <br/>
	 * 创建时间：2015年1月28日 上午9:51:09 <br/>
	 * 
	 * @author Drizzt
	 * @version
	 */
	private final class MessageWorker implements Runnable {
		private MessageQueue messageQueue;
		private ClientRequest clientRequest;

		private MessageWorker(MessageQueue messageQueue) {
			messageQueue.setRunning(true);
			clientRequest = messageQueue.getClientQueue().poll();
			this.messageQueue = messageQueue;
		}

		public void run() {
			try {
				handMessageQueue();
			} finally {
				messageQueue.setRunning(false);
			}
		}

		/**
		 * 处理消息队列
		 * 
		 * @throws InterruptedException
		 */
		private void handMessageQueue() {
			Logger.info("处理：" + clientRequest.getChannel().hashCode() + "_"
					+ clientRequest.getMsg());
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			clientRequest.getChannel().writeAndFlush("close")
					.addListener(ChannelFutureListener.CLOSE);
		}

	}

}
