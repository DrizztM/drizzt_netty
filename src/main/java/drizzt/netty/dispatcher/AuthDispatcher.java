package drizzt.netty.dispatcher;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import drizzt.netty.domain.AuthQueue;
import drizzt.netty.domain.ClientRequest;
import drizzt.netty.protobuf.DrizztProtocol.Msg;

/**
 * 类名称：AuthDispatcher <br/>
 * 类描述：验证逻辑处理器 <br/>
 * 创建时间：2015年1月28日 上午9:48:28 <br/>
 * 
 * @author Drizzt
 * @version
 */
@Component
public class AuthDispatcher implements Runnable {

	private static final Logger Logger = LoggerFactory
			.getLogger(AuthDispatcher.class);

	@Autowired
	private ThreadPoolExecutor threadPoolExecutor;
	@Autowired
	private Environment env;
	private Map<Integer, AuthQueue> sessionAQ;
	private boolean running;

	@PostConstruct
	public void init() {
		if (!running) {
			running = true;
			sessionAQ = new ConcurrentHashMap<Integer, AuthQueue>();
		}
	}

	@PreDestroy
	public void stop() {
		running = false;
	}

	public void run() {
		while (running) {
			Set<Integer> keySet = sessionAQ.keySet();
			for (Integer key : keySet) {
				AuthQueue authQueue = sessionAQ.get(key);
				if (authQueue == null || authQueue.size() <= 0
						|| authQueue.isRunning()) {
					continue;
				}
				Worker worker = new Worker(authQueue);
				threadPoolExecutor.execute(worker);
				Logger.debug("线程池中线程数目：" + threadPoolExecutor.getPoolSize()
						+ "，队列中等待执行的任务数目："
						+ threadPoolExecutor.getQueue().size() + "，已执行完毕的任务数目："
						+ threadPoolExecutor.getCompletedTaskCount());
			}
			try {
				Thread.sleep(Integer.parseInt(env
						.getProperty("dispatcher.sleepTime")));
			} catch (InterruptedException e) {
				Logger.error("", e);
			}
		}
	}

	public void addAuthQueue(Integer channelId, AuthQueue authQueue) {
		sessionAQ.put(channelId, authQueue);
	}

	public void removeAuthQueue(Integer key) {
		AuthQueue authQueue = sessionAQ.remove(key);
		if (authQueue != null) {
			authQueue.clear();
		}
	}

	public boolean checkAuthQueue(Integer key) {
		return sessionAQ.containsKey(key);
	}

	public boolean addAuth(ClientRequest request) {
		boolean b = false;
		int channelId = request.getChannel().hashCode();
		AuthQueue authQueue = sessionAQ.get(channelId);
		if (authQueue == null) {
			request.getChannel().close();
			Logger.error("", new IllegalStateException());
		} else {
			authQueue.empty();
			b = authQueue.add(request);
		}
		return b;
	}

	/**
	 * 类名称：MessageWorker <br/>
	 * 类描述：消息队列处理线程实现 <br/>
	 * 创建时间：2015年1月28日 上午9:51:09 <br/>
	 * 
	 * @author Drizzt
	 * @version
	 */
	private final class Worker implements Runnable {
		private AuthQueue authQueue;
		private ClientRequest clientRequest;

		private Worker(AuthQueue authQueue) {
			authQueue.setRunning(true);
			clientRequest = authQueue.getClientQueue().poll();
			this.authQueue = authQueue;
		}

		public void run() {
			try {
				handAuthQueue();
			} finally {
				authQueue.setRunning(false);
			}
		}

		/**
		 * 处理消息队列
		 * 
		 * @throws InterruptedException
		 */
		private void handAuthQueue() {
			Logger.info("处理：" + clientRequest.getChannel().hashCode() + "_"
					+ clientRequest.getMsg().getAuthRequest().getAppId());
			if (System.currentTimeMillis() - clientRequest.getCurrentTime() < Integer
					.parseInt(env.getProperty("dispatcher.timeout"))) {
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Msg.Builder builder = Msg.newBuilder();
				builder.getAuthResponseBuilder().setDes("成功");
				builder.getAuthResponseBuilder().setResultCode("1");
				builder.getAuthResponseBuilder().setTransId("123");
				clientRequest.getChannel().writeAndFlush(builder.build());
				// .addListener(ChannelFutureListener.CLOSE)
			}
		}
	}

}
