package org.virtue.network.protocol.handlers.impl;

import org.virtue.game.logic.social.OnlineStatus;
import org.virtue.network.protocol.handlers.PacketHandler;
import org.virtue.network.session.impl.WorldSession;

/**
 * @author Virtue Development Team 2014 (c).
 * @since Apr 20, 2014
 */
public class OnlineStatusHandler extends PacketHandler<WorldSession> {

    @Override
    public void handle(WorldSession session) {
        OnlineStatus onlineStatus = OnlineStatus.forCode(getFlag("onlineStatus", -1));
        if (onlineStatus == null) {
            return;//Invalid online status
        }
        int val1 = getFlag("publicStatus", -1);
        int val3 = getFlag("tradeStatus", -1);
        session.getPlayer().getChatManager().setOnlineStatus(onlineStatus);
        System.out.println("val1="+val1+", onlineStatus="+onlineStatus.getStatusCode()+", val3="+val3);
    }
    
}
