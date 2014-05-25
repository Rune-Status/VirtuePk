package org.virtue.game.logic.node.interfaces.impl;

import org.virtue.game.logic.events.InputEnteredEvent;
import org.virtue.game.logic.node.entity.player.Player;
import org.virtue.game.logic.node.interfaces.AbstractInterface;
import org.virtue.game.logic.node.interfaces.ActionButton;
import org.virtue.game.logic.node.interfaces.RSInterface;
import org.virtue.game.logic.social.ChannelPermission;
import org.virtue.game.logic.social.ChannelRank;
import org.virtue.network.protocol.messages.ClientScriptVar;
import org.virtue.utility.StringUtils;
import org.virtue.utility.StringUtils.FormatType;

public class FriendsChatSettings extends AbstractInterface {

	public FriendsChatSettings(Player p) {
		super(RSInterface.FRIENDS_CHAT_SETTINGS, p);
	}
	
	public void sendPermission (ChannelPermission permission, ChannelRank rank) {
		String name = "";
		switch (rank) {
		case GUEST:
			name = "Anyone";
			break;
		case FRIEND:
			name = "Any friends";
			break;
		case RECRUIT:
		case CORPORAL:
		case SERGEANT:
		case LIEUTENANT:
		case CAPTAIN:
		case GENERAL:
			name = rank.getName()+"+";
			break;
		case OWNER:
			name = (permission.equals(ChannelPermission.LOOTSHARE) ? "No one" : "Only me");
			break;
		case JMOD:
			return;//This should never occur. If it does, there must be an error
		}
		if (name.isEmpty()) {
			return;
		}
		//System.out.println("Setting permission for "+permission+" to "+name);
		setComponentText(permission.getSettingsID(), name);
	}
	
	public void sendPrefix (String prefix) {
		if (prefix == null || prefix.isEmpty()) {
			prefix = "Chat disabled";
		} 
		setComponentText(1, prefix);
		//System.out.println("New prefix: "+prefix);
	}

	@Override
	public void postSend() {
		getPlayer().getChatManager().setFriendsChatSettings(this);
		getPlayer().getChatManager().getFriendManager().sendFriendsChatSettings();
		sendInterfaceSettings(22, 199, 0, 254);
		setComponentHidden(49, true);
		setComponentHidden(63, true);
		setComponentHidden(77, true);
		setComponentHidden(91, true);
		//11 = [coinshare]
	}

	@Override
	public void handleActionButton(int component, int slot1, int slot2, ActionButton button) {
		ChannelRank rank = null;
		switch (component) {
		case 1:
			if (button.equals(ActionButton.ONE)) {//Set channel prefix
				getPlayer().getPacketDispatcher().dispatchInterface(RSInterface.DIALOG_BOX);
				getPlayer().getPacketDispatcher().dispatchInterface(RSInterface.INPUT_DIALOG);
				getPlayer().getPacketDispatcher().dispatchClientScriptVar(new ClientScriptVar(110, "Enter chat prefix:"));
				getPlayer().setInputEvent(onPrefixChange);
			} else if (button.equals(ActionButton.TWO)) {//Disable channel
				getPlayer().getChatManager().getFriendManager().setFriendsChatPrefix("");				
			}
			break;
		case 2://Enter
		case 3://Talk
		case 4://Kick
		case 5://Lootshare
			rank = getRankFromButton(button);
			ChannelPermission permission = ChannelPermission.forComponent(component);
			if (rank == null || permission == null) {
				return;
			}
			if (permission.equals(ChannelPermission.LOOTSHARE) && rank.equals(ChannelRank.GUEST)) {
				rank = ChannelRank.OWNER;
			}
			//System.out.println("Selected rank: "+rank+" for component: "+component);
			getPlayer().getChatManager().getFriendManager().setFriendsChatPermission(permission, rank);
			break;
		case 12://Coinshare
			
		default:
			System.out.println("Unhandled friends chat settings button: component="+component+", slot1="+slot1+", slot2="+slot2+", button="+button.getID());
			break;
		}		
	}
	
	private InputEnteredEvent onPrefixChange = new InputEnteredEvent () {
		@Override
		public void onInputEntered(Object input) {
			String prefix = StringUtils.format((String) input, FormatType.NAME).trim();
			if (prefix.isEmpty()) {
				return;
			}
			getPlayer().getChatManager().getFriendManager().setFriendsChatPrefix(prefix);
		}
		@Override
		public void onCancel() { }		
	};

	@Override
	public int getTabID() {
		return -1;
	}
	
	public static ChannelRank getRankFromButton (ActionButton button) {
		switch (button) {
		case ONE:
			return ChannelRank.GUEST;
		case TWO:
			return ChannelRank.FRIEND;
		case THREE:
			return ChannelRank.RECRUIT;
		case FOUR:
			return ChannelRank.CORPORAL;
		case FIVE:
			return ChannelRank.SERGEANT;
		case SIX:
			return ChannelRank.LIEUTENANT;
		case SEVEN:
			return ChannelRank.CAPTAIN;
		case EIGHT:
			return ChannelRank.GENERAL;
		case NINE:
			return ChannelRank.OWNER;
		default:
			return null;	
		}
	}

}
