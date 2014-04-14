package org.virtue.engine.filter;

import java.util.ArrayDeque;
import java.util.Queue;

import org.virtue.network.session.impl.JS5Session;

/**
 * @author Taylor Moon
 * @since Jan 23, 2014
 */
public class Js5Filter implements Runnable {
	
	/**
	 * Represents a queue of sessions.
	 */
	private Queue<JS5Session> sessions = new ArrayDeque<>();

	@Override
	public void run() {
		for (;;) {
			JS5Session session;
			synchronized (sessions) {
				while ((session = sessions.poll()) == null) {
					try {
						sessions.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			session.processFileQueue();
		}
	}
	
	/**
	 * Queues a pending JS5Session
	 * @param session The session to queue.
	 */
	public void addPendingSession(JS5Session session) {
		synchronized (sessions) {
			sessions.add(session);
			sessions.notifyAll();
		}
	}
}
