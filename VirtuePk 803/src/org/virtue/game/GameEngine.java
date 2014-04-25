package org.virtue.game;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.virtue.game.core.filter.Js5Filter;
import org.virtue.game.core.filter.LoginFilter;
import org.virtue.game.core.gametick.TickManager;
import org.virtue.game.core.logic.LogicProcessor;
import org.virtue.game.core.logic.impl.CharacterLogicUpdateEvent;
import org.virtue.game.core.synchronization.AsynchronousExecutorService;
import org.virtue.game.core.synchronization.SynchronousExecutorService;
import org.virtue.game.core.threads.AsynchronousThreadFactory;
import org.virtue.game.core.threads.LogicThreadFactory;
import org.virtue.game.core.threads.SynchronousThreadFactory;
import org.virtue.game.core.threads.WorkerThreadFactory;
import org.virtue.game.logic.region.RegionUpdateManager;
import org.virtue.script.GroovyManager;
import org.virtue.utility.GameClock;
import org.virtue.utility.TimeUtil;

/**
 * @author Taylor Moon
 * @since Jan 22, 2014
 */
public class GameEngine implements Runnable {

	/**
	 * Represents the worker pool.
	 */
	private final ExecutorService WORKER_POOL = Executors.newFixedThreadPool(7, new WorkerThreadFactory());
	
	/**
	 * Represents the asyncronous executor.
	 */
	private final ExecutorService ASYNCHRONOUS_POOL = Executors.newFixedThreadPool(10, new AsynchronousThreadFactory());
	
	/**
	 * Represents the syncronous executor.
	 */
	private final ExecutorService SYNCHRONOUS_POOL = Executors.newFixedThreadPool(10, new SynchronousThreadFactory()); 

	/**
	 * Represents the asynchronous executor.
	 */
	private final AsynchronousExecutorService ASYNCHRONOUS_EXECUTOR = new AsynchronousExecutorService();
	
	/**
	 * Represents the synchronous executor service.
	 */
	private final SynchronousExecutorService SYNCHRONOUS_EXECUTOR = new SynchronousExecutorService();
	
	/**
	 * Represents the logic service.
	 */
	private final ScheduledExecutorService LOGIC_SERVICE = Runtime.getRuntime().availableProcessors() >= 6 ? Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors() >= 12 ? 4 : 2, new LogicThreadFactory()) : Executors.newSingleThreadScheduledExecutor(new LogicThreadFactory());
	
	/**
	 * Represents the logic processor responsible for executed periodic logical tasks.
	 */
	private final LogicProcessor LOGIC_PROCESSOR = new LogicProcessor();
	
	/**
	 * Represents the tick manager.
	 */
	private final TickManager TICK_MANAGER = new TickManager();
	
	/**
	 * Represents the js5 monitor.
	 */
	private final Js5Filter JS5_FILTER = new Js5Filter();
	
	/**
	 * Represents the login filter.
	 */
	private final LoginFilter LOGIN_FILTER = new LoginFilter();
	
	/**
	 * Represents if the engine is running.
	 */
	private boolean running;
	
	/**
	 * Represents the cycle count.
	 */
	private long cycleCount;
	
	/**
	 * Represents the engine start time.
	 */
	private long startTime;
	
	/**
	 * Constructs a new {@code GameEngine.java}.
	 */
	public GameEngine() {
		startTime = System.currentTimeMillis();
		running = true;
		WORKER_POOL.execute(JS5_FILTER);
		LOGIC_SERVICE.scheduleAtFixedRate(LOGIN_FILTER, 0, 5, TimeUnit.MILLISECONDS);
//		ASYNCHRONOUS_POOL.execute(ASYNCHRONOUS_EXECUTOR);
//		SYNCHRONOUS_POOL.execute(SYNCHRONOUS_EXECUTOR);
		LOGIC_PROCESSOR.registerEvent(new CharacterLogicUpdateEvent());
		LOGIC_PROCESSOR.registerEvent(new RegionUpdateManager());
		//GroovyManager.Init();//XXX TODO fix this
	}
	
	@Override
	public void run() {
		while (running) {
			try {
				long currentTime = System.currentTimeMillis();
				TICK_MANAGER.processAllTicks();
				long elapsedTime = (System.currentTimeMillis() - currentTime);
				if (GameClock.ONE_TICK - elapsedTime > 0) {
					Thread.sleep(GameClock.ONE_TICK - elapsedTime);					
				} else {
					System.err.println("Warning - Missed one tick at cycle "+cycleCount);
				}
				cycleCount++;
			} catch (Exception e) {
				handleException(e);
			}
		}
	}
	
	/**
	 * Registers a logic event.
	 * @param event The event to register.
	 */
	public void registerLogicEvent(final Runnable event, long delay, long intervalDelay) {
		try {
			LOGIC_SERVICE.scheduleAtFixedRate(event, delay, intervalDelay, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			handleException(e);
		}
	}
	
	/**
	 * Registers a logic event.
	 * @param event The event to register.
	 */
	public void registerLogicEvent(final Runnable event) {
		try {
			LOGIC_SERVICE.execute(event);
		} catch (Exception e) {
			handleException(e);
		}
	}
	
	/**
	 * Called when an exception occurs.
	 * @param e The exception.
	 */
	public void handleException(Exception e) {
		long elapsedTime = System.currentTimeMillis() - startTime;
		System.err.println("An exception has been caught on engine cycle # " + cycleCount + ". Terminating process.");
		System.err.println("Total runtime: " + TimeUtil.millisecondsToSeconds(elapsedTime) + " seconds, " + TimeUtil.millisecondsToMinutes(elapsedTime) + " minutes, " + TimeUtil.millisecondsToHours(elapsedTime) + " hours, " + TimeUtil.millisecondsToDays(elapsedTime) + " days.");
		e.printStackTrace();
		System.exit(-1);
	}

	/**
	 * @return the aSYNCHRONOUS_EXECUTOR
	 */
	public AsynchronousExecutorService getAsynchronousExecutor() {
		return ASYNCHRONOUS_EXECUTOR;
	}

	/**
	 * @return the sYNCRONOUS_EXECUTOR
	 */
	public SynchronousExecutorService getSynchronousExecutor() {
		return SYNCHRONOUS_EXECUTOR;
	}

	/**
	 * @return the aSYNCHRONOUS_POOL
	 */
	public ExecutorService getAsycnhronousPool() {
		return ASYNCHRONOUS_POOL;
	}

	/**
	 * @return the sYNCHRONOUS_POOL
	 */
	public ExecutorService getSynchronousPool() {
		return SYNCHRONOUS_POOL;
	}

	/**
	 * @return the running
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * Causes the engine to shutdown.
	 */
	public void shutdown() {
		running = false;
		LOGIC_SERVICE.shutdown();
	}

	/**
	 * @return the tICK_MANAGER
	 */
	public TickManager getTickManager() {
		return TICK_MANAGER;
	}

	/**
	 * @return the logicService
	 */
	public ScheduledExecutorService getLogicService() {
		return LOGIC_SERVICE;
	}

	/**
	 * @return the lOGIC_PROCESSOR
	 */
	public LogicProcessor getLogicProcessor() {
		return LOGIC_PROCESSOR;
	}
	
	/**
	 * @return The worker count
	 */
	public static int getAvailableWorkerCount() {
		int availableProcessors = Runtime.getRuntime().availableProcessors();
		int workerCount = availableProcessors >= 6 ? availableProcessors - (availableProcessors >= 12 ? 7 : 5) : 1;
		return workerCount;
	}

	/**
	 * @return the js5Filter
	 */
	public Js5Filter getJs5Filter() {
		return JS5_FILTER;
	}

	/**
	 * @return the wORKER_POOL
	 */
	public ExecutorService getWorkerPool() {
		return WORKER_POOL;
	}

	/**
	 * @return the lOGIN_FILTER
	 */
	public LoginFilter getLoginFilter() {
		return LOGIN_FILTER;
	}
}
