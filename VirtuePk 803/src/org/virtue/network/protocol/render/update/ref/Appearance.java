package org.virtue.network.protocol.render.update.ref;

import org.virtue.game.item.Item;
import org.virtue.game.node.entity.player.Player;
import org.virtue.game.node.entity.player.container.Equipment;
import org.virtue.network.protocol.packet.RS3PacketBuilder;
import org.virtue.utility.Encryption;

/**
 * @author Taylor
 * @version 1.0
 */
public class Appearance {
	
	/**
	 * @author Taylor
	 * @version 1.0
	 */
	public enum Gender { MALE, FEMALE }
	
	/**
	 * @author Taylor
	 * @version 1.0
	 */
	public enum RenderState { AS_NPC, AS_HUMAN, AS_INVISIBLE };
	
	/**
	 * Represents the title ID.
	 */
	private int title = -1;
	
	/**
	 * Represents the body styles.
	 */
	private int[] bodyStyles = new int[7];
	
	/**
	 * Represents the body colors.
	 */
	private int[] bodyColors = new int[10];
	
	/**
	 * Represents the {@link Gender}.
	 */
	private Gender gender = Gender.MALE;
	
	/**
	 * Represents the {@link RenderState}.
	 */
	private RenderState state = RenderState.AS_HUMAN;
	
	/**
	 * Represenst the appearance data buffer.
	 */
	private byte[] buffer;
	
	/**
	 * Represents the buffer hash.
	 */
	private byte[] bufferHash;
	
	/**
	 * Represents the NPC ID if rendering as {@code AS_NPC}.
	 */
	private short npcId = -1;
	
	/**
	 * Represenst the render emote.
	 */
	private int renderEmote = -1;
	
	/**
	 * Represents the skull icon ID that hovers above the player's head.
	 */
	private int skullIconId = -1;
	
	/**
	 * Represents the prayer icon ID that hovers above the player's head.
	 */
	private int prayerIconId = -1;

	/**
	 * Represents the player.
	 */
	private Player player;

	/**
	 * Constructs a new {@code Appearance.java}.
	 * @param player The player.
	 */
	public Appearance(Player player) {
		this.player = player;
		loadMaleBody();
		if (player != null)
			load();
	}
	
	public void load() {
		RS3PacketBuilder buffer = new RS3PacketBuilder();
		int mask = 0;
		if (gender.equals(Gender.FEMALE)) {
			mask |= 0x1;
		}
		if (state.equals(RenderState.AS_NPC) && npcId != -1) {
			mask |= 0x2;
		}
		if (title != -1) {
			mask |= title > 32 && title <= 37 ? 0x80 : 0x40;
		}
		buffer.put(mask);
		if (title != -1)
			buffer.putString("Swag");
		buffer.put(skullIconId);
		buffer.put(prayerIconId);
		buffer.put(state.equals(RenderState.AS_INVISIBLE) ? 1 : 0);
		if (state.equals(RenderState.AS_NPC) && npcId >= 0) {
			buffer.putShort(-1);
			buffer.putShort(npcId);
			buffer.put(0);
			// we still gotta put data even if their invisible.
		} else if (state.equals(RenderState.AS_HUMAN) || state.equals(RenderState.AS_INVISIBLE)) {
			for (int index = 0; index < 4; index++) {
				Item item = player.getEquipment().getItems().get(index);
				if (item == null)
					buffer.put(0);
				else
					buffer.putShort(32768 + item.getEquipId());
			}
			Item item = player.getEquipment().getItems().get(Equipment.SLOT_CHEST);
			buffer.putShort(item == null ? 0x100 + getBodyStyles()[2] : 32768 + item.getEquipId());
			item = player.getEquipment().getItems().get(Equipment.SLOT_SHIELD);
			if (item == null)
				buffer.put(0);
			else
				buffer.putShort(32768 + item.getEquipId());
			item = player.getEquipment().getItems().get(Equipment.SLOT_CHEST);
			if (item == null)
				buffer.putShort(0x100 + getBodyStyles()[3]);
			else
				buffer.put(0);
			item = player.getEquipment().getItems().get(Equipment.SLOT_LEGS);
			buffer.putShort(item == null ? 0x100 + getBodyStyles()[5] : 32768 + item.getEquipId());
			item = player.getEquipment().getItems().get(Equipment.SLOT_HAT);
			if (item == null)
				buffer.putShort(0x100 + getBodyStyles()[0]);
			else
				buffer.put(0);
			item = player.getEquipment().getItems().get(Equipment.SLOT_HANDS);
			buffer.putShort(item == null ? 0x100 + getBodyStyles()[4] : 32768 + item.getEquipId());
			item = player.getEquipment().getItems().get(Equipment.SLOT_FEET);
			buffer.putShort(item == null ? 0x100 + getBodyStyles()[6] : 32768 + item.getEquipId());
			item = player.getEquipment().getItems().get(gender.equals(Gender.MALE) ? Equipment.SLOT_HAT : Equipment.SLOT_CHEST);
			if (item == null)
				buffer.putShort(0x100 + getBodyStyles()[1]);
			else
				buffer.put(0);
			item = player.getEquipment().getItems().get(Equipment.SLOT_AURA);
			if (item == null)
				buffer.put(0);
			else
				buffer.putShort(32768 + item.getEquipId());
			buffer.putShort(0);
			for (int index = 0; index < getBodyColors().length; index++)
				buffer.put(getBodyColors()[index]);
		}
		buffer.putShort(getRenderEmote());
		buffer.putString(player.getAccount().getUsername().getName());
		buffer.put(138);
		buffer.put(138);
		buffer.put(-1);
		buffer.put(npcId >= 0 ? 1 : 0);
		if (npcId >= 0) {
//			NPCDefinitions defs = NPCDefinitions.getNPCDefinitions(npcId);
//			buffer.putShort(defs.anInt876);
//			buffer.putShort(defs.anInt842);
//			buffer.putShort(defs.anInt884);
//			buffer.putShort(defs.anInt875);
//			buffer.put(defs.anInt875);
		}
		byte[] appeareanceData = new byte[buffer.getPosition()];
		System.arraycopy(buffer.buffer(), 0, appeareanceData, 0, appeareanceData.length);
		byte[] md5Hash = Encryption.MD5Encryption(appeareanceData);
		this.buffer = appeareanceData;
		bufferHash = md5Hash;
	}

	/**
	 * @return The size.
	 */
	public int getSize() {
		return 1;
	}

	/**
	 * @return The render emote.
	 */
	public int getRenderEmote() {
		if (renderEmote > 0)
			return renderEmote;
		Item weapon = player.getEquipment().getItems().get(3);
		if (weapon != null)
//			return weapon.getDefinitions().getRenderAnimId();
			return -1;
		return 1426;
	}

	/**
	 * Loads the male body style.
	 */
	public void loadMaleBody() {
		if (gender.equals(Gender.FEMALE))
			try {
				throw new IllegalAccessException("ERROR: Male body cannot be loaded for a female render.");
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		getBodyStyles()[0] = 3;
		getBodyStyles()[1] = 14;
		getBodyStyles()[2] = 18;
		getBodyStyles()[3] = 26;
		getBodyStyles()[4] = 34;
		getBodyStyles()[5] = 38;
		getBodyStyles()[6] = 42;
		getBodyColors()[2] = 16;
		getBodyColors()[1] = 16;
		getBodyColors()[0] = 3;
	}

	/**
	 * Loads the female body style.
	 */
	public void loadFemaleBody() {
		if (gender.equals(Gender.MALE))
			try {
				throw new IllegalAccessException("ERROR: Female body cannot be loaded for a male render.");
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		getBodyStyles()[0] = 48;
		getBodyStyles()[1] = 57;
		getBodyStyles()[2] = 57;
		getBodyStyles()[3] = 65;
		getBodyStyles()[4] = 68;
		getBodyStyles()[5] = 77;
		getBodyStyles()[6] = 80;
		getBodyColors()[2] = 16;
		getBodyColors()[1] = 16;
		getBodyColors()[0] = 3;
	}

	public byte[] getBuffer() {
		return buffer;
	}

	public byte[] getEncryptedBuffer() {
		return bufferHash;
	}

	public void setStyle(int part, int opcode) {
		getBodyStyles()[part] = opcode;
	}

	public void setColor(int part, int opcode) {
		getBodyColors()[part] = (byte) opcode;
	}

	public void setHairStyle(int opcode) {
		getBodyStyles()[0] = opcode;
	}

	public void setTopStyle(int opcode) {
		getBodyStyles()[2] = opcode;
	}

	public int getTopStyle() {
		return getBodyStyles()[2];
	}

	public void setArmsStyle(int part) {
		getBodyStyles()[3] = part;
	}

	public void setWristsStyle(int part) {
		getBodyStyles()[4] = part;
	}

	public void setLegsStyle(int part) {
		getBodyStyles()[5] = part;
	}

	public int getHairStyle() {
		return getBodyStyles()[0];
	}

	public void setBeardStyle(int i) {
		getBodyStyles()[1] = i;
	}

	public int getBeardStyle() {
		return getBodyStyles()[1];
	}

	public void setFacialHair(int i) {
		getBodyStyles()[1] = i;
	}

	public int getFacialHair() {
		return getBodyStyles()[1];
	}

	public void setSkinColor(int color) {
		getBodyColors()[4] = (byte) color;
	}

	public int getSkinColor() {
		return getBodyColors()[4];
	}

	public void setHairColor(int color) {
		getBodyColors()[0] = (byte) color;
	}

	public void setTopColor(int color) {
		getBodyColors()[1] = (byte) color;
	}

	public void setLegsColor(int color) {
		getBodyColors()[2] = (byte) color;
	}

	public int getHairColor() {
		return getBodyColors()[0];
	}

	public void setTitle(int title) {
		this.title = title;
	}

	/**
	 * @return The skullIconId
	 */
	public int getSkullIconId() {
		return skullIconId;
	}

	/**
	 * @param skullIconId The skullIconId to set
	 */
	public void setSkullIconId(int skullIconId) {
		this.skullIconId = skullIconId;
	}

	/**
	 * @return The prayerIconId
	 */
	public int getPrayerIconId() {
		return prayerIconId;
	}

	/**
	 * @param prayerIconId The prayerIconId to set
	 */
	public void setPrayerIconId(int prayerIconId) {
		this.prayerIconId = prayerIconId;
		load();
	}
	
	public void setGender(Gender gender) {
		this.gender = gender;
		switch(gender) {
		case FEMALE:
			loadFemaleBody();
			break;
		case MALE:
			loadMaleBody();
			break;
		default:
			break;
		}
	}
	
	/**
	 * @return The bodyStyles
	 */
	private int[] getBodyStyles() {
		return bodyStyles;
	}

	/**
	 * @param bodyStyles The bodyStyles to set
	 */
	public void setBodyStyles(int[] bodyStyles) {
		this.bodyStyles = bodyStyles;
	}

	/**
	 * @return The bodyColors
	 */
	private int[] getBodyColors() {
		return bodyColors;
	}

	/**
	 * @param bodyColors The bodyColors to set
	 */
	public void setBodyColors(int[] bodyColors) {
		this.bodyColors = bodyColors;
	}
}