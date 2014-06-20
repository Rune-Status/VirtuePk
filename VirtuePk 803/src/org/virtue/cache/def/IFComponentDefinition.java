package org.virtue.cache.def;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.virtue.network.protocol.packet.RS3PacketReader;

/**
 *
 * @author Virtue Development Team 2014 (c).
 */
public class IFComponentDefinition {
	public int anInt809;
    //public static int anInt810;
    public Object[] anObjectArray811;
    //public static int anInt812;
    //public static int anInt813;
    //public static int anInt814;
    //public static int anInt815;
    //public static int anInt816;
    //public static int anInt817;
    public int anInt818;
    //public static int anInt819 = 0;
    public int anInt820;
    public int anInt821;
    //public static int anInt822;
    //public static int anInt823;
    //static JS5 aClass210_824;
    //public static final int anInt825 = 5;
    public boolean aBool826;
    public int anInt827;
    //short[] aShortArray828;
    //static Class129 aClass129_829;
    //static Class129 aClass129_830;
    public int[] anIntArray831;
    //static int anInt832;
    //public static boolean aBool833;
    public String aString834;
    public int anInt835;
    //public InterfaceComponent[] aClass58Array836;
    public int slot1;//anInt837
    //public static final int anInt838 = 5;
    //public static int anInt839;
    public int anInt840;
    public byte locationYMode;//aByte841
    public int anInt842;
    public boolean filledBox;//aBool843
    public int drawOffsetX;//anInt844
    public int drawOffsetY;//anInt845
    short[] aShortArray846;
    public int fixedHeight;//anInt847
    public Object[] onMouseUpScript;//anObjectArray848
    public String aString849;
    public int anInt850;
    public Object[] onMouseHoverScript;//anObjectArray851
    public int anInt852;
    public int anInt853;
    public boolean aBool854;
    public boolean hidden;//aBool855
    public int anInt856;
    public int anInt857;
    public int anInt858;
    public boolean aBool859;
    public int anInt860;
    public int[] anIntArray861;
    public int scrollMaxX;//anInt862
    //public static Class129 aClass129_863;
    public int colour;//anInt864
    public Object[] onFriendsChatUpdateScript;//anObjectArray865
    public int colourAlpha;//anInt866
    public int anInt867;
    public boolean aBool868;
    public byte[][] aByteArrayArray869;
    public int anInt870;
    public boolean aBool871;
    public int anInt872;
    //public static int anInt873;
    public int componentType;//anInt874
    public boolean flipHorizontally;//aBool875
    public boolean aBool876;
    public boolean aBool877;
    public int modelType;//anInt878
    public int anInt879;
    public int anInt880;
    public int anInt881;
    public int anInt882;
    public int parentNode;//anInt883
    //public static final int anInt884 = 8;
    public int anInt885;
    public int anInt886;
    public int anInt887;
    public int anInt888;
    public int anInt889;
    public int anInt890;
    public int anInt891;
    public int anInt892;
    public boolean aBool893;
    public int anInt894;
    public int anInt895;
    public int anInt896;
    short[] aShortArray897;
    public int[] skillUpdateWatch;//anIntArray898
    short[] aShortArray899;
    //public static Class129 aClass129_900;
    public int anInt901;
    public int anInt902;
    public int spriteID;//anInt903
    public boolean aBool904;
    public int anInt905;
    public int[] anIntArray906;
    public int anInt907;
    public byte locationXMode;//aByte908
    public int slot2;//anInt909
    public int anInt910;
    //public static int anInt911;
    public Object[] anObjectArray912;
    public int fontID;//anInt913
    public int[] anIntArray914;
    public byte resizeModeY;//aByte915
    public int textLineHeight;//anInt916
    public int horizontalTextAlign;//anInt917
    public int verticalTextAlign;//anInt918
    public boolean textHasShadow;//aBool919
    public int anInt920;
    public int anInt921;
    //public Class241_Sub20 aClass241_Sub20_922;
    public boolean aBool923;
    public String[] contextMenuOptions;//aStringArray924
    public byte[][] aByteArrayArray925;
    public int[] anIntArray926;
    public Object[] onVarcstrChangeScript;//anObjectArray927
    public int anInt928;
    public Object[] anObjectArray929;
    //public static final int anInt930 = 2;
    //public static final int anInt931 = 4;
    public int componentHash = -1;//anInt932
    public String aString933;
    public boolean flipVertically;//aBool934
    public String aString935;
    public int[] customCursorIDs;//anIntArray936
    public int anInt937;
   // public InterfaceComponent aClass58_938;
    public int anInt939;
    public Object[] onMessageReceiveScript;//anObjectArray940
    public int anInt941;
    public int anInt942;
    //public static int anInt943;
    public Object[] onLoadScript;//anObjectArray944
    public Object[] anObjectArray945;
    public Object[] anObjectArray946;
    public int anInt947;
    public boolean aBool948;
    public Object[] onMouseOverScript;//anObjectArray949
    public boolean aBool950;
    public Object[] onMouseOutScript;//anObjectArray951
    public Object[] anObjectArray952;
    public Object[] anObjectArray953;
    public int anInt954;
    //public Class489 aClass489_955;
    public Object[] onVarpChangeScript;//anObjectArray956
    public int[] varpChangeWatch;//anIntArray957
    public Object[] onItemsUpdateScript;//anObjectArray958
    public int[] itemUpdateWatch;//anIntArray959
    public Object[] onSkillUpdateScript;//anObjectArray960
    public Object[] onVarcChangeScript;//anObjectArray961
    //public static int anInt962;
    public int[] varcChangeWatch;//anIntArray963
    public String text;//aString964
    public int[] varcstrChangeWatch;//anIntArray965
    public Object[] onVarClanChangeScript;//anObjectArray966
    //public static int anInt967;
    public int anInt968;
    public Object[] onContextMenuSelect;//anObjectArray969
    public Object[] onItemSwapScript;//anObjectArray970
    public Object[] anObjectArray971;
    //public static final int anInt972 = 1;
    public Object[] anObjectArray973;
    public Object[] onFriendsListUpdateScript;//anObjectArray974
    public int anInt975;
    public Object[] onClanSettingsUpdateScript;//anObjectArray976
    public Object[] anObjectArray977;
    public Object[] anObjectArray978;
    public Object[] anObjectArray979;
    public Object[] anObjectArray980;
    public Object[] anObjectArray981;
    public Object[] anObjectArray982;
    public Object[] anObjectArray983;//anObjectArray983
    //HashTable aClass407_984;
    Map<Integer, Object> aClass407_984;
    public int anInt985;
    public int anInt986;
    public String linkedFriend;//aString987
    public boolean aBool988;
    public int anInt989;
    public int anInt990;
    public int fixedWidth;//anInt991
    public int anInt992;
    public int scrollMaxY;//anInt993
    public int anInt994;
    public int anInt995;
    public Object[] anObjectArray996;
    public byte resizeModeX;//aByte997
    //public Class409 aClass409_998;
    public int scrollCurrentY;//anInt999
    //public InterfaceComponent[] aClass58Array1000;
    public boolean aBool1001;
    public int anInt1002;
    public int componentLastUpdated;//anInt1003
    public int lastVarpChanged;//anInt1004
    public int lastSkillUpdate;//anInt1005
    public int anInt1006;
    public boolean aBool1007;
    public int lastVarcstrChanged;//anInt1008
    public int lastVarClanChanged;//anInt1009
    public int anInt1010;
    public int anInt1011;
    public boolean hasClientScript;
    public int anInt1013;
    //public Class404 aClass404_1014;
    
    public IFComponentDefinition (int hash, byte[] data) throws IOException {
    	this.componentHash = hash;
    	try {
			if (data == null) {
				return;
			}
			decode(new RS3PacketReader(data));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
    }
    
    void decode(RS3PacketReader buffer) {
		int i_0_ = buffer.getUnsignedByte();
		if (i_0_ == 255)
		    i_0_ = -1;
		componentType = buffer.getUnsignedByte();
		if (0 != (componentType & 0x80)) {
		    componentType = (componentType & 0x7f);
		    aString834 = buffer.getString();
		}
		anInt1002 = buffer.getUnsignedShort();
		drawOffsetX = buffer.getShort();
		drawOffsetY = buffer.getShort();
		fixedWidth = buffer.getUnsignedShort();
		fixedHeight = buffer.getUnsignedShort();
		resizeModeX = (byte) buffer.get();
		resizeModeY = (byte) buffer.get();
		locationXMode = (byte) buffer.get();
		locationYMode = (byte) buffer.get();
		parentNode = buffer.getUnsignedShort();
		if (parentNode == 65535) {
		    parentNode = -1;//-1
		} else {
		    parentNode = ((componentHash & ~0xffff) + parentNode);
		}
		int hideFlags = buffer.getUnsignedByte();
		hidden = (hideFlags & 0x1) != 0;
		if (i_0_ >= 0) {
		    aBool859 = (hideFlags & 0x2) != 0;
		}
		if (0 == componentType) {
		    scrollMaxX = buffer.getUnsignedShort();
		    scrollMaxY = buffer.getUnsignedShort();
		    if (i_0_ < 0) {
		    	aBool859 = buffer.getUnsignedByte() == 1;
		    }
		}
		if (componentType == 5) {
		    spriteID = buffer.getInt();
		    anInt870 = buffer.getUnsignedShort();
		    int i_2_ = buffer.getUnsignedByte();
		    aBool871 = (i_2_ & 0x1) != 0;
		    aBool876 = 0 != (i_2_ & 0x2);
		    colourAlpha = buffer.getUnsignedByte();
		    anInt872 = buffer.getUnsignedByte();
		    anInt881 = buffer.getInt();
		    flipVertically = buffer.getUnsignedByte() == 1;
		    flipHorizontally = buffer.getUnsignedByte() == 1;
		    colour = buffer.getInt();
		    if (i_0_ >= 3) {
		    	aBool877 = buffer.getUnsignedByte() == 1;
		    }
		}
		if (6 == componentType) {
		    modelType = -1;
		    anInt879 = buffer.getBigSmart();
		    int i_3_ = buffer.getUnsignedByte();
		    boolean bool = 1 == (i_3_ & 0x1);
		    aBool950 = (i_3_ & 0x2) == 2;
		    aBool893 = 4 == (i_3_ & 0x4);
		    aBool826 = 8 == (i_3_ & 0x8);
		    if (bool) {
				anInt887 = buffer.getShort();
				anInt888 = buffer.getShort();
				anInt853 = buffer.getUnsignedShort();
				anInt885 = buffer.getUnsignedShort();
				anInt886 = buffer.getUnsignedShort();
				anInt890 = buffer.getUnsignedShort();
		    } else if (aBool950) {
				anInt887 = buffer.getShort();
				anInt888 = buffer.getShort();
				anInt889 = buffer.getShort();
				anInt853 = buffer.getUnsignedShort();
				anInt885 = buffer.getUnsignedShort();
				anInt886 = buffer.getUnsignedShort();
				anInt890 = buffer.getShort();
		    }
		    anInt818 = buffer.getBigSmart();
		    if (resizeModeX != 0) {
		    	anInt891 = buffer.getUnsignedShort();
		    }
		    if (resizeModeY != 0) {
		    	anInt892 = buffer.getUnsignedShort();
		    }
		}
		if (componentType == 4) {
		    fontID = buffer.getBigSmart();
		    if (i_0_ >= 2) {
		    	aBool1007 = buffer.getUnsignedByte() == 1;
		    }
		    text = buffer.getString();
		    if (text.contains("unavailable")) {
		    	System.out.println("[Component] Text="+text+", interface="+componentHash);
		    }
		    textLineHeight = buffer.getUnsignedByte();
		    horizontalTextAlign = buffer.getUnsignedByte();
		    verticalTextAlign = buffer.getUnsignedByte();
		    textHasShadow = buffer.getUnsignedByte() == 1;
		    colour = buffer.getInt();
		    colourAlpha = buffer.getUnsignedByte();
		    if (i_0_ >= 0) {
		    	anInt920 = buffer.getUnsignedByte();
		    }
		}
		if (componentType == 3) {
		    colour = buffer.getInt();
		    filledBox = buffer.getUnsignedByte() == 1;
		    colourAlpha = buffer.getUnsignedByte();
		}
		if (componentType == 9) {
		    anInt985 = buffer.getUnsignedByte();
		    colour = buffer.getInt();
		    aBool868 = buffer.getUnsignedByte() == 1;
		}
		int i_4_ = buffer.getTriByte();
		int i_5_ = buffer.getUnsignedByte();
		if (0 != i_5_) {
		    aByteArrayArray869 = new byte[11][];
		    aByteArrayArray925 = new byte[11][];
		    anIntArray926 = new int[11];
		    anIntArray861 = new int[11];
		    for (/**/; 0 != i_5_; i_5_ = buffer.getUnsignedByte()) {
				int i_6_ = (i_5_ >> 4) - 1;
				i_5_ = i_5_ << 8 | buffer.getUnsignedByte();
				i_5_ &= 0xfff;
				if (4095 == i_5_) {
				    i_5_ = -1;
				}
				byte i_7_ = (byte) buffer.get();
				if (i_7_ != 0) {
				    aBool923 = true;
				}
				byte i_8_ = (byte) buffer.get();
				anIntArray926[i_6_] = i_5_;
				aByteArrayArray869[i_6_] = new byte[] { i_7_ };
				aByteArrayArray925[i_6_] = new byte[] { i_8_ };
		    }
		}
		aString933 = buffer.getString();
		int optionFlags = buffer.getUnsignedByte();
		int contextOptCount = optionFlags & 0xf;
		int customCursorFlags = optionFlags >> 4;
		if (contextOptCount > 0) {
		    contextMenuOptions = new String[contextOptCount];
		    for (int index = 0; index < contextOptCount; index++) {
		    	contextMenuOptions[index] = buffer.getString();
		    }
		}
		if (customCursorFlags > 0) {
		    int i_13_ = buffer.getUnsignedByte();
		    customCursorIDs = new int[i_13_ + 1];
		    for (int index = 0; index < customCursorIDs.length; index++) {
		    	customCursorIDs[index] = -1;
			}
		    customCursorIDs[i_13_] = buffer.getUnsignedShort();
		}
		if (customCursorFlags > 1) {
		    int i_15_ = buffer.getUnsignedByte();
		    customCursorIDs[i_15_] = buffer.getUnsignedShort();
		}
		aString935 = buffer.getString();
		if (aString935.equals("")) {
		    aString935 = null;
		}
		anInt939 = buffer.getUnsignedByte();
		anInt921 = buffer.getUnsignedByte();
		anInt941 = buffer.getUnsignedByte();
		aString849 = buffer.getString();
		int i_16_ = -1;
		//if (Class448.method7923(i_4_, (byte) -125) != 0) {
		if ((i_4_ >> 11 & 0x7f) != 0) {
		    i_16_ = buffer.getUnsignedShort();
		    if (65535 == i_16_) {
				i_16_ = -1;
			}
		    anInt821 = buffer.getUnsignedShort();
		    if (anInt821 == 65535) {
				anInt821 = -1;//457822219 * -971652003
			}
		    anInt857 = buffer.getUnsignedShort();
		    if (65535 == anInt857) {
				anInt857 = -1;//206800565 * 412108899
			}
		}
		if (i_0_ >= 0) {
		    anInt858 = buffer.getUnsignedShort();
		    if (anInt858 == 65535) {
				anInt858 = -1;//1883881257 * -2055556377
			}
		}
		//aClass241_Sub20_922 = new Class241_Sub20(i_4_, i_16_);
		if (i_0_ >= 0) {
			aClass407_984 = new HashMap<Integer, Object>();
		    int i_17_ = buffer.getUnsignedByte();
		    for (int index = 0; index < i_17_; index++) {
				int key = buffer.getTriByte();
				int value = buffer.getInt();				
				aClass407_984.put(key, new Integer(value));
		    }
		    int i_21_ = buffer.getUnsignedByte();
		    for (int index = 0; index < i_21_; index++) {
				int key = buffer.getTriByte();
				String value = buffer.getJagString();
				aClass407_984.put(key, value);
		    }
		}
		onLoadScript = decodeScriptParams(buffer);
		onMouseOverScript = decodeScriptParams(buffer);
		onMouseOutScript = decodeScriptParams(buffer);
		anObjectArray929 = decodeScriptParams(buffer);
		anObjectArray912 = decodeScriptParams(buffer);
		onVarpChangeScript = decodeScriptParams(buffer);
		onItemsUpdateScript = decodeScriptParams(buffer);
		onSkillUpdateScript = decodeScriptParams(buffer);
		anObjectArray977 = decodeScriptParams(buffer);
		onContextMenuSelect = decodeScriptParams(buffer);
		if (i_0_ >= 0) {
		    onItemSwapScript = decodeScriptParams(buffer);
		}
		onMouseHoverScript = decodeScriptParams(buffer);
		anObjectArray945 = decodeScriptParams(buffer);
		anObjectArray946 = decodeScriptParams(buffer);
		onMouseUpScript = decodeScriptParams(buffer);
		anObjectArray996 = decodeScriptParams(buffer);
		anObjectArray952 = decodeScriptParams(buffer);
		anObjectArray953 = decodeScriptParams(buffer);
		anObjectArray971 = decodeScriptParams(buffer);
		onVarcChangeScript = decodeScriptParams(buffer);
		onVarcstrChangeScript = decodeScriptParams(buffer);
		varpChangeWatch = decodeVarWatchKeys(buffer);
		itemUpdateWatch = decodeVarWatchKeys(buffer);
		skillUpdateWatch = decodeVarWatchKeys(buffer);
		varcChangeWatch = decodeVarWatchKeys(buffer);
		varcstrChangeWatch = decodeVarWatchKeys(buffer);
    }
	
	Object[] decodeScriptParams(RS3PacketReader buffer) {
		int paramCount = buffer.getUnsignedByte();
		if (paramCount == 0) {
			return null;
		}
		Object[] objects = new Object[paramCount];
		for (int index = 0; index < paramCount; index++) {
			int type = buffer.getUnsignedByte();
			if (type == 0) {
				objects[index] = new Integer(buffer.getInt());
			} else if (1 == type) {
				objects[index] = buffer.getString();
			}
		}
		hasClientScript = true;
		return objects;
    }
	
	int[] decodeVarWatchKeys(RS3PacketReader buffer) {
		int length = buffer.getUnsignedByte();
		if (length == 0) {
			return null;
		}
		int[] is = new int[length];
		for (int index = 0; index < length; index++) {
			is[index] = buffer.getInt();
		}
		return is;
    }
	
	protected void printFields(BufferedWriter writer) throws IllegalArgumentException, IllegalAccessException, IOException {
		writer.write("hash="+componentHash);
		writer.write(", componentType="+componentType);
		writer.write(", anInt1002="+anInt1002);
		writer.write(", drawOffsetX="+drawOffsetX);
		writer.write(", drawOffsetY="+drawOffsetY);
		writer.write(", fixedWidth="+fixedWidth);
		writer.write(", fixedHeight="+fixedHeight);
		writer.write(", resizeModeX="+resizeModeX);
		writer.write(", resizeModeY="+resizeModeY);
		writer.write(", locationXMode="+locationXMode);
		writer.write(", locationYMode="+locationYMode);
		writer.write(", parentNode="+parentNode);
		writer.write(", hidden="+hidden);
		writer.write(", aBool859="+aBool859);		
		writer.newLine();
		switch (componentType) {
		case 0:
			writer.write("Component type=container(");
			writer.write("scrollMaxX="+scrollMaxX);
			writer.write(", scrollMaxY="+scrollMaxY);
			writer.write(", aBool859="+aBool859+")");
			break;
		case 5:
			writer.write("Component type=sprite(");
			writer.write("spriteID="+spriteID);
			writer.write(", anInt870="+anInt870);
            writer.write(", aBool871="+aBool871);
            writer.write(", aBool876="+aBool876);
            writer.write(", colourAlpha="+colourAlpha);
            writer.write(", anInt872="+anInt872);
            writer.write(", anInt881="+anInt881);
            writer.write(", flipVertically="+flipVertically);
            writer.write(", flipHorizontally="+flipHorizontally);
            writer.write(", colour="+colour);
            writer.write(", aBool877="+aBool877+")");
			break;
		case 6:
			writer.write("Component type=[5](");
			writer.write(", anInt879="+anInt879);
			writer.write(", aBool950="+aBool950);
			writer.write(", aBool893="+aBool893);
			writer.write(", aBool826="+aBool826);
			writer.write(", anInt887="+anInt887);
			writer.write(", anInt888="+anInt888);
			writer.write(", anInt889="+anInt889);
			writer.write(", anInt853="+anInt853);
			writer.write(", anInt885="+anInt885);
			writer.write(", anInt886="+anInt886);
			writer.write(", anInt890="+anInt890);
			writer.write(", anInt818="+anInt818);
			writer.write(", anInt891="+anInt891);
			writer.write(", anInt892="+anInt892+")");
			break;
		case 4:
			writer.write("Component type=text(");
			writer.write("fontID="+fontID);
			writer.write(", aBool1007="+aBool1007);
			writer.write(", text="+text);
			writer.write(", textLineHeight="+textLineHeight);
			writer.write(", horizontalTextAlign="+horizontalTextAlign);
			writer.write(", verticalTextAlign="+verticalTextAlign);
			writer.write(", textHasShadow="+textHasShadow);
			writer.write(", colour="+colour);
			writer.write(", colourAlpha="+colourAlpha);
			writer.write(", anInt920="+anInt920+")");
			break;
		case 3:
			writer.write("Component type=Rectangle(");
			writer.write("colour="+colour);
            writer.write(", filledBox="+filledBox);
            writer.write(", colourAlpha="+colourAlpha+")");
			break;
		case 9:
			writer.write("Component type=[9](");
			writer.write("anInt985="+anInt985);
			writer.write("colour="+colour);
            writer.write(", aBool868="+aBool868+")");
			break;
		}
        writer.newLine();
        if (contextMenuOptions != null) {
        	writer.write("contextMenuOptions="+Arrays.toString(contextMenuOptions));
        	writer.newLine();
        }
        if (aClass407_984 != null) {
        	writer.write("aClass407_984="+aClass407_984.toString());
        	writer.newLine();
        }
		for (Field field : this.getClass().getDeclaredFields()) {
			if (field == null) {
				continue;
			}
			Object value = field.get(this);
			if (value == null) {
				continue;
			}
			
			if (value instanceof Object[] && !field.getName().equalsIgnoreCase("contextMenuOptions")) {
				writer.write(field.getName() + "->" + Arrays.toString((Object[]) value));
				writer.newLine();
			} else if (value instanceof int[]) {
				writer.write(field.getName() + "->" + Arrays.toString((int[]) value));
				writer.newLine();
			}

		}
		writer.flush();
	}
}
