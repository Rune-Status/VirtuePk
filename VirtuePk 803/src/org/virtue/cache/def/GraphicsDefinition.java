package org.virtue.cache.def;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;

import org.virtue.network.protocol.packet.RS3PacketReader;

/**
 * @author Virtue Development Team 2014 (c).
 * @since Apr 19, 2014
 */
public class GraphicsDefinition {

	int anInt6261;
    private int graphicsID;
    int defaultModel;//anInt6264
    short[] aShortArray6265;
    short[] aShortArray6266;
    public int anInt6267 = -1;
    short[] aShortArray6268;
    short[] aShortArray6269;
    byte[] aByteArray6270;
    byte[] aByteArray6271;
    int anInt6272 = 128;
    byte aByte6273;
    int anInt6274 = 128;
    int anInt6275;
    //static final int anInt6276 = 8224;
    public boolean aBool6277;
    int anInt6278 = 0;
    int anInt6279 = -1;
    
    public GraphicsDefinition (int id, byte[] data) {
        this.graphicsID = id;
		try {
			read(new RS3PacketReader(data));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
    }
    
    void read(RS3PacketReader buffer) {
		for (;;) {
		    int opcode = buffer.getUnsignedByte();
		    if (0 == opcode) {
		    	break;
		    }
		    decode(buffer, opcode);
		}
    }
    
    private void decode(RS3PacketReader buffer, int opcode) {
		do {
		    if (opcode == 1) {
		    	defaultModel = buffer.getBigSmart();
		    } else if (2 == opcode) {
		    	anInt6267 = buffer.getBigSmart();
		    } else if (4 == opcode) {
		    	anInt6272 = buffer.getUnsignedShort();
		    } else if (opcode == 5) {
		    	anInt6274 = buffer.getUnsignedShort();
		    } else if (6 == opcode) {
		    	anInt6278 = buffer.getUnsignedShort();
		    } else if (7 == opcode) {
		    	anInt6275 = buffer.getUnsignedByte();
		    } else if (opcode == 8) {
		    	anInt6261 = buffer.getUnsignedByte();
		    } else if (10 == opcode) {
		    	aBool6277 = true;
		    } else if (9 == opcode) {
		    	aByte6273 = (byte) 3;
		    	anInt6279 = 8224;
		    } else if (15 == opcode) {
		    	aByte6273 = (byte) 3;
		    	anInt6279 = buffer.getUnsignedShort();
		    } else if (opcode == 16) {
		    	aByte6273 = (byte) 3;
		    	anInt6279 = buffer.getInt();
		    } else if (40 == opcode) {
				int length = buffer.getUnsignedByte();
				aShortArray6265 = new short[length];
				aShortArray6268 = new short[length];
				for (int index = 0; index < length; index++) {
				    aShortArray6265[index] = (short) buffer.getUnsignedShort();
				    aShortArray6268[index] = (short) buffer.getUnsignedShort();
				}
		    } else if (opcode == 41) {
				int length = buffer.getUnsignedByte();
				aShortArray6266 = new short[length];
				aShortArray6269 = new short[length];
				for (int index = 0; index < length; index++) {
				    aShortArray6266[index] = (short) buffer.getUnsignedShort();
				    aShortArray6269[index] = (short) buffer.getUnsignedShort();
				}
		    } else if (opcode == 44) {
				int i_45_ = buffer.getUnsignedShort();
				int i_46_ = 0;
				for (int i_47_ = i_45_; i_47_ > 0; i_47_ >>= 1) {
					i_46_++;
				}
				aByteArray6270 = new byte[i_46_];
				byte i_48_ = 0;
				for (int i_49_ = 0; i_49_ < i_46_; i_49_++) {
				    if ((i_45_ & 1 << i_49_) > 0) {
						aByteArray6270[i_49_] = i_48_;
						i_48_++;
				    } else {
				    	aByteArray6270[i_49_] = (byte) -1;
				    }
				}
		    } else if (45 == opcode) {
				int i_50_ = buffer.getUnsignedShort();
				int i_51_ = 0;
				for (int i_52_ = i_50_; i_52_ > 0; i_52_ >>= 1) {
				    i_51_++;
				}
				aByteArray6271 = new byte[i_51_];
				byte i_53_ = 0;
				for (int i_54_ = 0; i_54_ < i_51_; i_54_++) {
				    if ((i_50_ & 1 << i_54_) > 0) {
						aByteArray6271[i_54_] = i_53_;
						i_53_++;
				    } else {
				    	aByteArray6271[i_54_] = (byte) -1;
				    }
				}
		    } else if (46 == opcode) {
		    	break;
		    }
		} while (false);
    }
	
	public int getID () {
		return graphicsID;
	}
	
	public void printFields() throws IllegalArgumentException, IllegalAccessException, IOException {
		File directory = new File("./dumps/gfx/"+((graphicsID/1000)*1000)+"/");
		directory.mkdirs();
		File file = new File(directory, graphicsID+".txt");
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

			writer.write(field.getName() + "->" + value);
			writer.newLine();
			writer.flush();

		}
		writer.close();
	}
}
