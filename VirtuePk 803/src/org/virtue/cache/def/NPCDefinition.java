package org.virtue.cache.def;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;

import org.virtue.network.protocol.packet.RS3PacketReader;

/**
 * @author Virtue Development Team 2014 (c).
 * @since Apr 13, 2014
 */
public class NPCDefinition {
    
	private int npcID;
    private String name = "null";
    public String[] options = new String[] { null, null, null, null, null, "Examine" };
    public boolean visibleOnMap = true;
    
    public int respawnDirection = 4;
    public int[] modelIDs;
    private int size = 1;
    private int combatLevel = -1;
    public int headsIcon = 32;
    public int[] transformTo;
    public int renderEmote = -1;
    public byte walkMask = (byte) 0;
    HashMap<Integer, Object> paramaters;
    
    
    public static final int anInt5804 = 2;
    public short aShort5806;
    int[][] anIntArrayArray5807;
    public int anInt5808;
    public int anInt5809;
    public boolean aBool5810;
    byte[] aByteArray5811;
    short[] aShortArray5812;
    public short[] aShortArray5813;
    public byte[] aByteArray5814;
    public byte[] aByteArray5815;
    byte aByte5816;
    byte aByte5817;
    byte aByte5818;
    public int anInt5819;
    int[] anIntArray5821;
    public int anInt5822;
    public int anInt5825;
    int anInt5826;
    public int anInt5827;
    public boolean aBool5828;
    int anInt5830;
    public boolean aBool5831;
    int anInt5832;
    public short[] aShortArray5833;
    public int anInt5834;
    public int anInt5837;
    int anInt5838;
    public boolean aBool5839;
    public boolean aBool5840;
    public int[] anIntArray5841;
    int anInt5842;
    public short aShort5843;
    public short aShort5844;
    public byte aByte5845;
    //public Class272 aClass272_5846;
    public byte aByte5847;
    public short[] aShortArray5849;
    public int anInt5850;
    int anInt5851;
    public int anInt5852;
    public boolean aBool5853;
    byte aByte5855 = 0;
    public int anInt5856;
    public int anInt5857;
    public int anInt5858;
    public int anInt5859;
    public byte aByte5861;
    public int[] anIntArray5862;
    public byte aByte5863;
    short[] aShortArray5864;
    public boolean aBool5865;
    public int anInt5866;
    public int anInt5868;
    public int[] anIntArray5869;
    public static final int anInt5870 = 1;
    public boolean aBool5871;
    
    public NPCDefinition (int id, byte[] data) {
        this.npcID = id;
		try {
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
		    int length = buffer.getUnsignedByte();
		    modelIDs = new int[length];
		    for (int index = 0; index < length; index++) {
		    	modelIDs[index] = buffer.getBigSmart();
		    }
		} else if (opcode == 2) {
		    name = buffer.getString();
		} else if (opcode == 12) {
		    size = buffer.getUnsignedByte();
		} else if (opcode >= 30 && opcode < 35) {
		    options[opcode - 30] = buffer.getString();
		} else if (opcode == 40) {
		    int length = buffer.getUnsignedByte();
		    aShortArray5864 = new short[length];
		    aShortArray5849 = new short[length];
		    for (int index = 0; index < length; index++) {
		    	aShortArray5864[index] = (short) buffer.getUnsignedShort();
		    	aShortArray5849[index] = (short) buffer.getUnsignedShort();
		    }
		} else if (opcode == 41) {
		    int length = buffer.getUnsignedByte();
		    aShortArray5812 = new short[length];
		    aShortArray5813 = new short[length];
		    for (int index = 0; index < length; index++) {
		    	aShortArray5812[index] = (short) buffer.getUnsignedShort();
		    	aShortArray5813[index] = (short) buffer.getUnsignedShort();
		    }
		} else if (42 == opcode) {
		    int length = buffer.getUnsignedByte();
		    aByteArray5811 = new byte[length];
		    for (int index = 0; index < length; index++) {
		    	aByteArray5811[index] = (byte) buffer.get();
		    }
		} else if (opcode == 44) {
		    int length = buffer.getUnsignedShort();
		    int i_11_ = 0;
		    for (int index = length; index > 0; index >>= 1) {
		    	i_11_++;
		    }
		    aByteArray5814 = new byte[i_11_];
		    byte i_13_ = 0;
		    for (int index = 0; index < i_11_; index++) {
				if ((length & 1 << index) > 0) {
				    aByteArray5814[index] = i_13_;
				    i_13_++;
				} else {
				    aByteArray5814[index] = (byte) -1;
				}
		    }
		} else if (45 == opcode) {
		    int i_15_ = buffer.getUnsignedShort();
		    int i_16_ = 0;
		    for (int i_17_ = i_15_; i_17_ > 0; i_17_ >>= 1)
			i_16_++;
		    aByteArray5815 = new byte[i_16_];
		    byte i_18_ = 0;
		    for (int i_19_ = 0; i_19_ < i_16_; i_19_++) {
			if ((i_15_ & 1 << i_19_) > 0) {
			    aByteArray5815[i_19_] = i_18_;
			    i_18_++;
			} else
			    aByteArray5815[i_19_] = (byte) -1;
		    }
		} else if (60 == opcode) {
		    int length = buffer.getUnsignedByte();
		    anIntArray5841 = new int[length];
		    for (int index = 0; index < length; index++) {
		    	anIntArray5841[index] = buffer.getBigSmart();
		    }
		} else if (93 == opcode) {
		    visibleOnMap = false;
		} else if (95 == opcode) {
		    combatLevel = buffer.getUnsignedShort();
		} else if (97 == opcode) {
		    anInt5832 = buffer.getUnsignedShort();
		} else if (98 == opcode) {
		    anInt5826 = buffer.getUnsignedShort();
		} else if (opcode == 99) {
		    aBool5810 = true;
		} else if (opcode == 100) {
		    anInt5830 = buffer.get();
		} else if (opcode == 101) {
		    anInt5851 = buffer.get();
		} else if (102 == opcode) {
		    int i_22_ = buffer.getUnsignedByte();
		    int i_23_ = 0;
		    for (int i_24_ = i_22_; 0 != i_24_; i_24_ >>= 1)
			i_23_++;
		    anIntArray5869 = new int[i_23_];
		    aShortArray5833 = new short[i_23_];
		    for (int i_25_ = 0; i_25_ < i_23_; i_25_++) {
			if ((i_22_ & 1 << i_25_) == 0) {
			    anIntArray5869[i_25_] = -1;
			    aShortArray5833[i_25_] = (short) -1;
			} else {
			    anIntArray5869[i_25_] = buffer.getBigSmart();
			    aShortArray5833[i_25_] = (short) buffer.getSmart2();
			}
		    }
		} else if (opcode == 103) {
		    headsIcon = buffer.getUnsignedShort();
		} else if (106 == opcode || 118 == opcode) {
		    anInt5842 = buffer.getUnsignedShort();
		    if (anInt5842 == 65535) {
		    	anInt5842 = -1;
		    }
		    anInt5838 = buffer.getUnsignedShort();
		    if (anInt5838 == 65535) {
		    	anInt5838 = -1;
		    }
		    int i_26_ = -1;
		    if (opcode == 118) {
				i_26_ = buffer.getUnsignedShort();
				if (i_26_ == 65535) {
				    i_26_ = -1;
				}
		    }
		    int i_27_ = buffer.getUnsignedByte();
		    transformTo = new int[i_27_ + 2];
		    for (int i_28_ = 0; i_28_ <= i_27_; i_28_++) {
				transformTo[i_28_] = buffer.getUnsignedShort();
				if (transformTo[i_28_] == 65535) {
				    transformTo[i_28_] = -1;
				}
		    }
		    transformTo[1 + i_27_] = i_26_;
		} else if (opcode == 107) {
		    aBool5839 = false;
		} else if (109 == opcode) {
		    aBool5840 = false;
		} else if (opcode == 111) {
		    aBool5831 = false;
		} else if (opcode == 113) {
		    aShort5844 = (short) buffer.getUnsignedShort();
		    aShort5843 = (short) buffer.getUnsignedShort();
		} else if (opcode == 114) {
		    aByte5861 = (byte) buffer.get();
		    aByte5845 = (byte) buffer.get();
		} else if (119 == opcode) {
		    walkMask = (byte) buffer.get();
		} else if (opcode == 121) {
		    anIntArrayArray5807 = new int[modelIDs.length][];
		    int i_29_ = buffer.getUnsignedByte();
		    for (int i_30_ = 0; i_30_ < i_29_; i_30_++) {
				int i_31_ = buffer.getUnsignedByte();
				int[] is = (anIntArrayArray5807[i_31_] = new int[3]);
				is[0] = buffer.get();
				is[1] = buffer.get();
				is[2] = buffer.get();
		    }
		} else if (123 == opcode) {
		    anInt5859 = buffer.getUnsignedShort();
		} else if (125 == opcode) {
		    respawnDirection = buffer.get();
		} else if (opcode == 127) {
		    renderEmote = buffer.getUnsignedShort();
		} else if (128 == opcode) {
		    buffer.getUnsignedByte();
		} else if (134 == opcode) {
		    anInt5819 = buffer.getUnsignedShort();
		    if (65535 ==  anInt5819) {
		    	anInt5819 = -1;
		    }
		    anInt5852 = buffer.getUnsignedShort();
		    if (65535 == anInt5852) {
		    	anInt5852 = -1;
		    }
		    anInt5808 = buffer.getUnsignedShort();
		    if (anInt5808 == 65535) {
		    	anInt5808 = -1;
		    }
		    anInt5858 = buffer.getUnsignedShort();
		    if (anInt5858 == 65535) {
		    	anInt5858 = -1;
		    }
		    anInt5866 = buffer.getUnsignedByte();
		} else if (opcode == 135 || opcode == 136) {
		    buffer.getUnsignedByte();
		    buffer.getUnsignedShort();
		} else if (137 == opcode) {
		    anInt5822 = buffer.getUnsignedShort();
		} else if (138 == opcode) {
		    anInt5834 = buffer.getBigSmart();
		} else if (opcode == 140) {
		    anInt5857 = buffer.getUnsignedByte();
		} else if (141 == opcode) {
		    aBool5865 = true;
		} else if (opcode == 142) {
		    anInt5837 = buffer.getUnsignedShort();
		} else if (143 == opcode) {
		    aBool5828 = true;
		} else if (opcode >= 150 && opcode < 155) {
		    options[opcode - 150] = buffer.getString();
		} else if (opcode == 155) {
		    aByte5816 = (byte) buffer.get();
		    aByte5817 = (byte) buffer.get();
		    aByte5818 = (byte) buffer.get();
		    aByte5855 = (byte) buffer.get();
		} else if (158 == opcode) {
		    aByte5863 = (byte) 1;
		} else if (opcode == 159) {
		    aByte5863 = (byte) 0;
		} else if (160 == opcode) {
		    int length = buffer.getUnsignedByte();
		    anIntArray5862 = new int[length];
		    for (int index = 0; index < length; index++) {
		    	anIntArray5862[index] = buffer.getUnsignedShort();
		    }
		} else if (opcode != 162) {
		    if (163 == opcode) {
		    	anInt5827 = buffer.getUnsignedByte();
		    } else if (164 == opcode) {
				anInt5850 = buffer.getUnsignedShort();
				anInt5825 = buffer.getUnsignedShort();
		    } else if (opcode == 165) {
		    	anInt5868 = buffer.getUnsignedByte();
		    } else if (opcode == 168) {
		    	anInt5856 = buffer.getUnsignedByte();
		    } else if (opcode == 169) {
		    	aBool5853 = false;
		    } else if (opcode >= 170 && opcode < 176) {
				if (null == anIntArray5821) {
				    anIntArray5821 = new int[6];
				    Arrays.fill(anIntArray5821, -1);
				}
				int i_34_ = buffer.getUnsignedShort();
				if (i_34_ == 65535) {
				    i_34_ = -1;
				}
				anIntArray5821[opcode - 170] = i_34_;
		    } else if (opcode != 178) {
				if (179 == opcode) {
				    buffer.getSmart3();
				    buffer.getSmart3();
				    buffer.getSmart3();
				    buffer.getSmart3();
				    buffer.getSmart3();
				    buffer.getSmart3();
				} else if (opcode == 180) {
				    anInt5809 = (buffer.getUnsignedByte() & 0xff);
				} else if (opcode == 181) {
				    aShort5806 = (short) buffer.getUnsignedShort();
				    aByte5847 = (byte) buffer.getUnsignedByte();
				} else if (182 == opcode) {
				    aBool5871 = true;
				} else if (249 == opcode) {
				    int length = buffer.getUnsignedByte();
				    if (null == paramaters) {
						paramaters = new HashMap<Integer, Object>(length);
				    }
				    for (int index = 0; index < length; index++) {
				    	boolean stringInstance = buffer.getUnsignedByte() == 1;
						int key = buffer.getTriByte();
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
    }
	
	public String getName () {
		return name;
	}
	
	public int getID () {
		return npcID;
	}
        
    public int getSize () {
    	return size;
    }
    
    public int getCombatLevel () {
    	return combatLevel;
    }

	public boolean hasMarkOption() {
		for (String option : options) {
			if (option != null && option.equalsIgnoreCase("mark"))
				return true;
		}
		return false;
	}

	public boolean hasOption(String op) {
		for (String option : options) {
			if (option != null && option.equalsIgnoreCase(op))
				return true;
		}
		return false;
	}

	public boolean hasAttackOption() {
		if (npcID == 14899) {
			return true;
		}
		for (String option : options) {
			if (option != null && option.equalsIgnoreCase("attack")) {
				return true;
			}
		}
		return false;
	}
	
	public void printFields() throws IllegalArgumentException, IllegalAccessException, IOException {
		File directory = new File("./dumps/npcs/"+((npcID/1000)*1000)+"/");
		directory.mkdirs();
		File file = new File(directory, npcID+"-"+name.replace("/", " ")+".txt");
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
