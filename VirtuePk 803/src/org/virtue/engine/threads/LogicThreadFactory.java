package org.virtue.engine.threads;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Taylor Moon
 * @since Jan 27, 2014
 */
public class LogicThreadFactory implements ThreadFactory {
	
	/**
	 * Represents the number of threads in this pool.
	 */
	private AtomicInteger thread_number = new AtomicInteger(0);

	@Override
	public Thread newThread(Runnable event) {
		Thread thread = new Thread(event);
		thread.setName("Logic Thread#" + thread_number.getAndIncrement());
		thread.setPriority(Thread.NORM_PRIORITY);
		return thread;
	}
}