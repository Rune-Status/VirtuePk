package org.virtue.game.core.threads;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Taylor Moon
 * @since Jan 23, 2014
 */
public class MainThreadFactory implements ThreadFactory {

	/**
	 * Represents the number of threads in this pool.
	 */
	private AtomicInteger thread_number = new AtomicInteger(0);

	@Override
	public Thread newThread(Runnable event) {
		Thread thread = new Thread(event);
		thread.setName("Main Thread#" + thread_number.getAndIncrement());
		thread.setPriority(Thread.MAX_PRIORITY);
		return thread;
	}
}
