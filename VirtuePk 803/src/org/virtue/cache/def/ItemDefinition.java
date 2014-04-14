package org.virtue.cache.def;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import org.virtue.Launcher;
import org.virtue.cache.Cache;
import org.virtue.cache.Container;
import org.virtue.game.content.skills.Skill;
import org.virtue.network.protocol.packet.RS3PacketReader;

/**
 * @author Virtue Development Team 2014 (c).
 * @since Apr 13, 2014
 */
public class ItemDefinition {
	
	public static final int STAFF_WEAPON_TYPE = 7;
	public static final int BOW_WEAPON_TYPE = 8;
	public static final int CROSSBOW_WEAPON_TYPE = 9;
	public static final int THROWN_WEAPON_TYPE = 10;

    public static final int ITEM_DEFINITIONS_INDEX = 19;
    
	private static ItemDefinition[] itemDefinitions;
	
    private final int itemID;
    private String name = "null";
	private HashMap<Skill, Integer> itemRequiriments;
	
	//Model size information
    public int modelZoom = 2000;
    public int modelRotation1;
    public int modelRotation2;
    public int modelOffset1;
    public int modelOffset2;
	
    //Extra information
    public int stackable;
    public int value = 1;
    public boolean membersOnly = false;
    public boolean unnoted;
    public int teamID;
    HashMap<Integer, Object> params;
    
	//Wearing model information
    public int equipID;
    public int equipSlotID;
    int maleEquip1 = -1;
    int maleEquip2 = -1;
    int femaleEquip1 = -1;
    int femaleEquip2 = -1;
    int colourEquip1 = -1;
    int colourEquip2 = -1;

    //Transformed object information
    public int noteID = -1;
    public int noteTemplateID = -1;
    int lendID = -1;
    public int lendTemplateID = -1;
    public int anInt7923 = -1;
    public int anInt7904 = -1;
    int bindID = -1;
    public int bindTemplateID = -1;
    
    //Model information
    short[] originalModelColors;
    short[] modifiedModelColors;
    short[] modifiedTextureColors;
    short[] originalTextureColors;
    byte[] aByteArray7928;
    byte[] aByteArray7895;
    public int[] anIntArray7949;
    
    //Options
    public String[] groundOptions = new String[] { null, null, "take", null, null };
    public String[] inventoryOptions = new String[] { null, null, null, null, "drop" };    
	
	int anInt7879;
    /*public static final int anInt7880 = 1;
    public static final int anInt7881 = 2;
    static final int anInt7882 = 6;*/
    byte[] aByteArray7883;
    //public static short[] aShortArray7884 = new short[256];
    //Class618 aClass618_7885;
    public int anInt7887 = 182938293;
    int[] stackAmounts;
    int interfaceModelID;
    int[] stackIds;
    public int anInt7900;
    String aString7902;
    int[] anIntArray7909;
    public int anInt7910;
    int anInt7912;
    public int anInt7913;
    int[] anIntArray7914;
    int anInt7921;
    int anInt7924;
    int anInt7925;
    int anInt7926;
    int anInt7927;
    int anInt7929;
    int anInt7931;
    int anInt7932;
    //static final int anInt7933 = 5;
    public int anInt7936;
    int anInt7937;
    public int anInt7939;
    int anInt7940;
    int anInt7942;
    //public static final int anInt7943 = 0;
    int anInt7944;
    public int anInt7947;
    public int anInt7950;
    public boolean aBool7953;
    int anInt7954;
    public boolean aBool7955;
    
    

	public static ItemDefinition forId(int id) throws IOException {
		if (itemDefinitions == null) {
			itemDefinitions = new ItemDefinition[getSize()];
		}
		if (id < 0 || id > itemDefinitions.length) {
			id = 0;
		}
		ItemDefinition itemDef = itemDefinitions[id];
		if (itemDef == null) {
			itemDefinitions[id] = itemDef = new ItemDefinition(id);
		}
		return itemDef;
	}

	public static ItemDefinition forName(String name) throws IOException {
		if (itemDefinitions == null) {
			itemDefinitions = new ItemDefinition[getSize()];
		}
		for (int id = 0; id < itemDefinitions.length; id++) {
			ItemDefinition itemDef = forId(id);
			if (itemDef == null) {
				continue;
			}
			String itemName = itemDef.getName().toLowerCase();
			if (name.equalsIgnoreCase(itemName)) {
				return itemDef;
			}
		}
		return null;
	}

	public static int getSize() throws IOException {
		int lastArchiveId = Launcher.getCache().getFileCount(ITEM_DEFINITIONS_INDEX);//Cache.getStore().getIndexes()[19].getLastArchiveId();
		return (lastArchiveId * 256 + Launcher.getCache().getContainerCount(ITEM_DEFINITIONS_INDEX, lastArchiveId));//Cache.getStore().getIndexes()[19].getValidFilesCount(lastArchiveId));
	}
    
    public ItemDefinition (int id) {
    	this.itemID = id;
    	loadItemDefinitions();
    }

	public void loadItemDefinitions() {
		try {
			byte[] data = Launcher.getCache().read(ITEM_DEFINITIONS_INDEX, getArchiveId(), getFileId()).array();//Cache.getStore().getIndexes()[19].getFile(getArchiveId(), getFileId());
			if (data == null) {
				return;
			}
			read(new RS3PacketReader(data));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
    
    void read(RS3PacketReader buffer) {
		for (;;) {
		    int opcode = buffer.getUnsignedByte();
		    if (opcode == 0) {
		    	break;
			}
		    decode(buffer, opcode);
		}
    }
    
    void decode(RS3PacketReader buffer, int opcode) {
		if (opcode == 1) {
		    interfaceModelID = buffer.getLargeSmart();
		} else if (2 == opcode) {
		    name = buffer.getString();
		} else if (4 == opcode) {
		    modelZoom = buffer.getUnsignedShort();
		} else if (5 == opcode) {
		    modelRotation1 = buffer.getUnsignedShort();
	    } else if (opcode == 6) {
		    modelRotation2 = buffer.getUnsignedShort();
	    } else if (opcode == 7) {
		    modelOffset1 = buffer.getUnsignedShort();
		    if (modelOffset1 > 32767) {
		    	modelOffset1 -= 65536;
			}
		} else if (8 == opcode) {
		    modelOffset2 = buffer.getUnsignedShort();
		    if (modelOffset2 > 32767) {
		    	modelOffset2 -= 65536;
			}
		} else if (opcode == 11) {
		    stackable = 1;
		} else if (opcode == 12) {
		    value = buffer.getInt();
		} else if (13 == opcode) {
		    equipSlotID = buffer.getUnsignedByte();
		} else if (opcode == 14) {
		    equipID = buffer.getUnsignedByte();
		} else if (opcode == 16) {
		    membersOnly = true;
		} else if (18 == opcode) {//Unknown
		    anInt7936 = buffer.getUnsignedShort();
		} else if (23 == opcode) {
		    maleEquip1 = buffer.getLargeSmart();
		} else if (opcode == 24) {
		    maleEquip2 = buffer.getLargeSmart();
		} else if (opcode == 25) {
		    femaleEquip1 = buffer.getLargeSmart();
		} else if (26 == opcode) {
		    femaleEquip2 = buffer.getLargeSmart();
		} else if (27 == opcode) {//Unknown
		    anInt7913 = buffer.getUnsignedByte();
		} else if (opcode >= 30 && opcode < 35) {
		    groundOptions[opcode - 30] = buffer.getString();
		} else if (opcode >= 35 && opcode < 40) {
		    inventoryOptions[opcode - 35] = buffer.getString();
		} else if (opcode == 40) {
		    int length = buffer.getUnsignedByte();
		    originalModelColors = new short[length];
		    modifiedModelColors = new short[length];
		    for (int index = 0; index < length; index++) {
		    	originalModelColors[index] = (short) buffer.getUnsignedShort();
		    	modifiedModelColors[index] = (short) buffer.getUnsignedShort();
		    }
		} else if (opcode == 41) {
		    int length = buffer.getUnsignedByte();
		    originalTextureColors = new short[length];
		    modifiedTextureColors = new short[length];
		    for (int index = 0; index < length; index++) {
				originalTextureColors[index] = (short) buffer.getUnsignedShort();
				modifiedTextureColors[index] = (short) buffer.getUnsignedShort();
		    }
		} else if (opcode == 42) {
		    int length = buffer.getUnsignedByte();
		    aByteArray7928 = new byte[length];//Unknown
		    for (int index = 0; index < length; index++) {
		    	aByteArray7928[index] = (byte) buffer.get();
		    }
		} else if (opcode == 43) {//Unknown
		    anInt7910 = buffer.getInt();
		    aBool7953 = true;
		} else if (44 == opcode) {//Unknown
		    int length = buffer.getUnsignedShort();
		    int arraySize = 0;
		    for (int i_252_ = length; i_252_ > 0; i_252_ >>= 1) {
		    	arraySize++;
		    }
		    aByteArray7895 = new byte[arraySize];
		    byte offset = 0;
		    for (int index = 0; index < arraySize; index++) {
				if ((length & 1 << index) > 0) {
				    aByteArray7895[index] = offset;
				    offset++;
				} else {
				    aByteArray7895[index] = (byte) -1;
				}
		    }
		} else if (opcode == 45) {//Unknown
		    int length = buffer.getUnsignedShort();
		    int arraySize = 0;
		    for (int i_257_ = length; i_257_ > 0; i_257_ >>= 1) {
		    	arraySize++;
		    }
		    aByteArray7883 = new byte[arraySize];
		    byte offset = 0;
		    for (int index = 0; index < arraySize; index++) {
				if ((length & 1 << index) > 0) {
				    aByteArray7883[index] = offset;
				    offset++;
				} else {
				    aByteArray7883[index] = (byte) -1;
				}
		    }
		} else if (65 == opcode) {
		    unnoted = true;
		} else if (78 == opcode) {
		    colourEquip1 = buffer.getLargeSmart();
		} else if (79 == opcode) {
		    colourEquip2 = buffer.getLargeSmart();
		} else if (opcode == 90) {//Unknown
		    anInt7926 = buffer.getLargeSmart();
		} else if (91 == opcode) {//Unknown
		    anInt7932 = buffer.getLargeSmart();
		} else if (opcode == 92) {//Unknown
		    anInt7927 = buffer.getLargeSmart();
		} else if (opcode == 93) {//Unknown
		    anInt7929 = buffer.getLargeSmart();
		} else if (opcode == 94) {//Unknown
		    anInt7887 = buffer.getUnsignedShort();
		} else if (95 == opcode) {//Unknown
		    anInt7900 = buffer.getUnsignedShort();
		} else if (opcode == 96) {//Unknown
		    anInt7947 = buffer.getUnsignedByte();
		} else if (97 == opcode) {
		    noteID = buffer.getUnsignedShort();
		} else if (98 == opcode) {
		    noteTemplateID = buffer.getUnsignedShort();
		} else if (opcode >= 100 && opcode < 110) {
		    if (stackIds == null) {
				stackIds = new int[10];
				stackAmounts = new int[10];
		    }
		    stackIds[opcode - 100] = buffer.getUnsignedShort();
		    stackAmounts[opcode - 100] = buffer.getUnsignedShort();
		} else if (110 == opcode) {//Unknown
		    anInt7931 = buffer.getUnsignedShort();
		} else if (111 == opcode) {//Unknown
		    anInt7912 = buffer.getUnsignedShort();
		} else if (112 == opcode) {//Unknown
		    anInt7942 = buffer.getUnsignedShort();
		} else if (opcode == 113) {//Unknown
		    anInt7879 = buffer.get();
		} else if (opcode == 114) {//Unknown
		    anInt7944 = buffer.get();
		} else if (115 == opcode) {
		    teamID = buffer.getUnsignedByte();
		} else if (121 == opcode) {
		    lendID = buffer.getUnsignedShort();
		} else if (122 == opcode) {
		    lendTemplateID = buffer.getUnsignedShort();
		} else if (125 == opcode) {//Unknown
		    anInt7940 = (buffer.get() << 2);
		    anInt7937 = (buffer.get() << 2);
		    anInt7924 = (buffer.get() << 2);
		} else if (opcode == 126) {//Unknown
		    anInt7921 = (buffer.get() << 2);
		    anInt7954 = (buffer.get() << 2);
		    anInt7925 = (buffer.get() << 2);
		} else if (127 == opcode || opcode == 128 || 129 == opcode || 130 == opcode) {//Unknown
		    buffer.getUnsignedByte();
		    buffer.getUnsignedShort();
		} else if (opcode == 132) {//Unknown
		    int length = buffer.getUnsignedByte();
		    anIntArray7949 = new int[length];
		    for (int index = 0; index < length; index++) {
		    	anIntArray7949[index] = buffer.getUnsignedShort();
		    }
		} else if (opcode == 134) {//Unknown
		    anInt7950 = buffer.getUnsignedByte();
		} else if (139 == opcode) {
		    bindID = buffer.getUnsignedShort();
		} else if (opcode == 140) {
		    bindTemplateID = buffer.getUnsignedShort();
		} else if (opcode >= 142 && opcode < 147) {
		    if (null == anIntArray7909) {
				anIntArray7909 = new int[6];
				Arrays.fill(anIntArray7909, -1);
		    }
		    anIntArray7909[opcode - 142] = buffer.getUnsignedShort();
		} else if (opcode >= 150 && opcode < 155) {
		    if (null == anIntArray7914) {
				anIntArray7914 = new int[5];
				Arrays.fill(anIntArray7914, -1);
		    }
		    anIntArray7914[opcode - 150] = buffer.getUnsignedShort();
		} else if (opcode != 156) {
		    if (157 == opcode) {
		    	aBool7955 = true;
		    } else if (161 == opcode) {
		    	anInt7904 = buffer.getUnsignedShort();
		    } else if (162 == opcode) {
		    	anInt7923 = buffer.getUnsignedShort();
		    } else if (163 == opcode) {
		    	anInt7939 = buffer.getUnsignedShort();
		    } else if (164 == opcode) {
		    	aString7902 = buffer.getString();
		    } else if (opcode == 165) {
		    	stackable = 2;
		    } else if (249 == opcode) {
				int length = buffer.getUnsignedByte();
				if (params == null) {
					params = new HashMap<Integer, Object>(length);
				}
				for (int index = 0; index < length; index++) {
				    boolean stringInstance = buffer.getUnsignedByte() == 1;
				    int key = buffer.get24BitInt();
				    Object value;
				    if (stringInstance) {
				    	value = buffer.getString();
				    } else {
				    	value = new Integer(buffer.getInt());
				    }
				    params.put(key, value);
				}
		    }
		}
    }

	public int getArchiveId() {
		return itemID >>> 8;
	}

	public int getFileId() {
		return 0xFF & itemID;
	}
	
	public String getName () {
		return name;
	}
	
	public int getID () {
		return itemID;
	}

	public boolean hasOption(String option) {
		if (inventoryOptions == null) {
			return false;
		}

		for (String opt : inventoryOptions) {
			if (opt == null || opt.equalsIgnoreCase("null")) {
				continue;
			}

			if (opt.toLowerCase().equalsIgnoreCase(option.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	public boolean isWearItem() {
		return equipSlotID != -1;
	}

	public int getNotedId() {
		return noteID;
	}

	public int getValue() {
		return value;
	}

	public HashMap<Skill, Integer> getWearingSkillRequiriments() {
		if (params == null) {
			return null;
		}
		if (itemRequiriments == null) {
			HashMap<Skill, Integer> skills = new HashMap<Skill, Integer>();
			for (int i = 0; i < 10; i++) {
				Skill skill = Skill.getSkill((Integer) params.get(749 + (i * 2)));
				if (skill != null) {
					Integer level = (Integer) params.get(750 + (i * 2));
					if (level != null) {
						skills.put(skill, level);
					}
				}
			}
			Skill maxedSkill = Skill.getSkill((Integer) params.get(277));
			if (maxedSkill != null) {
				skills.put(maxedSkill, maxedSkill.getMaxLevel());//getID() == 19709 ? 120 : 99);
			}
			itemRequiriments = skills;
			if (getID() == 7462) {
				itemRequiriments.put(Skill.DEFENCE, 40);
			} else if (name.equals("Dragon defender")) {
				itemRequiriments.put(Skill.ATTACK, 60);
				itemRequiriments.put(Skill.DEFENCE, 60);
			}
		}

		return itemRequiriments;
	}

	/**
	 * Gets the combat style.
	 * 
	 * @return The style.
	 */
	/*public CombatStyle getCombatStyle() {
		if (params == null) {
			return null;
		}
		if (params.containsKey(2821) || params.containsKey(2825)) {
			return CombatStyle.MELEE;
		} else if (params.containsKey(2822) || params.containsKey(2826)) {
			return CombatStyle.RANGE;
		} else if (params.containsKey(2823) || params.containsKey(2827)) {
			return CombatStyle.MAGIC;
		}
		System.out.println(params);
		return null;
	}*/

	/**
	 * Checks if the item is a hybrid of combat types.
	 * 
	 * @return {@code True} if so.
	 */
	public boolean isHybridType() {
		return params != null && (params.containsKey(2824) || params.containsKey(2828));
	}
	


	public boolean isStackable() {
		/*if (isNoted()) {//TODO: Fix this.
			return true;
		}*/
		if (stackable == 1) {
			return true;
		}
		if (itemID == 9075 || itemID == 15243 || itemID >= 554 && itemID <= 566 || itemID >= 863 && itemID <= 869) {
			return true;
		}
		return false;
	}

	public double getArmourBonus() {
		int opcode = 2870;
		if (params == null || !params.containsKey(opcode)) {
			return 0;
		}
		int bonus = (int) params.get(opcode);
		if (bonus != 0) {
			return bonus * .1;
		}
		return 0.0;
	}

	/*public double getDamageBonus(CombatStyle style) {
		int opcode = 641 + style.ordinal();
		if (params == null || !params.containsKey(opcode)) {
			return 0;
		}
		int bonus = (int) params.get(opcode);
		if (bonus != 0) {
			return bonus * .1;
		}
		return 0.0;
	}

	public double getAccuracyBonus(CombatStyle s) {
		int opcode = 3267 + s.ordinal();
		if (params == null || !params.containsKey(opcode)) {
			return 0;
		}
		int bonus = (int) params.get(opcode);
		if (bonus != 0) {
			return bonus * .1;
		}
		return 0.0;
	}

	public double getCriticalBonus(CombatStyle style) {
		int opcode = 2833 + style.ordinal();
		if (params == null || !params.containsKey(opcode)) {
			return 0;
		}
		int bonus = (int) params.get(opcode);
		if (bonus != 0) {
			return bonus * .1;
		}
		return 0.0;
	}*/

	public int getLifeBonus() {
		int opcode = 1326;
		if (params == null || !params.containsKey(opcode)) {
			return 0;
		}
		return (int) params.get(opcode);
	}

	public int getPrayerBonus() {
		int opcode = 2946;
		if (params == null || !params.containsKey(opcode)) {
			return 0;
		}
		return (int) params.get(opcode);
	}

	public int getProjectileId() {
		int opcode = 2940;
		if (params == null || !params.containsKey(opcode)) {
			return -1;
		}
		return (int) params.get(opcode);
	}

	public int getQuestId() {
		if (params == null) {
			return -1;
		}
		Object questId = params.get(861);
		if (questId != null && questId instanceof Integer) {
			return (Integer) questId;
		}
		return -1;
	}

	public HashMap<Integer, Object> getClientScriptData() {
		return (HashMap<Integer, Object>) params;
	}

	public int getHealAmount(int hp) {
		if (params.get(2645) == null) {
			return -1;
		}
		if (params.get(2951) == null) {
			return -1;
		}
		int cookingRequirement = (int) params.get(2951);
		return hp <= 12 || cookingRequirement == 1 ? 200 : hp >= cookingRequirement ? cookingRequirement * 20 : 16 * hp;
	}

	public int getSpeed() {
		if (params == null) {
			return 6;
		}
		return (int) params.get(14);
	}
}
