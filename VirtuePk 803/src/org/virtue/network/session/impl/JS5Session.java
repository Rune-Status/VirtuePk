package org.virtue.network.session.impl;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.virtue.Launcher;
import org.virtue.cache.Cache;
import org.virtue.game.core.filter.Js5Filter;
import org.virtue.network.protocol.codec.ondemand.ArchiveRequest;
import org.virtue.network.protocol.codec.ondemand.FileResponse;
import org.virtue.network.protocol.codec.ondemand.UpdateEncryptionMessage;
import org.virtue.network.protocol.codec.ondemand.UpdateStatusMessage;
import org.virtue.network.protocol.codec.ondemand.UpdateVersionMessage;
import org.virtue.network.protocol.codec.ondemand.XorEncoder;
import org.virtue.network.session.Session;

/**
 * @author Taylor
 * @date Jan 14, 2014
 */
public class JS5Session extends Session {

	/**
	 * The cache file queue
	 */
	private final Deque<ArchiveRequest> fileQueue = new ArrayDeque<ArchiveRequest>();

	/**
	 * Idle update
	 */
	private boolean idle = true;

	/**
	 * Handshake was decoded or not
	 */
	private boolean handshakeComplete = false;
	
	/**
	 * The update service
	 */
	private Js5Filter service;

	/**
	 * Creates a new UpdateSession.
	 * @param context The channel handler context.
	 */
	public JS5Session(ChannelHandlerContext context) {
		super(context);
		service = Launcher.getEngine().getJs5Filter();
	}

	/**
	 * (non-Javadoc)
	 * @see org.xplur.network.session.Session#disconnected()
	 */
	@Override
	public void disconnect() {
		fileQueue.clear();
	}

	/**
	 * (non-Javadoc)
	 * @see org.xplur.network.session.Session#message(java.lang.Object)
	 */
	@Override
	public void decode(Object message) {
		if (handshakeComplete) {
			if (message instanceof ArchiveRequest) {
				ArchiveRequest request = (ArchiveRequest) message;
				synchronized (fileQueue) {
					if (request.isPriority()) {
						fileQueue.addFirst(request);
					} else {
						fileQueue.addLast(request);
					}
					if (idle) {
						service.addPendingSession(this);
						idle = false;
					}
				}
			} else if (message instanceof UpdateEncryptionMessage) {
				UpdateEncryptionMessage encryption = (UpdateEncryptionMessage) message;
				XorEncoder encoder = getContext().getChannel().getPipeline().get(XorEncoder.class);
				encoder.setKey(encryption.getKey());
			}
		} else {
			UpdateVersionMessage validationMessage = (UpdateVersionMessage) message;
			int status;
			if (validationMessage.getVersion() == Cache.SERVER_REVISION) {
				status = UpdateStatusMessage.STATUS_OK;
			} else {
				status = UpdateStatusMessage.STATUS_OUT_OF_DATE;
				System.err.println("CLIENT OUT OF DATE: " + getContext().getChannel().getId());
			}
			ChannelFuture future = getContext().getChannel().write(new UpdateStatusMessage(status, Cache.SERVER_REVISION));
			if (status == UpdateStatusMessage.STATUS_OK) {
				handshakeComplete = true;
			} else {
				future.addListener(ChannelFutureListener.CLOSE);
			}
		}
	}

	/**
	 * Processes the file queue.
	 */
	public void processFileQueue() {
		try {
			ArchiveRequest request;
			synchronized (fileQueue) {
				request = fileQueue.pop();
				if (fileQueue.isEmpty()) {
					idle = true;
				} else {
					service.addPendingSession(this);
					idle = false;
				}
			}
			if (request != null) {
				int type = request.getType();
				int file = request.getFile();
				Cache cache = Launcher.getCache();
				ChannelBuffer buffer;
				if (type == 0xFF && file == 0xFF) {
					buffer = ChannelBuffers.wrappedBuffer(Launcher.getCache().getChecksumtable());
				} else {
					buffer = ChannelBuffers.wrappedBuffer(cache.getStore().read(type, file));
					if (type != 255)
						buffer = buffer.slice(0, buffer.readableBytes() - 2);
				}
				getContext().getChannel().write(new FileResponse(request.isPriority(), type, file, buffer));
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
