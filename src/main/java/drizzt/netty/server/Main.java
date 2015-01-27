package drizzt.netty.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import drizzt.netty.cfg.DrizztConfig;

public class Main {
	
	private static final Logger Logger = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {
		Logger.info("Starting application context");
		@SuppressWarnings("resource")
		AbstractApplicationContext ctx = new AnnotationConfigApplicationContext(
				DrizztConfig.class);
		ctx.registerShutdownHook();
	}

}
