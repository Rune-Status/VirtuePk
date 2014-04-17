package org.virtue.game.core.synchronization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.virtue.Launcher;

/**
 * @author Taylor Moon
 * @since Jan 22, 2014
 */
public class AsynchronousExecutorService implements Runnable {
	
	/**
	 * Represents a list of tasks to be executed.
	 */
	private List<Runnable> tasks = Collections.synchronizedList(new ArrayList<Runnable>());

	/**
	 * Represents if this executor service is running.
	 */
	private boolean running = true;

	@Override
	public void run() {
		try {
			while (running) {
				synchronized (tasks) {
					for (int t = 0; t < tasks.size(); t++) {
						Runnable task = tasks.get(t);
						if (task != null) {
							task.run();
						}

					}
				}
			}
		} catch (Exception e) {
			Launcher.getEngine().handleException(e);
		}
	}
	
	/**
	 * Registers a task in the collection.
	 * @param task The task to queue.
	 */
	public void register(Runnable task) {
		synchronized (tasks) {
			tasks.add(task);
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
