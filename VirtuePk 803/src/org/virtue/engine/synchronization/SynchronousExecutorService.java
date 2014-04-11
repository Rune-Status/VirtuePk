package org.virtue.engine.synchronization;

import java.util.ArrayDeque;
import java.util.Queue;

import org.virtue.Launcher;

/**
 * @author Taylor Moon
 * @since Jan 22, 2014
 */
public class SynchronousExecutorService implements Runnable {
	
	/**
	 * Represents a queue of tasks to be executed
	 */
	private Queue<Runnable> tasks = new ArrayDeque<>();
	
	/**
	 * Represents if this executor service is running.
	 */
	private boolean running = true;

	@Override
	public void run() {
		try {
			while (running) {
				synchronized (tasks) {
					Runnable task = tasks.poll();
					if (task != null) {
						task.run();
					}
				}
			}
		} catch (Exception e) {
			Launcher.getEngine().handleException(e);
		}
	}
	
	/**
	 * Queues a task in the queue.
	 * @param runnable The task to queue.
	 */
	public void queueTask(Runnable runnable) {
		synchronized (tasks) {
			tasks.add(runnable);
		}
	}

	/**
	 * @return the running
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * @param running the running to set
	 */
	public void setRunning(boolean running) {
		this.running = running;
	}
}
