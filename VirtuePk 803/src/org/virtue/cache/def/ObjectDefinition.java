package org.virtue.cache.def;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;

import org.virtue.game.logic.node.object.ObjectOption;
import org.virtue.network.protocol.packet.RS3PacketReader;

public class ObjectDefinition {


	private final int objectID;
    private String name = "null";
    private boolean membersObject;

    public String[] options = new String[] { null, null, null, null, null, "Examine" };
    HashMap<Integer, Object> parameters;

    private int sizeX = 1;
    private int sizeY = 1;
    public int clipType = 2;
    
    
	public int[] toObjectIDs;//anIntArray6827
    public static short[] aShortArray6828 = new short[256];
    byte[] aByteArray6831;
    public int[][] modelIDs;//anIntArrayArray6832
    public int[] anIntArray6833;
    short[] originalColors;//aShortArray6834
    short[] modifiedColors;//aShortArray6835
    short[] aShortArray6837;
    int anInt6838;
    byte[] aByteArray6839;
    public boolean aBool6840;
    public int anInt6841;
    public boolean aBool6842;
    byte aByte6843;
    byte aByte6844 = 0;
    public int anInt6848 = -1;
    byte aByte6849 = 0;
    int anInt6850 = 1785892085;
    public int anInt6851;
    public int anInt6852;
    public int anInt6853;
    public int anInt6854;
    public int anInt6855;
    int[] anIntArray6856;
    public int mapSpriteID;//anInt6857
    public int anInt6858;
    int anInt6859;
    int anInt6860;
    int[] anIntArray6862;
    byte[] aByteArray6863;
    public int anInt6864;
    public boolean aBool6865;
    int configFileID;//anInt6866
    public boolean aBool6867;
    public boolean aBool6868;
    public boolean aBool6869;
    public boolean aBool6870;
    int anInt6871;
    int anInt6872;
    int anInt6873;
    int anInt6874;
    public boolean aBool6875;
    int anInt6876;
    static final int anInt6877 = 6;
    int anInt6878;
    public boolean aBool6879;
    boolean clippingFlag;//aBool6880
	public boolean projectileClipped = true;
    public int anInt6881;
    public int anInt6883;
    byte aByte6884;
    int configID;//anInt6885
    public int anInt6886;
    public int anInt6887;
    public int anInt6888;
    public int anInt6889;
    public boolean aBool6890;
    public int anInt6891;
    public int[] anIntArray6892;
    int anInt6893;
    public boolean aBool6895;
    int anInt6896;
    //public Class518 aClass518_6897;
    public boolean aBool6898;
    short[] aShortArray6899;
    public int anInt6900;
    byte[] aByteArray6901;
    byte aByte6902;
    public boolean bloom;//aBool6903
    static final int anInt6904 = 127007;
    //public Class272 aClass272_6905;
    int[] anIntArray6906;

	public ObjectDefinition (int id, byte[] data) {
		this.objectID = id;
		try {
			read(new RS3PacketReader(data));
			postDecodeEvent();
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
		if (opcode == 1) {//Unknown
		    int length = buffer.getUnsignedByte();
		    aByteArray6831 = new byte[length];
		    modelIDs = new int[length][];
		    for (int index = 0; index < length; index++) {
		    	aByteArray6831[index] = (byte) buffer.get();
				int i_6_ = buffer.getUnsignedByte();
				modelIDs[index] = new int[i_6_];
				for (int i_7_ = 0; i_7_ < i_6_; i_7_++) {
				    modelIDs[index][i_7_] = buffer.getBigSmart();
				}
		    }
		} else if (opcode == 2) {
		    name = buffer.getString();
		} else if (14 == opcode) {
		    sizeX = buffer.getUnsignedByte();
		} else if (opcode == 15) {
		    sizeY = buffer.getUnsignedByte();
		} else if (opcode == 17) {
		    clipType = 0;
			projectileClipped = false;
		} else if (opcode == 18) {
			projectileClipped = false;			
		} else if (19 == opcode) {//Unknown
		    	anInt6848 = buffer.getUnsignedByte();
		    } else if (opcode == 21) {//Unknown
		    	aByte6849 = (byte) 1;
		    } else if (opcode == 22) {//Unknown
		    	aBool6842 = true;
		    } else if (23 == opcode) {//Unknown
		    	anInt6852 = 1;
		    } else if (24 == opcode) {//Unknown
				int i_8_ = buffer.getBigSmart();
				if (i_8_ != -1) {
				    anIntArray6862 = new int[] { i_8_ };
				}
		    } else if (opcode == 27) {
		    	clipType = 1;
		    } else if (28 == opcode) {//Unknown
		    	anInt6858 = (buffer.getUnsignedByte() << 2);
		    } else if (29 == opcode) {//Unknown
		    	anInt6859 = buffer.get();
		    } else if (39 == opcode) {//Unknown
		    	anInt6860 = buffer.get();
		    } else if (opcode >= 30 && opcode < 35) {
		    	options[opcode - 30] = buffer.getString();
		    } else if (40 == opcode) {//Unknown
				int length = buffer.getUnsignedByte();
				originalColors = new short[length];
				modifiedColors = new short[length];
				for (int index = 0; index < length; index++) {
				    originalColors[index] = (short) buffer.getUnsignedShort();
				    modifiedColors[index] = (short) buffer.getUnsignedShort();
				}
		    } else if (opcode == 41) {//Unknown
				int length = buffer.getUnsignedByte();
				aShortArray6837 = new short[length];
				aShortArray6899 = new short[length];
				for (int index = 0; index < length; index++) {
				    aShortArray6837[index] = (short) buffer.getUnsignedShort();
				    aShortArray6899[index] = (short) buffer.getUnsignedShort();
				}
		    } else if (42 == opcode) {//Unknown
				int length = buffer.getUnsignedByte();
				aByteArray6901 = new byte[length];
				for (int index = 0; index < length; index++) {
				    aByteArray6901[index] = (byte) buffer.get();
				}
		    } else if (44 == opcode) {//Unknown
				int i_15_ = buffer.getUnsignedShort();
				int i_16_ = 0;
				for (int i_17_ = i_15_; i_17_ > 0; i_17_ >>= 1) {
				    i_16_++;
				}
				aByteArray6839 = new byte[i_16_];
				byte i_18_ = 0;
				for (int i_19_ = 0; i_19_ < i_16_; i_19_++) {
				    if ((i_15_ & 1 << i_19_) > 0) {
						aByteArray6839[i_19_] = i_18_;
						i_18_++;
				    } else {
				    	aByteArray6839[i_19_] = (byte) -1;
				    }
				}
		    } else if (45 == opcode) {//Unknown
				int i_20_ = buffer.getUnsignedShort();
				int i_21_ = 0;
				for (int i_22_ = i_20_; i_22_ > 0; i_22_ >>= 1) {
				    i_21_++;
				}
				aByteArray6863 = new byte[i_21_];
				byte i_23_ = 0;
				for (int i_24_ = 0; i_24_ < i_21_; i_24_++) {
				    if ((i_20_ & 1 << i_24_) > 0) {
						aByteArray6863[i_24_] = i_23_;
						i_23_++;
				    } else {
				    	aByteArray6863[i_24_] = (byte) -1;
				    }
				}
		    } else if (opcode == 62) {//Unknown
		    	aBool6868 = true;
		    } else if (64 == opcode) {//Unknown
		    	aBool6869 = false;
		    } else if (65 == opcode) {//Unknown
		    	anInt6896 = buffer.getUnsignedShort();
		    } else if (66 == opcode) {//Unknown
		    	anInt6871 = buffer.getUnsignedShort();
		    } else if (opcode == 67) {//Unknown
		    	anInt6872 = buffer.getUnsignedShort();
		    } else if (opcode == 69) {//Unused
		    	buffer.getUnsignedByte();
		    } else if (70 == opcode) {//Unknown
		    	anInt6873 = (buffer.getShort() << 2);
		    } else if (opcode == 71) {//Unknown
		    	anInt6874 = (buffer.getShort() << 2);
		    } else if (opcode == 72) {//Unknown
		    	anInt6838 = (buffer.getShort() << 2);
		    } else if (73 == opcode) {//Unknown
		    	aBool6879 = true;
		    } else if (74 == opcode) {
		    	clippingFlag = true;
		    } else if (opcode == 75) {//Unknown
		    	anInt6881 = buffer.getUnsignedByte();
		    } else if (opcode == 77 || opcode == 92) {
				configFileID = buffer.getUnsignedShort();
				if (65535 == configFileID) {
				    configFileID = -1;
				}
				configID = buffer.getUnsignedShort();
				if (65535 == configID) {
				    configID = -1;
				}
				int obj = -1;
				if (opcode == 92) {
					obj = buffer.getBigSmart();
				}
				int length = buffer.getUnsignedByte();
				toObjectIDs = new int[2 + length];//TODO: Figure out how to handle this...
				for (int index = 0; index <= length; index++) {
				    toObjectIDs[index] = buffer.getBigSmart();
				}
				toObjectIDs[length + 1] = obj;
		    } else if (opcode == 78) {//Unknown
				anInt6886 = buffer.getUnsignedShort();
				anInt6841 = buffer.getUnsignedByte();
		    } else if (79 == opcode) {//Unknown
				anInt6887 = buffer.getUnsignedShort();
				anInt6891 = buffer.getUnsignedShort();
				anInt6841 = buffer.getUnsignedByte();
				int length = buffer.getUnsignedByte();
				anIntArray6892 = new int[length];
				for (int index = 0; index < length; index++) {
				    anIntArray6892[index] = buffer.getUnsignedShort();
				}
		    } else if (opcode == 81) {//Unknown
				aByte6849 = (byte) 2;
				anInt6850 = buffer.getUnsignedByte();
		    } else if (opcode == 82) {//Unknown
		    	aBool6890 = true;
		    } else if (opcode == 88) {//Unknown
		    	aBool6895 = false;
		    } else if (opcode == 89) {//Unknown
		    	aBool6840 = false;
		    } else if (opcode == 91) {//Unknown
		    	membersObject = true;
		    } else if (93 == opcode) {//Unknown
				aByte6849 = (byte) 3;
				anInt6850 = buffer.getUnsignedShort();
		    } else if (opcode == 94) {//Unknown
		    	aByte6849 = (byte) 4;
		    } else if (95 == opcode) {//Unknown
				aByte6849 = (byte) 5;
				anInt6850 = buffer.getShort();
		    } else if (opcode == 97) {//Unknown
		    	aBool6865 = true;
		    } else if (98 == opcode) {//Unknown
		    	aBool6898 = true;
		    } else if (opcode == 99 || 100 == opcode) {//Unused
				buffer.getUnsignedByte();
				buffer.getUnsignedShort();
		    } else if (101 == opcode) {//Unknown
		    	anInt6851 = buffer.getUnsignedByte();
		    } else if (102 == opcode) {//Unknown
		    	anInt6864 = buffer.getUnsignedShort();
		    } else if (opcode == 103) {//Unknown
		    	anInt6852 = 0;
		    } else if (opcode == 104) {//Unknown
		    	anInt6889 = buffer.getUnsignedByte();
		    } else if (opcode == 105) {//Unknown
		    	aBool6867 = true;
		    } else if (opcode == 106) {//Unknown
				int length = buffer.getUnsignedByte();
				int i_31_ = 0;
				anIntArray6862 = new int[length];
				anIntArray6856 = new int[length];
				for (int index = 0; index < length; index++) {
				    anIntArray6862[index] = buffer.getBigSmart();
				    i_31_ += anIntArray6856[index] = buffer.getUnsignedByte();
				}
				for (int index = 0; index < length; index++) {
				    anIntArray6856[index] = (anIntArray6856[index] * 65535 / i_31_);
				}
		    } else if (opcode == 107) {
		    	mapSpriteID = buffer.getUnsignedShort();
		    } else if (opcode >= 150 && opcode < 155) {
		    	options[opcode - 150] = buffer.getString();
		    } else if (160 == opcode) {//(Cursor IDs?)
				int length = buffer.getUnsignedByte();
				anIntArray6833 = new int[length];
				for (int index = 0; index < length; index++) {
				    anIntArray6833[index] = buffer.getUnsignedShort();
				}
		    } else if (opcode == 162) {//Unknown
				aByte6849 = (byte) 3;
				anInt6850 = buffer.getInt();
		    } else if (opcode == 163) {//Unknown
				aByte6884 = (byte) buffer.get();
				aByte6902 = (byte) buffer.get();
				aByte6843 = (byte) buffer.get();
				aByte6844 = (byte) buffer.get();
		    } else if (opcode == 164) {//Unknown
		    	anInt6876 = buffer.getShort();
		    } else if (165 == opcode) {//Unknown
		    	anInt6893 = buffer.getShort();
		    } else if (opcode == 166) {//Unknown
		    	anInt6878 = buffer.getShort();
		    } else if (167 == opcode) {//Unknown
		    	anInt6883 = buffer.getUnsignedShort();
		    } else if (168 != opcode && opcode != 169) {
				if (opcode == 170) {//Unknown
				    anInt6853 = buffer.getSmart();
				} else if (171 == opcode) {//Unknown
				    anInt6854 = buffer.getSmart();
				} else if (opcode == 173) {//Unknown
				    anInt6900 = buffer.getUnsignedShort();
				    anInt6855 = buffer.getUnsignedShort();
				} else if (opcode == 177) {//Unknown
				    aBool6870 = true;
				} else if (178 == opcode) {//Unknown
				    anInt6888 = buffer.getUnsignedByte();
				} else if (186 == opcode) {//Unknown
					buffer.getUnsignedByte();
				} else if (opcode == 189) {//Unknown
				    bloom = true;
				} else if (opcode >= 190 && opcode < 196) {//Unknown
				    if (null == anIntArray6906) {
						anIntArray6906 = new int[6];
						Arrays.fill(anIntArray6906, -1);
				    }
				    anIntArray6906[opcode - 190] = buffer.getUnsignedShort();
				} else if (196 == opcode) {//Unknown
					buffer.getUnsignedByte();
				} else if (opcode == 197) {//Unknown
					buffer.getUnsignedByte();
				} else if (opcode != 198 && 199 != opcode) {
				    if (200 == opcode) {//Unknown
				    	aBool6875 = true;
				    } else if (opcode == 201) {//Unknown
						buffer.getUnsignedSmart();
						buffer.getUnsignedSmart();
						buffer.getUnsignedSmart();
						buffer.getUnsignedSmart();
						buffer.getUnsignedSmart();
						buffer.getUnsignedSmart();//NOTE: These aren't the actual methods, but it doesn't matter for these purposes
				    } else if (opcode == 249) {
						int length = buffer.getUnsignedByte();
						if (parameters == null) {
						    parameters = new HashMap<Integer, Object>(length);
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
						    parameters.put(key, value);
						}
				    }
				}
		}
    }
    
    void postDecodeEvent() {
		if (-1 == anInt6848) {
		    anInt6848 = 0;
		    if (aByteArray6831 != null && aByteArray6831.length == 1
		    		/*&& (-2030829961 * Class527.aClass527_6928.anInt6931 == aByteArray6831[0])*/) {
		    	anInt6848 = 1;
		    }
		    for (int index = 0; index < 5; index++) {
				if (null != options[index]) {
				    anInt6848 = 1;
				    break;
				}
		    }
		}
		if (-1 == anInt6881) {
		    anInt6881 = (0 != clipType ? 1 : 0);
		}
		if (null != anIntArray6862 || aBool6898 || toObjectIDs != null) {
		    aBool6870 = true;
		}
    }
	
	public String getName () {
		return name;
	}
	
	public int getID () {
		return objectID;
	}
	
	public int[] getSize () {
		return new int[] { sizeX, sizeY };
	}
	
	public boolean isMembers () {
		return membersObject;
	}
	
	public String getOption(ObjectOption option) {
		if (option.getID() > 0 && option.getID() < 6) {
			return options[option.getID()-1];
		} else if (option.equals(ObjectOption.EXAMINE)) {
			return options[5];
		} else {
			return null;
		}
		
	}

	public boolean hasOption(String option) {
		if (options == null) {
			return false;
		}

		for (String opt : options) {
			if (opt == null || opt.equalsIgnoreCase("null")) {
				continue;
			}

			if (opt.toLowerCase().equalsIgnoreCase(option.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return the clipType.
	 */
	public int getClipType() {
		return clipType;
	}
	
	public boolean isClipped () {
		return clipType == 1 || aBool6879 /*|| anInt6848 != 0*/;
	}

	/**
	 * @return the projectileClipped
	 */
	public boolean isProjectileClipped() {
		return projectileClipped;//projectileClipped
	}

	public boolean containsOption(String option) {
		for (String opt : options) {
			if (opt == null)
				continue;
			if (opt.equalsIgnoreCase(option)) {
				return true;
			}
		}
		return false;
	}
	
	public void printFields() throws IllegalArgumentException, IllegalAccessException, IOException {
		File directory = new File("./dumps/objects/"+((objectID/1000)*1000)+"/");
		directory.mkdirs();
		File file = new File(directory, objectID+"-"+name.replace("/", " ")+".txt");
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		for (Field field : this.getClass().getDeclaredFields()) {
			if (field == null) {
				continue;
			}
			Object value = field.get(this);
			if (value == null) {
				continue;
			}
			if (value instanceof int[][]) {
				String[] values = new String[((int[][]) value).length];
				int i=0;
				for (int[] v : ((int[][]) value)) {
					values[i++] = Arrays.toString(v);
				}
				value = values;
			}
			
			if (value instanceof String[]) {
				value = Arrays.toString((String[]) value);
			} else if (value instanceof int[]) {
				value = Arrays.toString((int[]) value);
			} else if (value instanceof byte[]) {
				value = Arrays.toString((byte[]) value);
			} else if (value instanceof short[]) {
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
