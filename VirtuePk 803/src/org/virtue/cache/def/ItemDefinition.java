package org.virtue.cache.def;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;

import org.virtue.game.logic.content.skills.Skill;
import org.virtue.game.logic.node.entity.player.container.EquipSlot;
import org.virtue.game.logic.node.entity.update.ref.Appearance;
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
    
	private final int itemID;
    private String name = "null";
	private HashMap<Skill, Integer> itemRequirements;
	
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
    public boolean stockmarket;
    public int teamID;
    HashMap<Integer, Object> paramaters;
    
	//Wearing model information
    private int equipTypeID = -1;
    private int equipSlotID = -1;
    public int maleEquip1 = -1;
    public int maleEquip2 = -1;
    public int femaleEquip1 = -1;
    public int femaleEquip2 = -1;
    public int colourEquip1 = -1;
    public int colourEquip2 = -1;

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
    byte[] recol_d_palette;
    byte[] aByteArray7895;
    public int[] anIntArray7949;
    
    //Options
    private String[] groundOptions = new String[] { null, null, "take", null, null };
    private String[] inventoryOptions = new String[] { null, null, null, null, "drop" };    
	
	int ambient;
    public static final int SHOWCOUNT_IFNOT1 = 1;
    public static final int SHOWCOUNT_ALWAYS = 2;
    static final int MAX_OP_COUNT = 6;
    byte[] aByteArray7883;
    //public static short[] aShortArray7884 = new short[256];
    //Class618 aClass618_7885;
    public int anInt7887 = 182938293;
    int[] stackAmounts;
    int interfaceModelID;
    int[] stackIds;
    public int zan2d;
    String aString7902;
    int[] anIntArray7909;
    public int anInt7910;
    int resizeY;
    public int anInt7913;
    int[] anIntArray7914;
    int womanWearXoff;
    int manWearZoff;
    int womanWearZoff;
    int manhead;
    int manhead2;
    int womanhead2;
    int resizeX;
    int womanhead;
    static final int MAX_IOP_COUNT = 5;
    public int multistacksize;
    int manWearYoff;
    public int anInt7939;
    int manWearXoff;
    int resizeZ;
    public static final int SHOWCOUNT_NEVER = 0;
    int contrast;
    public int dummyitem;
    public int pickSizeShift;
    public boolean aBool7953;
    int womanWearYoff;
    public boolean aBool7955;
    
    public boolean noted, lent, bound;
    
    public ItemDefinition (int id, byte[] data) throws IOException {
    	this.itemID = id;
    	try {
			if (data == null) {
				return;
			}
			read(new RS3PacketReader(data));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
    	//loadItemDefinition(data);
    	
		if (noteTemplateID != -1) {
			toNote();
		}
		if (lendTemplateID != -1) {
			toLend();
		}
		if (bindTemplateID != -1) {
			toBind();
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
		    stackable = SHOWCOUNT_IFNOT1;//1
		} else if (opcode == 12) {
		    value = buffer.getInt();
		} else if (13 == opcode) {
		    equipSlotID = buffer.getUnsignedByte();
		} else if (opcode == 14) {
		    equipTypeID = buffer.getUnsignedByte();
		} else if (opcode == 16) {
		    membersOnly = true;
		} else if (18 == opcode) {
		    multistacksize = buffer.getUnsignedShort();
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
		    recol_d_palette = new byte[length];
		    for (int index = 0; index < length; index++) {
		    	recol_d_palette[index] = (byte) buffer.get();
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
		    stockmarket = true;
		} else if (78 == opcode) {
		    colourEquip1 = buffer.getLargeSmart();
		} else if (79 == opcode) {
		    colourEquip2 = buffer.getLargeSmart();
		} else if (opcode == 90) {
		    manhead = buffer.getLargeSmart();
		} else if (91 == opcode) {
		    womanhead = buffer.getLargeSmart();
		} else if (opcode == 92) {
		    manhead2 = buffer.getLargeSmart();
		} else if (opcode == 93) {
		    womanhead2 = buffer.getLargeSmart();
		} else if (opcode == 94) {//Unknown
		    anInt7887 = buffer.getUnsignedShort();
		} else if (95 == opcode) {
		    zan2d = buffer.getUnsignedShort();
		} else if (opcode == 96) {
		    dummyitem = buffer.getUnsignedByte();
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
		} else if (110 == opcode) {
		    resizeX = buffer.getUnsignedShort();
		} else if (111 == opcode) {
		    resizeY = buffer.getUnsignedShort();
		} else if (112 == opcode) {
		    resizeZ = buffer.getUnsignedShort();
		} else if (opcode == 113) {
		    ambient = buffer.get();
		} else if (opcode == 114) {
		    contrast = buffer.get();
		} else if (115 == opcode) {
		    teamID = buffer.getUnsignedByte();
		} else if (121 == opcode) {
		    lendID = buffer.getUnsignedShort();
		} else if (122 == opcode) {
		    lendTemplateID = buffer.getUnsignedShort();
		} else if (125 == opcode) {
		    manWearXoff = (buffer.get() << 2);
		    manWearYoff = (buffer.get() << 2);
		    manWearZoff = (buffer.get() << 2);
		} else if (opcode == 126) {
		    womanWearXoff = (buffer.get() << 2);
		    womanWearYoff = (buffer.get() << 2);
		    womanWearZoff = (buffer.get() << 2);
		} else if (127 == opcode || opcode == 128 || 129 == opcode || 130 == opcode) {//Unknown
		    buffer.getUnsignedByte();
		    buffer.getUnsignedShort();
		} else if (opcode == 132) {//Unknown
		    int length = buffer.getUnsignedByte();
		    anIntArray7949 = new int[length];
		    for (int index = 0; index < length; index++) {
		    	anIntArray7949[index] = buffer.getUnsignedShort();
		    }
		} else if (opcode == 134) {
		    pickSizeShift = buffer.getUnsignedByte();
		} else if (139 == opcode) {
		    bindID = buffer.getUnsignedShort();
		} else if (opcode == 140) {
		    bindTemplateID = buffer.getUnsignedShort();
		} else if (opcode >= 142 && opcode < 147) {
		    if (null == anIntArray7909) {
				anIntArray7909 = new int[MAX_OP_COUNT];//6
				Arrays.fill(anIntArray7909, -1);
		    }
		    anIntArray7909[opcode - 142] = buffer.getUnsignedShort();
		} else if (opcode >= 150 && opcode < 155) {
		    if (null == anIntArray7914) {
				anIntArray7914 = new int[MAX_IOP_COUNT];//6
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
		    	stackable = SHOWCOUNT_ALWAYS;//2
		    } else if (249 == opcode) {
				int length = buffer.getUnsignedByte();
				if (paramaters == null) {
					paramaters = new HashMap<Integer, Object>(length);
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
				    paramaters.put(key, value);
				}
		    }
		}
    }

	private void toNote() throws IOException {
		ItemDefinition realItem = ItemDefinitionLoader.forId(noteID);
		membersOnly = realItem.membersOnly;
		value = realItem.value;
		name = realItem.name;
		stackable = SHOWCOUNT_IFNOT1;//1
		noted = true;
		paramaters = realItem.paramaters;
	}

	private void toBind() throws IOException {
		ItemDefinition realItem = ItemDefinitionLoader.forId(bindID);
		originalModelColors = realItem.originalModelColors;
		colourEquip1 = realItem.colourEquip1;
		colourEquip2 = realItem.colourEquip2;
		teamID = realItem.teamID;
		value = 0;
		membersOnly = realItem.membersOnly;
		name = realItem.name;
		inventoryOptions = new String[MAX_IOP_COUNT];//5
		groundOptions = realItem.groundOptions;
		if (realItem.inventoryOptions != null) {
			for (int optionIndex = 0; optionIndex < 4; optionIndex++) {
				inventoryOptions[optionIndex] = realItem.inventoryOptions[optionIndex];
			}
		}
		inventoryOptions[4] = "Discard";
		maleEquip1 = realItem.maleEquip1;
		maleEquip2 = realItem.maleEquip2;
		femaleEquip1 = realItem.femaleEquip1;
		femaleEquip2 = realItem.femaleEquip2;
		paramaters = realItem.paramaters;
		equipTypeID = realItem.equipTypeID;
		equipSlotID = realItem.equipSlotID;
		bound = true;
	}

	private void toLend() throws IOException {
		ItemDefinition realItem = ItemDefinitionLoader.forId(lendID);
		originalModelColors = realItem.originalModelColors;
		colourEquip1 = realItem.colourEquip1;
		colourEquip2 = realItem.colourEquip2;
		teamID = realItem.teamID;
		value = 0;
		membersOnly = realItem.membersOnly;
		name = realItem.name;
		inventoryOptions = new String[MAX_IOP_COUNT];//5
		groundOptions = realItem.groundOptions;
		if (realItem.inventoryOptions != null) {
			for (int optionIndex = 0; optionIndex < 4; optionIndex++) {
				inventoryOptions[optionIndex] = realItem.inventoryOptions[optionIndex];
			}
		}
		inventoryOptions[4] = "Discard";
		maleEquip1 = realItem.maleEquip1;
		maleEquip2 = realItem.maleEquip2;
		femaleEquip1 = realItem.femaleEquip1;
		femaleEquip2 = realItem.femaleEquip2;
		paramaters = realItem.paramaters;
		equipTypeID = realItem.equipTypeID;
		equipSlotID = realItem.equipSlotID;
		lent = true;
	}
	
	public String getName () {
		return name;
	}
	
	public int getID () {
		return itemID;
	}
	
	public String getOption (boolean ground, int id) {
		if (id <= 0 || id > 5) {
			return null;
		}
		if (ground) {
			return groundOptions[id-1];
		} else {
			return inventoryOptions[id-1];
		}
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

	public boolean isDestroyItem() {
		if (inventoryOptions == null)
			return false;
		for (String option : inventoryOptions) {
			if (option == null) {
				continue;
			}
			if (option.equalsIgnoreCase("destroy")) {
				return true;
			}
		}
		return false;
	}

	public boolean isWearItem(boolean male) {
		//System.out.println("maleEquip1="+maleEquip1+", femaleEquip1="+femaleEquip1+", equipSlotID="+equipSlotID);
		if (equipSlotID < EquipSlot.RING.getSlotID() && 
				(male ? maleEquip1 == -1 : femaleEquip1 == -1)) {
			//System.out.println("Not equiptable");
			return false;
		}
		return equipSlotID != -1;
	}
	
	public boolean showBeard () {
		return equipTypeID != 8 || (name.contains("helm") && !name.contains("full"));
	}
        
	public int getEquiptSlotID () {
		return equipSlotID;
	}
    
	public int getEquiptTypeID () {
		return equipTypeID;
	}

	public int getNotedID() {
		return noteID;
	}

	public int getValue() {
		return value;
	}	

	public boolean isNoted() {
		return noted;
	}

	public boolean isLent() {
		return lent;
	}

	public boolean isBound() {
		return bound;
	}

	public HashMap<Skill, Integer> getWearingSkillRequiriments() {
		if (paramaters == null) {
			return null;
		}
		if (itemRequirements == null) {
			HashMap<Skill, Integer> skills = new HashMap<Skill, Integer>();
			for (int i = 0; i < 10; i++) {
				Skill skill = Skill.getSkill((Integer) paramaters.get(749 + (i * 2)));
				if (skill != null) {
					Integer level = (Integer) paramaters.get(750 + (i * 2));
					if (level != null) {
						skills.put(skill, level);
					}
				}
			}
			Skill maxedSkill = Skill.getSkill((Integer) paramaters.get(277));
			if (maxedSkill != null) {
				skills.put(maxedSkill, maxedSkill.getMaxLevel());//getID() == 19709 ? 120 : 99);
			}
			itemRequirements = skills;
			if (getID() == 7462) {
				itemRequirements.put(Skill.DEFENCE, 40);
			} else if (name.equals("Dragon defender")) {
				itemRequirements.put(Skill.ATTACK, 60);
				itemRequirements.put(Skill.DEFENCE, 60);
			}
		}

		return itemRequirements;
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
		return paramaters != null && (paramaters.containsKey(2824) || paramaters.containsKey(2828));
	}
	


	public boolean isStackable() {
		if (isNoted()) {
			return true;
		}
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
		if (paramaters == null || !paramaters.containsKey(opcode)) {
			return 0;
		}
		int bonus = (int) paramaters.get(opcode);
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
		if (paramaters == null || !paramaters.containsKey(opcode)) {
			return 0;
		}
		return (int) paramaters.get(opcode);
	}

	public int getPrayerBonus() {
		int opcode = 2946;
		if (paramaters == null || !paramaters.containsKey(opcode)) {
			return 0;
		}
		return (int) paramaters.get(opcode);
	}

	public int getProjectileId() {
		int opcode = 2940;
		if (paramaters == null || !paramaters.containsKey(opcode)) {
			return -1;
		}
		return (int) paramaters.get(opcode);
	}

	public int getQuestId() {
		if (paramaters == null) {
			return -1;
		}
		Object questId = paramaters.get(861);
		if (questId != null && questId instanceof Integer) {
			return (Integer) questId;
		}
		return -1;
	}

	public HashMap<Integer, Object> getClientScriptData() {
		return (HashMap<Integer, Object>) paramaters;
	}

	public int getHealAmount(int hp) {
		if (paramaters.get(2645) == null) {
			return -1;
		}
		if (paramaters.get(2951) == null) {
			return -1;
		}
		int cookingRequirement = (int) paramaters.get(2951);
		return hp <= 12 || cookingRequirement == 1 ? 200 : hp >= cookingRequirement ? cookingRequirement * 20 : 16 * hp;
	}

	public int getSpeed() {
		if (paramaters == null) {
			return 6;
		}
		return (int) paramaters.get(14);
	}

	public int getRenderAnimID() {
		if (paramaters == null) {
			return Appearance.DEFAULT_RENDER_EMOTE;
		}
		Object animId = paramaters.get(644);
		if (animId != null && animId instanceof Integer) {
			return (Integer) animId;
		}
		return Appearance.DEFAULT_RENDER_EMOTE;
	}
	
	public void printFields() throws IllegalArgumentException, IllegalAccessException, IOException {
		File directory = new File("./dumps/items/"+((itemID/1000)*1000)+"/");
		directory.mkdirs();
		File file = new File(directory, itemID+"-"+name.replace("/", " ")+".txt");
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		for (Field field : this.getClass().getDeclaredFields()) {
			if (field == null) {
				continue;
			}
			Object value = field.get(this);
			if (value == null) {
				continue;
			}

			if (value instanceof String[]) {
				value = Arrays.toString((String[]) value);
			}
			if (value instanceof int[]) {
				value = Arrays.toString((int[]) value);
			}
			if (value instanceof byte[]) {
				value = Arrays.toString((byte[]) value);
			}
			if (value instanceof short[]) {
				value = Arrays.toString((short[]) value);
			}

			//System.out.println(field.getName() + "->" + value);

			//writer.write("");
			//writer.write("=================================");
			//writer.write("");
			writer.write(field.getName() + "->" + value);
			//writer.write("");
			//writer.write("=================================");
			//writer.write("");
			writer.newLine();
			writer.flush();

		}
		writer.close();
	}
	
}